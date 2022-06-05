package de.dafuqs.spectrum.blocks.melon;

import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.MelonBlock;
import net.minecraft.block.StemBlock;

public class GlisteringMelonBlock extends MelonBlock {
	
	public GlisteringMelonBlock(Settings settings) {
		super(settings);
	}
	
	public StemBlock getStem() {
		return (StemBlock) SpectrumBlocks.GLISTERING_MELON_STEM;
	}
	
	public AttachedStemBlock getAttachedStem() {
		return (AttachedStemBlock) SpectrumBlocks.ATTACHED_GLISTERING_MELON_STEM;
	}
	
}
