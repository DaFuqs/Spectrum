package de.dafuqs.spectrum.blocks.flammable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import org.jetbrains.annotations.NotNull;

public class FlammableFenceGateBlock extends FenceGateBlock {
	
	public FlammableFenceGateBlock(WoodType woodType, Properties properties) {
		super(woodType, properties.ignitedByLava());
	}
	
	@Override
	public int getFireSpreadSpeed(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Direction direction) {
		return 5;
	}
	
	@Override
	public int getFlammability(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Direction direction) {
		return 20;
	}
	
}
