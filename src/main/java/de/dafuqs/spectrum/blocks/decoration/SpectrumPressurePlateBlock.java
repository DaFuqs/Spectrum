package de.dafuqs.spectrum.blocks.decoration;

import net.minecraft.block.BlockSetType;
import net.minecraft.block.PressurePlateBlock;

public class SpectrumPressurePlateBlock extends PressurePlateBlock {
	
	public SpectrumPressurePlateBlock(PressurePlateBlock.ActivationRule type, Settings settings) {
		// TODO - BlockSetType/WoodType
		super(type, settings, BlockSetType.SPRUCE);
	}
	
}
