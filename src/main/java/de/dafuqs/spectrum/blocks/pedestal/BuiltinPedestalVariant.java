package de.dafuqs.spectrum.blocks.pedestal;

import de.dafuqs.spectrum.enums.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;

public enum BuiltinPedestalVariant implements PedestalVariant {
	BASIC_TOPAZ(PedestalRecipeTier.BASIC),
	BASIC_AMETHYST(PedestalRecipeTier.BASIC),
	BASIC_CITRINE(PedestalRecipeTier.BASIC),
	CMY(PedestalRecipeTier.SIMPLE),
	ONYX(PedestalRecipeTier.ADVANCED),
	MOONSTONE(PedestalRecipeTier.COMPLEX);
	
	final PedestalRecipeTier tier;
	
	BuiltinPedestalVariant(PedestalRecipeTier tier) {
		this.tier = tier;
	}
	
	@Override
	public PedestalRecipeTier getRecipeTier() {
		return this.tier;
	}
	
	@Override
	public Block getPedestalBlock() {
		switch (this) {
			case BASIC_TOPAZ -> {
				return SpectrumBlocks.PEDESTAL_BASIC_TOPAZ;
			}
			case BASIC_AMETHYST -> {
				return SpectrumBlocks.PEDESTAL_BASIC_AMETHYST;
			}
			case BASIC_CITRINE -> {
				return SpectrumBlocks.PEDESTAL_BASIC_CITRINE;
			}
			case CMY -> {
				return SpectrumBlocks.PEDESTAL_ALL_BASIC;
			}
			case ONYX -> {
				return SpectrumBlocks.PEDESTAL_ONYX;
			}
			default -> {
				return SpectrumBlocks.PEDESTAL_MOONSTONE;
			}
		}
	}
	
}