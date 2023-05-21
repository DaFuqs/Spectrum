package de.dafuqs.spectrum.blocks.melon;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;

public class GlisteringMelonBlock extends MelonBlock {
	
	public GlisteringMelonBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public StemBlock getStem() {
		return (StemBlock) SpectrumBlocks.GLISTERING_MELON_STEM;
	}
	
	@Override
	public AttachedStemBlock getAttachedStem() {
		return (AttachedStemBlock) SpectrumBlocks.ATTACHED_GLISTERING_MELON_STEM;
	}
	
}
