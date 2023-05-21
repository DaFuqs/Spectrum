package de.dafuqs.spectrum.blocks.item_roundel;

import de.dafuqs.spectrum.blocks.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

public class ItemRoundelBlock extends InWorldInteractionBlock {
	
	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	
	public ItemRoundelBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ItemRoundelBlockEntity(pos, state);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof ItemRoundelBlockEntity itemRoundelBlockEntity) {
				ItemStack handStack = player.getStackInHand(hand);
				if (player.isSneaking() || handStack.isEmpty()) {
					retrieveLastStack(world, pos, player, hand, handStack, itemRoundelBlockEntity);
				} else {
					inputHandStack(world, player, hand, handStack, itemRoundelBlockEntity);
				}
			}
			return ActionResult.CONSUME;
		}
	}
	
}
