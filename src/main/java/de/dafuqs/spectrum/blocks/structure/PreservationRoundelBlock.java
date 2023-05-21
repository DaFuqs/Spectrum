package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.blocks.item_roundel.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class PreservationRoundelBlock extends ItemRoundelBlock {
	
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	
	public PreservationRoundelBlock(Settings settings) {
		super(settings);
		this.setDefaultState((this.stateManager.getDefaultState()).with(FACING, Direction.NORTH));
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new PreservationRoundelBlockEntity(pos, state);
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
					int countBefore = handStack.getCount();
					ItemStack leftoverStack = InventoryHelper.addToInventoryUpToSingleStackWithMaxTotalCount(handStack, itemRoundelBlockEntity, PreservationRoundelBlockEntity.INVENTORY_SIZE);
					player.setStackInHand(hand, leftoverStack);
					if (countBefore != leftoverStack.getCount()) {
						world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
						itemRoundelBlockEntity.inventoryChanged();
					}
				}
			}
			return ActionResult.CONSUME;
		}
	}
	
	
	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}
	
	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
}
