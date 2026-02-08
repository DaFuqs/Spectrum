package de.dafuqs.spectrum.blocks.flammable;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.*;

public class FlammableRotatedPillarBlock extends RotatedPillarBlock {
	
	public FlammableRotatedPillarBlock(Properties settings) {
		super(settings.ignitedByLava());
	}
	
	@Override
	public MapCodec<? extends FlammableRotatedPillarBlock> codec() {
		return null;
	}
	
	@Override
	public int getFireSpreadSpeed(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Direction direction) {
		return 5;
	}
	
	@Override
	public int getFlammability(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Direction direction) {
		return 5;
	}
	
}
