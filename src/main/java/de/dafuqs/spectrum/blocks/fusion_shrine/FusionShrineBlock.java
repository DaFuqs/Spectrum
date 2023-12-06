package de.dafuqs.spectrum.blocks.fusion_shrine;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.inventories.storage.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.*;
import net.fabricmc.fabric.impl.transfer.context.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.api.*;

@SuppressWarnings("UnstableApiUsage")
public class FusionShrineBlock extends InWorldInteractionBlock {
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("collect_all_basic_pigments_besides_brown");
	public static final IntProperty LIGHT_LEVEL = IntProperty.of("light_level", 0, 15);
	protected static final VoxelShape SHAPE;
	
	public FusionShrineBlock(Settings settings) {
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(LIGHT_LEVEL, 0));
	}
	
	public static void clearCurrentlyRenderedMultiBlock(World world) {
		if (world.isClient) {
			IMultiblock currentlyRenderedMultiBlock = PatchouliAPI.get().getCurrentMultiblock();
			if (currentlyRenderedMultiBlock != null && currentlyRenderedMultiBlock.getID().equals(SpectrumMultiblocks.FUSION_SHRINE_IDENTIFIER)) {
				PatchouliAPI.get().clearMultiblock();
			}
		}
	}
	
	public static boolean verifyStructureWithSkyAccess(World world, BlockPos blockPos) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (!world.getBlockState(blockPos.up()).isAir()) {
			world.playSound(null, blockPos, SpectrumSoundEvents.USE_FAIL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			return false;
		}
		if (!world.isSkyVisible(blockPos) && client.player != null) {
			if (world.isClient) {
				world.addParticle(SpectrumParticleTypes.RED_SPARKLE_RISING, blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, 0, 0.5, 0);
				client.player.playSound(SpectrumSoundEvents.USE_FAIL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			}
			return false;
		}
		return FusionShrineBlock.verifyStructure(world, blockPos, null);
	}
	
	private static boolean verifyStructure(World world, BlockPos blockPos, @Nullable ServerPlayerEntity serverPlayerEntity) {
		IMultiblock multiblock = SpectrumMultiblocks.MULTIBLOCKS.get(SpectrumMultiblocks.FUSION_SHRINE_IDENTIFIER);
		boolean valid = multiblock.validate(world, blockPos.down(), BlockRotation.NONE);
		
		if (valid) {
			if (serverPlayerEntity != null) {
				SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger(serverPlayerEntity, multiblock);
			}
		} else {
			if (world.isClient) {
				IMultiblock currentMultiBlock = PatchouliAPI.get().getCurrentMultiblock();
				if (currentMultiBlock == multiblock) {
					PatchouliAPI.get().clearMultiblock();
				} else {
					PatchouliAPI.get().showMultiblock(multiblock, Text.translatable("multiblock.spectrum.fusion_shrine.structure"), blockPos.down(2), BlockRotation.NONE);
				}
			} else {
				scatterContents(world, blockPos);
			}
		}
		
		return valid;
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(LIGHT_LEVEL);
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new FusionShrineBlockEntity(pos, state);
	}
	
	@Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		if(world.getBlockEntity(pos) instanceof FusionShrineBlockEntity blockEntity) {
			DefaultedList<ItemStack> inventory = blockEntity.getItems();
			
			int i = 0;
	        float f = 0.0f;
	        for (int j = 0; j < inventory.size(); ++j) {
	            ItemStack itemStack = blockEntity.getStack(j);
	            if (itemStack.isEmpty()) continue;
	            f += (float)itemStack.getCount() / (float)Math.min(blockEntity.getMaxCountPerStack(), itemStack.getMaxCount());
	            ++i;
	        }
			
			if (blockEntity.fluidStorage.amount > 0) {
				f += (float)blockEntity.fluidStorage.amount / (float)blockEntity.fluidStorage.getCapacity();
				++i;
			}
			
	        return MathHelper.floor(f / ((float) inventory.size() + 1) * 14.0f) + (i > 0 ? 1 : 0);
		}
		
		return 0;
    }
	
	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		if (world.isClient()) {
			clearCurrentlyRenderedMultiBlock((World) world);
		}
	}
	
	@Override
	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		if(!world.isClient) {
			// Specially handle fluid items
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if(entity instanceof ItemEntity itemEntity && blockEntity instanceof FusionShrineBlockEntity fusionShrineBlockEntity) {
				SingleVariantStorage<FluidVariant> storage = fusionShrineBlockEntity.fluidStorage;
				ItemStack itemStack = itemEntity.getStack();

				// We're not considering stacked fluid storages for the time being
				if(itemStack.getCount() == 1) {
					Item item = itemStack.getItem();
					SingleSlotStorage<ItemVariant> slot = new DroppedItemStorage(item, itemStack.getNbt());
					SingleSlotContainerItemContext ctx = new SingleSlotContainerItemContext(slot);
					Storage<FluidVariant> fluidStorage = FluidStorage.ITEM.find(itemStack, ctx);

					if(fluidStorage != null) {
						boolean anyInserted = false;
						for(StorageView<FluidVariant> view : fluidStorage) {
							try(Transaction transaction = Transaction.openOuter()) {
								FluidVariant variant = view.getResource();
								long inserted = variant.isBlank() ? 0 : storage.insert(variant, view.getAmount(), transaction);
								long extracted = fluidStorage.extract(variant, inserted, transaction);
								if(inserted == extracted && inserted != 0) {
									anyInserted = true;
									transaction.commit();
								}
							}
						}

						if(!anyInserted && !storage.getResource().isBlank()) {
							try(Transaction transaction = Transaction.openOuter()) {
								long inserted = fluidStorage.insert(storage.getResource(), storage.getAmount(), transaction);
								long extracted = storage.extract(storage.getResource(), inserted, transaction);
								if(inserted == extracted && inserted != 0) {
									transaction.commit();
								}
							}
						}

						itemEntity.setStack(slot.getResource().toStack(itemStack.getCount()));
						return;
					}
				}
			}

			// do not pick up items that were results of crafting
			if(entity.getPos().x % 0.5 != 0 && entity.getPos().z % 0.5 != 0) {
				super.onLandedUpon(world, state, pos, entity, fallDistance);
			}
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			verifyStructureWithSkyAccess(world, pos);
			return ActionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			// if the structure is valid the player can put / retrieve items and fluids into the shrine
			if (blockEntity instanceof FusionShrineBlockEntity fusionShrineBlockEntity && verifyStructure(world, pos, (ServerPlayerEntity) player)) {
				fusionShrineBlockEntity.setOwner(player);

				ItemStack handStack = player.getStackInHand(hand);
				if (FluidStorageUtil.interactWithFluidStorage(fusionShrineBlockEntity.fluidStorage, player, hand)
				|| (player.isSneaking() || handStack.isEmpty()) && retrieveLastStack(world, pos, player, hand, handStack, fusionShrineBlockEntity)
				|| !handStack.isEmpty() && inputHandStack(world, player, hand, handStack, fusionShrineBlockEntity)) {
					fusionShrineBlockEntity.updateInClientWorld();
				}
			}
			
			return ActionResult.CONSUME;
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (world.isClient) {
			return checkType(type, SpectrumBlockEntities.FUSION_SHRINE, FusionShrineBlockEntity::clientTick);
		} else {
			return checkType(type, SpectrumBlockEntities.FUSION_SHRINE, FusionShrineBlockEntity::serverTick);
		}
	}
	
	static {
		VoxelShape neck = Block.createCuboidShape(2, 0, 2, 14, 12, 14);
		VoxelShape head = Block.createCuboidShape(1, 12, 1, 15, 15, 15);
		VoxelShape crystal = Block.createCuboidShape(6.5, 13, 6.5, 9.5, 23, 9.5);
		neck = VoxelShapes.union(neck, head);
		SHAPE = VoxelShapes.union(neck, crystal);
	}
}
