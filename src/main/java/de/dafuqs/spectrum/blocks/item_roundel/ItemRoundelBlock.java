package de.dafuqs.spectrum.blocks.item_roundel;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.blocks.*;
import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;

public class ItemRoundelBlock extends InWorldInteractionBlock implements PaintbrushTriggered {
	
	public static final MapCodec<ItemRoundelBlock> CODEC = simpleCodec(ItemRoundelBlock::new);
	
	protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 14.0D, 12.0D);
	
	public ItemRoundelBlock(Properties settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends ItemRoundelBlock> codec() {
		return CODEC;
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ItemRoundelBlockEntity(pos, state);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	public ItemInteractionResult useItemOn(ItemStack handStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemInteractionResult actionResult = checkAndDoPaintbrushTrigger(state, world, pos, player, hand, hit);
		if (actionResult.consumesAction()) {
			return actionResult;
		}
		
		if (world.isClientSide) {
			return ItemInteractionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof ItemRoundelBlockEntity itemRoundelBlockEntity) {
				if (player.isShiftKeyDown() || handStack.isEmpty()) {
					retrieveLastStack(world, pos, player, hand, handStack, itemRoundelBlockEntity);
				} else {
					inputHandStack(world, player, hand, handStack, itemRoundelBlockEntity);
				}
			}
			return ItemInteractionResult.CONSUME;
		}
	}
	
	@Override
	public ItemInteractionResult onPaintBrushTrigger(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof ItemRoundelBlockEntity itemRoundelBlockEntity) {
			itemRoundelBlockEntity.reverse();
			return ItemInteractionResult.sidedSuccess(world.isClientSide);
		}
		return ItemInteractionResult.FAIL;
	}
	
}
