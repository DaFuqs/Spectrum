package de.dafuqs.spectrum.blocks.redstone;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.dispenser.*;
import net.minecraft.core.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

public class BlockPlacerBlock extends RedstoneInteractionBlock implements EntityBlock {
	
	public static final MapCodec<BlockPlacerBlock> CODEC = simpleCodec(BlockPlacerBlock::new);
	
	public BlockPlacerBlock(Properties settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends BlockPlacerBlock> codec() {
		return CODEC;
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BlockPlacerBlockEntity(pos, state);
	}
	
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		if (world.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			if (world.getBlockEntity(pos) instanceof BlockPlacerBlockEntity blockPlacerBlockEntity) {
				player.openMenu(blockPlacerBlockEntity);
			}
			return InteractionResult.CONSUME;
		}
	}
	
	protected void dispense(ServerLevel world, BlockState state, BlockPos pos) {
		BlockPlacerBlockEntity blockEntity = world.getBlockEntity(pos, SpectrumBlockEntities.BLOCK_PLACER).orElse(null);
		if (blockEntity == null) {
			SpectrumCommon.LOGGER.warn("Ignoring block place attempt for Block Player without matching block entity at {}", pos);
		} else {
			BlockSource pointer = new BlockSource(world, pos, state, blockEntity);
			int slot = blockEntity.getRandomSlot(world.random);
			if (slot < 0) {
				world.levelEvent(LevelEvent.SOUND_DISPENSER_FAIL, pos, 0);
				world.gameEvent(GameEvent.BLOCK_ACTIVATE, pos, GameEvent.Context.of(blockEntity.getBlockState()));
			} else {
				ItemStack stack = blockEntity.getItem(slot);
				tryPlace(stack, pointer);
			}
		}
	}
	
	// We can't reuse the vanilla BlockPlacementDispenserBehavior, since we are using a different orientation for our block:
	// BlockPlacerBlock.ORIENTATION instead of DispenserBlock.FACING
	protected void tryPlace(@NotNull ItemStack stack, BlockSource pointer) {
		Level world = pointer.level();
        if (stack.getItem() instanceof BlockItem blockItem) {
			Direction facing = pointer.state().getValue(BlockPlacerBlock.ORIENTATION).front();
			BlockPos placementPos = pointer.pos().relative(facing);
			Direction placementDirection = world.isEmptyBlock(placementPos.below()) ? facing : Direction.UP;
			
			if (!GenericClaimModsCompat.canPlaceBlock(world, placementPos, null)) {
				return;
			}
			
			try {
				blockItem.place(new BlockPlacerPlacementContext(world, placementPos, facing, stack, placementDirection));
				world.levelEvent(LevelEvent.SOUND_DISPENSER_DISPENSE, pointer.pos(), 0);
				world.levelEvent(LevelEvent.PARTICLES_SHOOT_SMOKE, pointer.pos(), pointer.state().getValue(BlockPlacerBlock.ORIENTATION).front().get3DDataValue());
				world.gameEvent(null, GameEvent.BLOCK_PLACE, placementPos);
			} catch (Exception e) {
				SpectrumCommon.logError("Block Placer encountered an error placing a block at " + placementPos + " when placing " + BuiltInRegistries.ITEM.getKey(blockItem));
				e.printStackTrace();
			}
		} else {
			world.levelEvent(LevelEvent.SOUND_DISPENSER_FAIL, pointer.pos(), 0);
			world.gameEvent(null, GameEvent.BLOCK_ACTIVATE, pointer.pos());
		}
	}
	
	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
		boolean bl = world.hasNeighborSignal(pos) || world.hasNeighborSignal(pos.above());
		boolean bl2 = state.getValue(TRIGGERED);
		if (bl && !bl2) {
			world.scheduleTick(pos, this, 4);
			world.setBlock(pos, state.setValue(TRIGGERED, true), 4);
		} else if (!bl && bl2) {
			world.setBlock(pos, state.setValue(TRIGGERED, false), 4);
		}
	}
	
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		this.dispense(world, state, pos);
	}
	
	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		Containers.dropContentsOnDestroy(state, newState, world, pos);
		super.onRemove(state, world, pos, newState, moved);
	}
	
	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}
	
	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
	}
	
	public static final class BlockPlacerPlacementContext extends DirectionalPlaceContext {
		
		// Shadows the variable facing in the superclass.
		private final Direction facing;
		
		public BlockPlacerPlacementContext(Level world, BlockPos pos, Direction facing, ItemStack stack, Direction side) {
			super(world, pos, facing, stack, side);
			this.facing = facing;
		}
		
		@Override
		public @NotNull Direction getHorizontalDirection() {
			return facing.getOpposite();
		}
		
		// SlabBlocks cause a non-funny StackOverflowError
		// at net.minecraft.block.SlabBlock.canReplace(SlabBlock.java)
		// at net.minecraft.block.AbstractBlock$AbstractBlockState.canReplace(AbstractBlock.java)
		// at net.minecraft.item.AutomaticItemPlacementContext.canPlace(AutomaticItemPlacementContext.java)
		// at net.minecraft.item.AutomaticItemPlacementContext.canReplaceExisting(AutomaticItemPlacementContext.java)
		// at net.minecraft.block.SlabBlock.canReplace(SlabBlock.java)
		@Override
		public boolean replacingClickedOnBlock() {
			return false;
		}
		
	}
	
}
