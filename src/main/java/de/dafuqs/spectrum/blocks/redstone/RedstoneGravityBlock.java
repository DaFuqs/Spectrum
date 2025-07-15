package de.dafuqs.spectrum.blocks.redstone;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import org.jetbrains.annotations.*;

public class RedstoneGravityBlock extends FallingBlock {
	
	public static final MapCodec<RedstoneGravityBlock> CODEC = simpleCodec(RedstoneGravityBlock::new);
	
	public static final BooleanProperty UNSTABLE = BooleanProperty.create("unstable");
	
	public RedstoneGravityBlock(Properties settings) {
		super(settings);
		registerDefaultState(getStateDefinition().any().setValue(UNSTABLE, false));
	}
	
	@Override
	public MapCodec<? extends RedstoneGravityBlock> codec() {
		return CODEC;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(UNSTABLE);
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState state = super.getStateForPlacement(ctx);
		if (ctx.getLevel().getBestNeighborSignal(ctx.getClickedPos()) > 0 && isFree(ctx.getLevel().getBlockState(ctx.getClickedPos().below()))) {
			state.setValue(UNSTABLE, true);
		} else {
			state.setValue(UNSTABLE, false);
		}
		return state;
	}
	
	/**
	 * Only trigger fall if redstone applied or unstable
	 * if redstone: set neighboring block to unstable
	 */
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (state.getValue(UNSTABLE)) {
			propagate(world, pos);
			world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(UNSTABLE, false));
			super.tick(state, world, pos, random); // fall, if not supported
		} else if (world.getBestNeighborSignal(pos) > 0) {
			world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(UNSTABLE, true));
			propagate(world, pos);
		}
	}
	
	/**
	 * Set all RedstoneGravityBlocks next to it to unstable
	 */
	protected void propagate(ServerLevel world, BlockPos pos) {
		for (Direction dir : Direction.values()) {
			BlockPos offsetPos = pos.relative(dir);
			BlockState offsetBlockState = world.getBlockState(offsetPos);
			if (offsetBlockState.is(this) && !offsetBlockState.getValue(UNSTABLE) && isFree(world.getBlockState(offsetPos.below()))) {
				world.setBlockAndUpdate(offsetPos, world.getBlockState(offsetPos).setValue(UNSTABLE, true));
				world.scheduleTick(pos, this, this.getDelayAfterPlace());
			}
		}
	}
	
}
