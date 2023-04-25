package de.dafuqs.spectrum.blocks.bottomless_bundle;

import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.block.piston.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.loot.context.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BottomlessBundleBlock extends BlockWithEntity {
	
	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	
	public BottomlessBundleBlock(Settings settings) {
		super(settings);
	}
	
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new BottomlessBundleBlockEntity(pos, state);
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.DESTROY;
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			if (player.isSneaking()) {
				world.getBlockEntity(pos, SpectrumBlockEntities.BOTTOMLESS_BUNDLE).ifPresent((bottomlessBundleBlockEntity) -> {
					ItemStack itemStack = bottomlessBundleBlockEntity.retrieveBundle();

					world.setBlockState(pos, Blocks.AIR.getDefaultState());

					ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, itemStack);
					itemEntity.setToDefaultPickupDelay();
					world.spawnEntity(itemEntity);

					itemEntity.onPlayerCollision(player); // auto pickup
				});
			} else {
				world.getBlockEntity(pos, SpectrumBlockEntities.BOTTOMLESS_BUNDLE).ifPresent((bottomlessBundleBlockEntity) -> {
					long amount = bottomlessBundleBlockEntity.storage.amount;
					if (amount == 0) {
						player.sendMessage(Text.translatable("item.spectrum.bottomless_bundle.tooltip.empty"), true);
					} else {
						ItemVariant variant = bottomlessBundleBlockEntity.storage.variant;
						long maxStoredAmount = BottomlessBundleItem.getMaxStoredAmount(bottomlessBundleBlockEntity.bottomlessBundleStack);
						player.sendMessage(Text.translatable("item.spectrum.bottomless_bundle.tooltip.count_of", amount, maxStoredAmount).append(variant.getItem().getName()), true);
					}
				});
			}
			return ActionResult.CONSUME;
		}
		return ActionResult.SUCCESS;
	}
	
	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return SpectrumItems.BOTTOMLESS_BUNDLE.getDefaultStack();
	}
	
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		BlockEntity blockEntity = builder.getNullable(LootContextParameters.BLOCK_ENTITY);
		if (blockEntity instanceof BottomlessBundleBlockEntity bottomlessBundleBlockEntity) {
			return List.of(bottomlessBundleBlockEntity.retrieveBundle());
		} else {
			return super.getDroppedStacks(state, builder);
		}
	}
	
	@Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }
	
	@Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof BottomlessBundleBlockEntity bottomlessBundleBlockEntity) {
			float curr = bottomlessBundleBlockEntity.storage.amount;
			float max = bottomlessBundleBlockEntity.storage.getCapacity();
			return MathHelper.floor(curr / max * 14.0f) + curr > 0 ? 1 : 0;
		}
		
		return 0;
    }
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof BottomlessBundleBlockEntity bottomlessBundleBlockEntity) {
				world.updateComparators(pos, this);
			}
			
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof BottomlessBundleBlockEntity bottomlessBundleBlockEntity) {
				bottomlessBundleBlockEntity.setBundle(itemStack.copy());
				world.updateComparators(pos, this);
			}
		}
	}
	
	@Override
	public MutableText getName() {
		return Text.translatable("item.spectrum.bottomless_bundle");
	}
	
}
