package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

public class DeepLightBlockEntity extends BlockEntity {

	public DeepLightBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.DEEP_LIGHT, pos, state);
	}
}
