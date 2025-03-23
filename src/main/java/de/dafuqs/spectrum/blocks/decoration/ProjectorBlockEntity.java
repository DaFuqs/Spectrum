package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

public class ProjectorBlockEntity extends BlockEntity {

	final Identifier texture;
	final float heightOffset, bobMultiplier, scaling;

	public ProjectorBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.PROJECTOR, pos, state);
		var projectorBlock = (ProjectorBlock) state.getBlock();
		heightOffset = projectorBlock.heightOffset;
		bobMultiplier = projectorBlock.bobMultiplier;
		texture = projectorBlock.texture;
		scaling = projectorBlock.scaling;
	}
}
