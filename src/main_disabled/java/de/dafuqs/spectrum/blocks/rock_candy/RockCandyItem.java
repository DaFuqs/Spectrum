package de.dafuqs.spectrum.blocks.rock_candy;

import net.minecraft.world.item.*;

public class RockCandyItem extends Item implements RockCandy {
	
	protected final RockCandyVariant variant;
	
	public RockCandyItem(Item.Properties properties, RockCandyVariant variant) {
		super(properties);
		this.variant = variant;
	}
	
	@Override
	public RockCandyVariant getVariant() {
		return variant;
	}
	
}