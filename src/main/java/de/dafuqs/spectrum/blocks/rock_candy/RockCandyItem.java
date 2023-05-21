package de.dafuqs.spectrum.blocks.rock_candy;

import net.minecraft.item.*;

public class RockCandyItem extends Item implements RockCandy {
	
	protected final RockCandyVariant rockCandyVariant;
	
	public RockCandyItem(Settings settings, RockCandyVariant rockCandyVariant) {
		super(settings);
		this.rockCandyVariant = rockCandyVariant;
	}
	
	@Override
	public RockCandyVariant getVariant() {
		return rockCandyVariant;
	}
	
}
