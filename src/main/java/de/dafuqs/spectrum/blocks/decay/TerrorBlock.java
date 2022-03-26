package de.dafuqs.spectrum.blocks.decay;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;

public class TerrorBlock extends RuinBlock {
	
	// A special version of ruin that spreads indefinitely, even through air.
	// There are no brakes on the Terror train
	public TerrorBlock(Settings settings, TagKey<Block> whiteListBlockTag, TagKey<Block> blackListBlockTag, int tier, float damageOnTouching) {
		super(settings, whiteListBlockTag, blackListBlockTag, tier, damageOnTouching);
	}
	
	@Override
	protected float getSpreadChance() {
		return SpectrumCommon.CONFIG.TerrorDecayTickRate;
	}
	
	
}
