package de.dafuqs.spectrum.blocks.flammable;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.*;

public class FlammableRotatedPillarBlock extends RotatedPillarBlock {
	
	public FlammableRotatedPillarBlock(Properties settings) {
		super(settings.ignitedByLava());
	}
	
	@Override
	public MapCodec<? extends FlammableRotatedPillarBlock> codec() {
		return null;
	}
	
	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return 5;
	}
	
	@Override
	public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return 5;
	}
	
}
