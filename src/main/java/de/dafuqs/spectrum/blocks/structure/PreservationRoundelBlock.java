package de.dafuqs.spectrum.blocks.structure;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.item_roundel.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.core.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.*;

public class PreservationRoundelBlock extends ItemRoundelBlock {
	
	public static final MapCodec<PreservationRoundelBlock> CODEC = simpleCodec(PreservationRoundelBlock::new);
	
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	
	public PreservationRoundelBlock(Properties settings) {
		super(settings);
		this.registerDefaultState((this.stateDefinition.any()).setValue(FACING, Direction.NORTH));
	}
	
	@Override
	public MapCodec<? extends PreservationRoundelBlock> codec() {
		return CODEC;
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PreservationRoundelBlockEntity(pos, state);
	}
	
	@Override
	public ItemInteractionResult useItemOn(ItemStack handStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide) {
			return ItemInteractionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof ItemRoundelBlockEntity itemRoundelBlockEntity) {
				if (player.isShiftKeyDown() || handStack.isEmpty()) {
					retrieveLastStack(world, pos, player, hand, handStack, itemRoundelBlockEntity);
				} else {
					int countBefore = handStack.getCount();
					ItemStack leftoverStack = InventoryHelper.addToInventoryUpToSingleStackWithMaxTotalCount(handStack, itemRoundelBlockEntity, PreservationRoundelBlockEntity.INVENTORY_SIZE);
					player.setItemInHand(hand, leftoverStack);
					if (countBefore != leftoverStack.getCount()) {
						world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
						itemRoundelBlockEntity.inventoryChanged();
					}
				}
			}
			return ItemInteractionResult.CONSUME;
		}
	}
	
	
	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}
	
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
}
