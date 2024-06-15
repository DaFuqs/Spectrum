package de.dafuqs.spectrum.blocks.redstone;

import de.dafuqs.spectrum.compat.claims.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.screen.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import org.jetbrains.annotations.*;

public class BlockPlacerBlock extends RedstoneInteractionBlock implements BlockEntityProvider {
	
	public BlockPlacerBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new BlockPlacerBlockEntity(pos, state);
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			if (world.getBlockEntity(pos) instanceof BlockPlacerBlockEntity blockPlacerBlockEntity) {
				player.openHandledScreen(blockPlacerBlockEntity);
			}
			return ActionResult.CONSUME;
		}
	}
	
	protected void dispense(ServerWorld world, BlockPos pos) {
		BlockPointerImpl pointer = new BlockPointerImpl(world, pos);
		BlockPlacerBlockEntity blockEntity = pointer.getBlockEntity();
		
		int slot = blockEntity.chooseNonEmptySlot(world.random);
		if (slot < 0) {
			world.syncWorldEvent(WorldEvents.DISPENSER_FAILS, pos, 0);
			world.emitGameEvent(null, GameEvent.BLOCK_ACTIVATE, pos);
		} else {
			ItemStack stack = blockEntity.getStack(slot);
			tryPlace(stack, pointer);
		}
	}
	
	// We can't reuse the vanilla BlockPlacementDispenserBehavior, since we are using a different orientation for our block:
	// BlockPlacerBlock.ORIENTATION instead of DispenserBlock.FACING
	protected void tryPlace(@NotNull ItemStack stack, BlockPointer pointer) {
        World world = pointer.getWorld();
        if (stack.getItem() instanceof BlockItem blockItem) {
			Direction facing = pointer.getBlockState().get(BlockPlacerBlock.ORIENTATION).getFacing();
			BlockPos placementPos = pointer.getPos().offset(facing);
            Direction placementDirection = world.isAir(placementPos.down()) ? facing : Direction.UP;
			
			if (!GenericClaimModsCompat.canPlaceBlock(world, placementPos, null)) {
				return;
			}
			
			try {
				blockItem.place(new BlockPlacerPlacementContext(world, placementPos, facing, stack, placementDirection));
				world.syncWorldEvent(WorldEvents.DISPENSER_DISPENSES, pointer.getPos(), 0);
				world.syncWorldEvent(WorldEvents.DISPENSER_ACTIVATED, pointer.getPos(), pointer.getBlockState().get(BlockPlacerBlock.ORIENTATION).getFacing().getId());
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, placementPos);
			} catch (Exception ignored) {
			}
		} else {
			world.syncWorldEvent(WorldEvents.DISPENSER_FAILS, pointer.getPos(), 0);
            world.emitGameEvent(null, GameEvent.BLOCK_ACTIVATE, pointer.getPos());
		}
	}
	
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
		boolean bl = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.up());
		boolean bl2 = state.get(TRIGGERED);
		if (bl && !bl2) {
			world.scheduleBlockTick(pos, this, 4);
			world.setBlockState(pos, state.with(TRIGGERED, true), 4);
		} else if (!bl && bl2) {
			world.setBlockState(pos, state.with(TRIGGERED, false), 4);
		}
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		this.dispense(world, pos);
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (itemStack.hasCustomName()) {
			if (world.getBlockEntity(pos) instanceof BlockPlacerBlockEntity blockPlacerBlockEntity) {
				blockPlacerBlockEntity.setCustomName(itemStack.getName());
			}
		}
		
	}
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) {
			if (world.getBlockEntity(pos) instanceof BlockPlacerBlockEntity blockPlacerBlockEntity) {
				ItemScatterer.spawn(world, pos, blockPlacerBlockEntity);
				world.updateComparators(pos, this);
			}
			
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}
	
	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}
	
	public static final class BlockPlacerPlacementContext extends AutomaticItemPlacementContext {
		
		public BlockPlacerPlacementContext(World world, BlockPos pos, Direction facing, ItemStack stack, Direction side) {
			super(world, pos, facing, stack, side);
		}
		
		// SlabBlocks cause a non-funny StackOverflowError
		// at net.minecraft.block.SlabBlock.canReplace(SlabBlock.java)
		// at net.minecraft.block.AbstractBlock$AbstractBlockState.canReplace(AbstractBlock.java)
		// at net.minecraft.item.AutomaticItemPlacementContext.canPlace(AutomaticItemPlacementContext.java)
		// at net.minecraft.item.AutomaticItemPlacementContext.canReplaceExisting(AutomaticItemPlacementContext.java)
		// at net.minecraft.block.SlabBlock.canReplace(SlabBlock.java)
		@Override
		public boolean canReplaceExisting() {
			return false;
		}
		
	}
	
}
