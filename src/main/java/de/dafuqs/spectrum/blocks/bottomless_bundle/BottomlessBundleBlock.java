package de.dafuqs.spectrum.blocks.bottomless_bundle;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BottomlessBundleBlock extends BaseEntityBlock {
	
	public static final MapCodec<BottomlessBundleBlock> CODEC = simpleCodec(BottomlessBundleBlock::new);
	public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
	public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;
	public static final int MAX_ROTATIONS = RotationSegment.getMaxSegmentIndex() + 1;
	
	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);
	
	public BottomlessBundleBlock(Properties settings) {
		super(settings);
		registerDefaultState(defaultBlockState().setValue(LOCKED, false));
	}
	
	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BottomlessBundleBlockEntity(pos, state);
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}
	
	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}
	
	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!world.isClientSide) {
			if (player.isShiftKeyDown()) {
				BlockEntity be = world.getBlockEntity(pos);
				if (be instanceof BottomlessBundleBlockEntity bottomlessBundleBlockEntity) {
					long amount = bottomlessBundleBlockEntity.storage().count;
					ItemStack variant = bottomlessBundleBlockEntity.storage().variant;
					long maxStoredAmount = bottomlessBundleBlockEntity.storage().getCapacity();
					if (variant == null || variant.isEmpty()) {
						player.displayClientMessage(Component.translatable("item.spectrum.bottomless_bundle.tooltip.empty"), true);
					} else {
						player.displayClientMessage(Component.translatable("item.spectrum.bottomless_bundle.tooltip.count_of", amount, maxStoredAmount).append(variant.getItem().getDescription()), true);
					}
				}
			} else {
				BlockEntity be = world.getBlockEntity(pos);
				if (be instanceof BottomlessBundleBlockEntity bottomlessBundleBlockEntity) {
					BottomlessItemHandler storage = bottomlessBundleBlockEntity.storage();
					ItemStack storedVariant = storage.variant;
					
					// If same type or empty template -> try to insert
					if ((storedVariant.isEmpty() || ItemStack.isSameItemSameComponents(storedVariant, stack))) {
						if (!stack.isEmpty() && stack.getItem().canFitInsideContainerItems(stack)) {
							ItemStack remainder = storage.insertItem(0, stack, false);
							stack.setCount(remainder.getCount());
							world.playSound(null, pos, SoundEvents.BUNDLE_INSERT, SoundSource.BLOCKS, 0.8F, 0.8F + world.getRandom().nextFloat() * 0.4F);
						}
					} else {
						// extract one stack worth (or up to available amount)
						if (!storage.variant.isEmpty() && storage.count > 0) {
							int extractCount = Math.min(storage.variant.getItem().getDefaultMaxStackSize(), (int) Math.min(Integer.MAX_VALUE, storage.count));
							ItemStack removed = storage.variant.copyWithCount(extractCount);
							storage.count -= extractCount;
							if (storage.count <= 0) {
								storage.variant = ItemStack.EMPTY;
								storage.count = 0;
							}
							player.getInventory().placeItemBackInInventory(removed);
							world.playSound(null, pos, SoundEvents.BUNDLE_REMOVE_ONE, SoundSource.BLOCKS, 0.8F, 0.8F + world.getRandom().nextFloat() * 0.4F);
						}
					}
					
					bottomlessBundleBlockEntity.setChanged();
				}
			}
			return ItemInteractionResult.CONSUME;
		}
		return ItemInteractionResult.SUCCESS;
	}
	
	@Override
	public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
		return SpectrumBlocks.BOTTOMLESS_BUNDLE.asItem().getDefaultInstance();
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		BlockEntity blockEntity = builder.getParameter(LootContextParams.BLOCK_ENTITY);
		if (blockEntity instanceof BottomlessBundleBlockEntity bottomlessBundleBlockEntity) {
			return List.of(bottomlessBundleBlockEntity.retrieveBundle());
		} else {
			return super.getDrops(state, builder);
		}
	}
	
	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}
	
	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof BottomlessBundleBlockEntity bottomlessBundleBlockEntity) {
			float curr = bottomlessBundleBlockEntity.storage().count;
			float max = bottomlessBundleBlockEntity.storage().getCapacity();
			int signal = Mth.floor(curr / max * 14.0f) + (curr > 0 ? 1 : 0);
			return signal;
		}
		
		return 0;
	}
	
	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.is(newState.getBlock())) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof BottomlessBundleBlockEntity) {
				world.updateNeighbourForOutputSignal(pos, this);
			}
			super.onRemove(state, world, pos, newState, moved);
		}
	}
	
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return super.getStateForPlacement(ctx)
				.setValue(ROTATION, RotationSegment.convertToSegment(ctx.getRotation()))
				.setValue(LOCKED, ctx.getItemInHand().has(DataComponents.LOCK));
	}
	
	protected BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(ROTATION, rotation.rotate(state.getValue(ROTATION), MAX_ROTATIONS));
	}
	
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return state.setValue(ROTATION, mirror.mirror(state.getValue(ROTATION), MAX_ROTATIONS));
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (!world.isClientSide) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof BottomlessBundleBlockEntity bottomlessBundleBlockEntity) {
				bottomlessBundleBlockEntity.setBundle(itemStack.copy());
				world.updateNeighbourForOutputSignal(pos, this);
			}
		}
	}
	
	@Override
	public MutableComponent getName() {
		return Component.translatable("item.spectrum.bottomless_bundle");
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.ROTATION_16, LOCKED);
	}
}