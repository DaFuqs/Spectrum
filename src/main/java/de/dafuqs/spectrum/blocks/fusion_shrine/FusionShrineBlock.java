package de.dafuqs.spectrum.blocks.fusion_shrine;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.InWorldInteractionBlock;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import de.dafuqs.spectrum.registries.SpectrumMultiblocks;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.fabric.mixin.transfer.BucketItemAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

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
	
	public static boolean verifyStructureWithSkyAccess(World world, BlockPos blockPos, @Nullable ServerPlayerEntity serverPlayerEntity) {
		if (!world.getBlockState(blockPos.up()).isAir()) {
			world.playSound(null, blockPos, SpectrumSoundEvents.USE_FAIL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			return false;
		}
		if (!world.isSkyVisible(blockPos)) {
			if (world.isClient) {
				world.addParticle(SpectrumParticleTypes.RED_SPARKLE_RISING, blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, 0, 0.5, 0);
				MinecraftClient.getInstance().player.playSound(SpectrumSoundEvents.USE_FAIL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			}
			return false;
		}
		return FusionShrineBlock.verifyStructure(world, blockPos, serverPlayerEntity);
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
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		if (world.isClient()) {
			clearCurrentlyRenderedMultiBlock((World) world);
		}
	}
	
	@Override
	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		// do not pick up items that were results of crafting
		if (!world.isClient && entity.getPos().x % 0.5 != 0 && entity.getPos().z % 0.5 != 0) {
			super.onLandedUpon(world, state, pos, entity, fallDistance);
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			verifyStructureWithSkyAccess(world, pos, null);
			return ActionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof FusionShrineBlockEntity fusionShrineBlockEntity) {
				
				ItemStack handStack = player.getStackInHand(hand);
				if (handStack.getItem() instanceof BucketItem) {
					fusionShrineBlockEntity.setOwner(player);
					
					Fluid storedFluid = fusionShrineBlockEntity.getFluid();
					Fluid bucketFluid = ((BucketItemAccessor) handStack.getItem()).fabric_getFluid();
					if (storedFluid == Fluids.EMPTY && bucketFluid != Fluids.EMPTY) {
						fusionShrineBlockEntity.setFluid(bucketFluid);
						if (!player.isCreative()) {
							handStack.decrement(1);
							player.setStackInHand(hand, handStack);
							
							player.giveItemStack(Items.BUCKET.getDefaultStack());
						}
						
						fusionShrineBlockEntity.inventoryChanged();
						if(bucketFluid.getBucketFillSound().isPresent()) {
							world.playSound(null, player.getBlockPos(), bucketFluid.getBucketFillSound().get(), SoundCategory.PLAYERS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
						}
					} else if (storedFluid != Fluids.EMPTY && bucketFluid == Fluids.EMPTY) {
						fusionShrineBlockEntity.setFluid(Fluids.EMPTY);
						if (!player.isCreative()) {
							handStack.decrement(1);
							player.setStackInHand(hand, handStack);
							
							player.giveItemStack(new ItemStack(storedFluid.getBucketItem()));
						}
						
						fusionShrineBlockEntity.inventoryChanged();
						if(storedFluid.getBucketFillSound().isPresent()) {
							world.playSound(null, player.getBlockPos(), storedFluid.getBucketFillSound().get(), SoundCategory.PLAYERS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
						}
					}
				} else {
					// if the structure is valid the player can put / retrieve blocks into the shrine
					if (player.isSneaking() || handStack.isEmpty()) {
						// sneaking or empty hand: remove items
						if(retrieveLastStack(world, pos, player, hand, handStack, fusionShrineBlockEntity)) {
							fusionShrineBlockEntity.setOwner(player);
						}
					} else if (verifyStructure(world, pos, (ServerPlayerEntity) player) && !handStack.isEmpty()) {
						if(inputHandStack(world, player, hand, handStack, fusionShrineBlockEntity)) {
							fusionShrineBlockEntity.setOwner(player);
						}
					}
				}
			}
			
			return ActionResult.CONSUME;
		}
	}
	
	@Override
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
	
	// drop all currently stored items
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!newState.getBlock().equals(this)) { // happens when filling with fluid
			super.onStateReplaced(state, world, pos, newState, moved);
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
