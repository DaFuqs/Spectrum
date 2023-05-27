package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.registries.SpectrumBlockSetTypes;
import net.minecraft.block.PressurePlateBlock;

// TODO - Remove?
public class SpectrumPressurePlateBlock extends PressurePlateBlock {
	
	public SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule type, Settings settings) {
		super(type, settings, SpectrumBlockSetTypes.COLORED_WOOD);
	}
	
}
