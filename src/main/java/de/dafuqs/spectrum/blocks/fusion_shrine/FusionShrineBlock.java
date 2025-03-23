package de.dafuqs.spectrum.blocks.fusion_shrine;

import com.klikli_dev.modonomicon.api.multiblock.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import de.dafuqs.spectrum.inventories.storage.*;
import de.dafuqs.spectrum.networking.*;
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
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

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
			if (world.isClient()) {
				ModonomiconHelper.clearRenderedMultiblock(SpectrumMultiblocks.get(SpectrumMultiblocks.FUSION_SHRINE));
			}
		}
	}
	
	public static boolean verifySkyAccess(ServerWorld world, BlockPos shrinePos) {
		if (!world.getBlockState(shrinePos.up()).isSolidBlock(world, shrinePos.up())) {
			world.playSound(null, shrinePos, SpectrumSoundEvents.USE_FAIL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity(world, shrinePos.up().toCenterPos(), SpectrumParticleTypes.RED_SPARKLE_RISING, 8, Vec3d.ZERO, new Vec3d(0.1, 0.1, 0.1));
			return false;
		}
		
		// getTopY() returns the topmost "air" block
		// which may or may not be the pos of the Fusion Shrine
		// we search down until we find the shrine itself or a non-opaque block
		int topY = world.getTopY(Heightmap.Type.WORLD_SURFACE, shrinePos.getX(), shrinePos.getZ());
		BlockPos.Mutable mutablePos = new BlockPos.Mutable(shrinePos.getX(), topY, shrinePos.getZ());
		for (int y = topY; y > shrinePos.getY(); y--) {
			mutablePos.setY(y - 1);
			BlockState posState = world.getBlockState(mutablePos);
			if (posState.getOpacity(world, mutablePos) > 0) {
				break;
			}
		}
		
		if (mutablePos.getY() == shrinePos.getY()) {
			return true;
		}
		
		SpectrumS2CPacketSender.playParticleWithExactVelocity(world, new Vec3d(shrinePos.getX() + 0.5, shrinePos.getY() + 1, shrinePos.getZ() + 0.5), SpectrumParticleTypes.RED_SPARKLE_RISING, 1, new Vec3d(0, 0.5, 0));
		SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity(world, new Vec3d(shrinePos.getX() + 0.5, topY - 0.5, shrinePos.getZ() + 0.5), SpectrumParticleTypes.RED_SPARKLE_RISING, 8, Vec3d.ZERO, new Vec3d(0.1, 0.1, 0.1));
		world.playSound(null, shrinePos, SpectrumSoundEvents.USE_FAIL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
		return false;
	}
	
	public static boolean verifyStructure(World world, BlockPos blockPos, @Nullable ServerPlayerEntity serverPlayerEntity) {
		Multiblock multiblock = SpectrumMultiblocks.get(SpectrumMultiblocks.FUSION_SHRINE);
		boolean valid = multiblock.validate(world, blockPos.down(), BlockRotation.NONE);
		
		if (valid) {
			if (serverPlayerEntity != null) {
				SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger(serverPlayerEntity, multiblock);
			}
		} else {
			if (world.isClient) {
				ModonomiconHelper.renderMultiblock(multiblock, SpectrumMultiblocks.FUSION_SHRINE_TEXT, blockPos.down(2), BlockRotation.NONE);
			} else if (world.getBlockEntity(blockPos) instanceof FusionShrineBlockEntity fusionShrineBlockEntity) {
				fusionShrineBlockEntity.scatterContents(world);
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
		if (world.getBlockEntity(pos) instanceof FusionShrineBlockEntity blockEntity) {
			DefaultedList<ItemStack> inventory = blockEntity.getItems();
			
			int i = 0;
			float f = 0.0f;
			for (int j = 0; j < inventory.size(); ++j) {
				ItemStack itemStack = blockEntity.getStack(j);
				if (itemStack.isEmpty()) continue;
				f += (float) itemStack.getCount() / (float) Math.min(blockEntity.getMaxCountPerStack(), itemStack.getMaxCount());
				++i;
			}
			
			if (blockEntity.fluidStorage.amount > 0) {
				f += (float) blockEntity.fluidStorage.amount / (float) blockEntity.fluidStorage.getCapacity();
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
		if (!world.isClient) {
			// Specially handle fluid items
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (entity instanceof ItemEntity itemEntity && blockEntity instanceof FusionShrineBlockEntity fusionShrineBlockEntity) {
				SingleVariantStorage<FluidVariant> storage = fusionShrineBlockEntity.fluidStorage;
				ItemStack itemStack = itemEntity.getStack();
				
				// We're not considering stacked fluid storages for the time being
				if (itemStack.getCount() == 1) {
					Item item = itemStack.getItem();
					SingleSlotStorage<ItemVariant> slot = new DroppedItemStorage(item, itemStack.getNbt());
					SingleSlotContainerItemContext ctx = new SingleSlotContainerItemContext(slot);
					Storage<FluidVariant> fluidStorage = FluidStorage.ITEM.find(itemStack, ctx);
					
					if (fluidStorage != null) {
						boolean anyInserted = false;
						for (StorageView<FluidVariant> view : fluidStorage) {
							try (Transaction transaction = Transaction.openOuter()) {
								FluidVariant variant = view.getResource();
								long inserted = variant.isBlank() ? 0 : storage.insert(variant, view.getAmount(), transaction);
								long extracted = fluidStorage.extract(variant, inserted, transaction);
								if (inserted == extracted && inserted != 0) {
									anyInserted = true;
									transaction.commit();
								}
							}
						}
						
						if (!anyInserted && !storage.getResource().isBlank()) {
							try (Transaction transaction = Transaction.openOuter()) {
								long inserted = fluidStorage.insert(storage.getResource(), storage.getAmount(), transaction);
								long extracted = storage.extract(storage.getResource(), inserted, transaction);
								if (inserted == extracted && inserted != 0) {
									transaction.commit();
								}
							}
						}
						
						itemEntity.setStack(slot.getResource().toStack(itemStack.getCount()));
						fusionShrineBlockEntity.inventoryChanged();
						return;
					}
				}
			}
			
			// do not pick up items that were results of crafting
			if (entity.getPos().x % 0.5 != 0 && entity.getPos().z % 0.5 != 0) {
				super.onLandedUpon(world, state, pos, entity, fallDistance);
			}
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			verifyStructure(world, pos, null);
			return ActionResult.SUCCESS;
		} else {
			verifySkyAccess((ServerWorld) world, pos);
			
			// if the structure is valid the player can put / retrieve items and fluids into the shrine
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof FusionShrineBlockEntity fusionShrineBlockEntity && verifyStructure(world, pos, (ServerPlayerEntity) player)) {
				fusionShrineBlockEntity.setOwner(player);
				
				ItemStack handStack = player.getStackInHand(hand);
				if (FluidStorageUtil.interactWithFluidStorage(fusionShrineBlockEntity.fluidStorage, player, hand)) {
					fusionShrineBlockEntity.inventoryChanged();
					return ActionResult.CONSUME;
				}
				if ((player.isSneaking() || handStack.isEmpty()) && retrieveLastStack(world, pos, player, hand, handStack, fusionShrineBlockEntity)) {
					fusionShrineBlockEntity.inventoryChanged();
					return ActionResult.CONSUME;
				}
				if (!handStack.isEmpty() && inputHandStack(world, player, hand, handStack, fusionShrineBlockEntity)) {
					fusionShrineBlockEntity.inventoryChanged();
					return ActionResult.CONSUME;
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
