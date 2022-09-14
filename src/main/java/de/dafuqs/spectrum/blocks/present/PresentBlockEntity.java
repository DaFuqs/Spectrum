package de.dafuqs.spectrum.blocks.present;

import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class PresentBlockEntity extends BlockEntity {
	
	public PresentBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.PRESENT, pos, state);
	}
	
}
