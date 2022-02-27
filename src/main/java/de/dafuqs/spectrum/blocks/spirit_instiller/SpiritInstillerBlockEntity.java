package de.dafuqs.spectrum.blocks.spirit_instiller;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class SpiritInstillerBlockEntity extends BlockEntity {
	
	public SpiritInstillerBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.SPIRIT_INSTILLER, pos, state);
	}
	
}
