package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

public class ProjectorBlockEntity extends BlockEntity {
	
	final ResourceLocation texture;
	final float heightOffset, bobMultiplier, scaling;
	
	public ProjectorBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.PROJECTOR.get(), pos, state);
		var projectorBlock = (ProjectorBlock) state.getBlock();
		heightOffset = projectorBlock.heightOffset;
		bobMultiplier = projectorBlock.bobMultiplier;
		texture = projectorBlock.texture;
		scaling = projectorBlock.scaling;
	}
}
