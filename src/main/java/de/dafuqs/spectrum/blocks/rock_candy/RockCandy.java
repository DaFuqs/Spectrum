package de.dafuqs.spectrum.blocks.rock_candy;

import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DyeColor;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public interface RockCandy {
	
	enum RockCandyVariant implements StringIdentifiable {
		SUGAR,
		AMETHYST,
		CITRINE,
		TOPAZ,
		ONYX,
		MOONSTONE;
		
		public static @Nullable RockCandyVariant getFor(ItemStack itemStack) {
			Item item = itemStack.getItem();
			if (item == Items.SUGAR) {
				return RockCandyVariant.SUGAR;
			} else if (item == SpectrumItems.TOPAZ_POWDER) {
				return RockCandyVariant.TOPAZ;
			} else if (item == SpectrumItems.AMETHYST_POWDER) {
				return RockCandyVariant.AMETHYST;
			} else if (item == SpectrumItems.CITRINE_POWDER) {
				return RockCandyVariant.CITRINE;
			} else if (item == SpectrumItems.ONYX_POWDER) {
				return RockCandyVariant.ONYX;
			} else if (item == SpectrumItems.MOONSTONE_POWDER) {
				return RockCandyVariant.MOONSTONE;
			}
			return null;
		}
		
		@Override
		public String asString() {
			return this.toString().toLowerCase(Locale.ROOT);
		}
		
		public DyeColor getDyeColor() {
			switch (this) {
				case TOPAZ -> {
					return DyeColor.CYAN;
				}
				case AMETHYST -> {
					return DyeColor.MAGENTA;
				}
				case CITRINE -> {
					return DyeColor.YELLOW;
				}
				case ONYX -> {
					return DyeColor.BLACK;
				}
				case MOONSTONE -> {
					return DyeColor.WHITE;
				}
				default -> {
					return DyeColor.LIGHT_GRAY;
				}
			}
		}
	}
	
	RockCandyVariant getVariant();
	
}
