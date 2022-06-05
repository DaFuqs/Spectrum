package de.dafuqs.spectrum.blocks.bottomless_bundle;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
		if (!world.isClient && player.isSneaking()) {
			world.getBlockEntity(pos, SpectrumBlockEntityRegistry.BOTTOMLESS_BUNDLE).ifPresent((bottomlessBundleBlockEntity) -> {
				ItemStack itemStack = bottomlessBundleBlockEntity.retrieveVoidBundle();
				
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				
				ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, itemStack);
				itemEntity.setToDefaultPickupDelay();
				world.spawnEntity(itemEntity);
				
				itemEntity.onPlayerCollision(player); // auto pickup
			});
			return ActionResult.CONSUME;
		}
		return ActionResult.SUCCESS;
	}
	
	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return SpectrumItems.BOTTOMLESS_BUNDLE.getDefaultStack();
	}
	
	// for automatic destruction, like pistons
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		BlockEntity blockEntity = builder.getNullable(LootContextParameters.BLOCK_ENTITY);
		if (blockEntity instanceof BottomlessBundleBlockEntity bottomlessBundleBlockEntity) {
			return List.of(bottomlessBundleBlockEntity.retrieveVoidBundle());
		} else {
			return super.getDroppedStacks(state, builder);
		}
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof BottomlessBundleBlockEntity bottomlessBundleBlockEntity) {
				bottomlessBundleBlockEntity.setVoidBundle(itemStack.copy());
			}
		}
	}
	
	@Override
	public MutableText getName() {
		return new TranslatableText("item.spectrum.void_bundle");
	}
	
}
