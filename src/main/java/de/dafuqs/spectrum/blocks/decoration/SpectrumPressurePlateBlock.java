package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;

// TODO - Remove?
public class SpectrumPressurePlateBlock extends PressurePlateBlock {
	
	public SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule type, Settings settings) {
		super(type, settings, SpectrumBlockSetTypes.COLORED_WOOD);
	}
	
}
