package de.dafuqs.spectrum.blocks.rock_candy;

import net.minecraft.util.StringIdentifiable;

import java.util.Locale;

public interface RockCandy {
	
	enum RockCandyVariant implements StringIdentifiable {
		NONE,
		AMETHYST,
		CITRINE,
		TOPAZ,
		ONYX,
		MOONSTONE;
		
		@Override
		public String asString() {
			return this.toString().toLowerCase(Locale.ROOT);
		}
	}
	
	RockCandyVariant getVariant();
	
}
