package de.dafuqs.spectrum.blocks.enchanter;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class EnchanterBlockEntity extends BlockEntity {
	
	public EnchanterBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.ENCHANTER, pos, state);
	}
	
}
