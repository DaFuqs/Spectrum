package de.dafuqs.spectrum.blocks.bottomless_bundle;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BottomlessBundleBlock extends BlockWithEntity {
	
	public BottomlessBundleBlock(Settings settings) {
		super(settings);
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
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient && player.isSneaking()) {
			world.getBlockEntity(pos, SpectrumBlockEntityRegistry.BOTTOMLESS_BUNDLE).ifPresent((bottomlessBundleBlockEntity) -> {
				ItemStack itemStack = bottomlessBundleBlockEntity.retrieveVoidBundle();
				
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				
				ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, itemStack);
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
		if(world != null) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if(blockEntity instanceof BottomlessBundleBlockEntity bottomlessBundleBlockEntity) {
				return bottomlessBundleBlockEntity.retrieveVoidBundle();
			}
		}
		return SpectrumItems.BOTTOMLESS_BUNDLE.getDefaultStack();
	}
	
	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient && !player.isCreative()) {
			world.getBlockEntity(pos, SpectrumBlockEntityRegistry.BOTTOMLESS_BUNDLE).ifPresent((bottomlessBundleBlockEntity) -> {
				ItemStack itemStack = bottomlessBundleBlockEntity.retrieveVoidBundle();
				ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, itemStack);
				itemEntity.setToDefaultPickupDelay();
				world.spawnEntity(itemEntity);
			});
		}
		super.onBreak(world, pos, state, player);
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if(!world.isClient) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof BottomlessBundleBlockEntity bottomlessBundleBlockEntity) {
				bottomlessBundleBlockEntity.setVoidBundle(itemStack);
			}
		}
	}
	
	@Override
	public MutableText getName() {
		return new TranslatableText("item.spectrum.void_bundle");
	}
	
}
