package de.dafuqs.spectrum.blocks.decoration;

import net.minecraft.block.*;
import net.minecraft.util.*;

public class SpectrumBedBlock extends BedBlock {

	public SpectrumBedBlock(DyeColor color, Settings settings) {
		super(color, settings);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

}
