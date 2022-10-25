package de.dafuqs.spectrum.blocks.rock_candy;

import de.dafuqs.spectrum.enums.BuiltinGemstoneColor;
import de.dafuqs.spectrum.enums.GemstoneColor;
import de.dafuqs.spectrum.items.conditional.GemstonePowderItem;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;
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
		
		public static RockCandyVariant fromGemstonePowder(Item item) {
			if(item instanceof GemstonePowderItem gemstoneColorItem) {
				GemstoneColor gemstoneColor = gemstoneColorItem.getGemstoneColor();
				if (BuiltinGemstoneColor.CYAN.equals(gemstoneColor)) {
					return RockCandyVariant.TOPAZ;
				} else if (BuiltinGemstoneColor.MAGENTA.equals(gemstoneColor)) {
					return RockCandyVariant.AMETHYST;
				} else if (BuiltinGemstoneColor.YELLOW.equals(gemstoneColor)) {
					return RockCandyVariant.CITRINE;
				} else if (BuiltinGemstoneColor.BLACK.equals(gemstoneColor)) {
					return RockCandyVariant.ONYX;
				} else if (BuiltinGemstoneColor.WHITE.equals(gemstoneColor)) {
					return RockCandyVariant.MOONSTONE;
				}
			}
			return RockCandyVariant.NONE;
		}
		
		@Override
		public String asString() {
			return this.toString().toLowerCase(Locale.ROOT);
		}
		
		public DyeColor getDyeColor() {
			switch (this) {
				case TOPAZ -> { return DyeColor.CYAN; }
				case AMETHYST -> { return DyeColor.MAGENTA; }
				case CITRINE -> { return DyeColor.YELLOW; }
				case ONYX -> { return DyeColor.BLACK; }
				case MOONSTONE -> { return DyeColor.WHITE; }
				default -> { return DyeColor.LIGHT_GRAY; }
			}
		}
	}
	
	RockCandyVariant getVariant();
	
}
