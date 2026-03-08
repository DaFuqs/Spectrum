package de.dafuqs.spectrum.blocks.redstone;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import org.jetbrains.annotations.*;

public class RedstoneSandBlock extends FallingBlock {
	
	public static final MapCodec<RedstoneSandBlock> CODEC = simpleCodec(RedstoneSandBlock::new);
	public static final IntegerProperty POWER = BlockStateProperties.POWER;
	
	public RedstoneSandBlock(Properties settings) {
		super(settings);
		registerDefaultState(getStateDefinition().any().setValue(POWER, 0));
	}

	@Override
	public MapCodec<? extends RedstoneSandBlock> codec() {
		return CODEC;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(POWER);
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState state = super.getStateForPlacement(ctx);
		
		int bestNeighborSignal = ctx.getLevel().getBestNeighborSignal(ctx.getClickedPos());
		int powerShould = Math.max(0, bestNeighborSignal - 1);
		state.setValue(POWER, powerShould);
		
		return state;
	}
	
	@Override
	protected boolean isSignalSource(BlockState state) {
		return true;
	}
	
	@Override
	protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return state.getValue(POWER);
	}
	
	/**
	 * Only trigger fall if redstone applied or unstable
	 * if redstone: set neighboring block to unstable
	 */
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		int bestNeighborSignal = world.getBestNeighborSignal(pos);
		int power = state.getValue(POWER);
		int powerShould = Math.max(0, bestNeighborSignal - 1);
		if (power != powerShould) {
			state = state.setValue(POWER, powerShould);
			world.setBlockAndUpdate(pos, state);
		}
		if (powerShould > 0) {
			// fall, if not supported
			super.tick(state, world, pos, random);
		}
	}
	
}
