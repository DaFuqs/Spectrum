package de.dafuqs.spectrum.blocks.deeper_down.flora;

import de.dafuqs.spectrum.blocks.SpreadableFloraBlock;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;

public class AshFloraBlock extends SpreadableFloraBlock {
	
	public AshFloraBlock(Settings settings) {
		super(7, settings);
	}
	
	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		return floor.isOf(SpectrumBlocks.ASH) || floor.isOf(SpectrumBlocks.ASHEN_BLACKSLAG) || super.canPlantOnTop(floor, world, pos);
	}
}
