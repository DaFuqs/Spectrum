package de.dafuqs.spectrum.blocks.ink.gen;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;

public class VantaclastBlockEntity extends InkGeneratorBlockEntity {
	
	public VantaclastBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.VANTACLAST.get(), blockPos, blockState, 2);
	}
	
	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.spectrum.vantaclast");
	}
	
	@Override
	protected boolean tickLogic(Level level) {
		return false;
	}
	
}
