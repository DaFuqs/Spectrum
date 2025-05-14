package de.dafuqs.spectrum.blocks;

import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

public class PureRedstoneBlock extends PoweredBlock {
	
	public PureRedstoneBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public int getDirectSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		return 15;
	}
	
	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onPlace(state, world, pos, oldState, notify);
		
		for (Direction direction : Direction.values()) {
			world.updateNeighborsAt(pos.relative(direction), this);
		}
	}
	
	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		super.onRemove(state, world, pos, newState, moved);
		
		for (Direction direction : Direction.values()) {
			world.updateNeighborsAt(pos.relative(direction), this);
		}
	}
	
}
