package de.dafuqs.spectrum.blocks.redstone;

import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import org.jetbrains.annotations.*;

public class RedstoneGravityBlock extends FallingBlock {
	
	public static final BooleanProperty UNSTABLE = BooleanProperty.of("unstable");
	
	public RedstoneGravityBlock(Settings settings) {
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(UNSTABLE, false));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(UNSTABLE);
	}
	
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState state = super.getPlacementState(ctx);
		if (ctx.getWorld().getReceivedRedstonePower(ctx.getBlockPos()) > 0 && canFallThrough(ctx.getWorld().getBlockState(ctx.getBlockPos().down()))) {
			state.with(UNSTABLE, true);
		} else {
			state.with(UNSTABLE, false);
		}
		return state;
	}
	
	/**
	 * Only trigger fall if redstone applied or unstable
	 * if redstone: set neighboring block to unstable
	 */
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (state.get(UNSTABLE)) {
			propagate(world, pos);
			world.setBlockState(pos, world.getBlockState(pos).with(UNSTABLE, false));
			super.scheduledTick(state, world, pos, random); // fall, if not supported
		} else if (world.getReceivedRedstonePower(pos) > 0) {
			world.setBlockState(pos, world.getBlockState(pos).with(UNSTABLE, true));
			propagate(world, pos);
		}
	}
	
	/**
	 * Set all RedstoneGravityBlocks next to it to unstable
	 */
	protected void propagate(ServerWorld world, BlockPos pos) {
		for (Direction dir : Direction.values()) {
			BlockPos offsetPos = pos.offset(dir);
			BlockState offsetBlockState = world.getBlockState(offsetPos);
			if (offsetBlockState.isOf(this) && !offsetBlockState.get(UNSTABLE) && canFallThrough(world.getBlockState(offsetPos.down()))) {
				world.setBlockState(offsetPos, world.getBlockState(offsetPos).with(UNSTABLE, true));
				world.createAndScheduleBlockTick(pos, this, this.getFallDelay());
			}
		}
	}
	
}
