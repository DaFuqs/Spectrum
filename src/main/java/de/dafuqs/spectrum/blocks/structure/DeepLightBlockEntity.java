package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.util.math.*;

public class DeepLightBlockEntity extends BlockEntity {

	public DeepLightBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.DEEP_LIGHT, pos, state);
	}
}
