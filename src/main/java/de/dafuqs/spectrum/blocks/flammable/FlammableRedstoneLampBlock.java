package de.dafuqs.spectrum.blocks.flammable;

import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

public class FlammableRedstoneLampBlock extends RedstoneLampBlock {
	
	public FlammableRedstoneLampBlock(Properties properties) {
		super(properties);
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
