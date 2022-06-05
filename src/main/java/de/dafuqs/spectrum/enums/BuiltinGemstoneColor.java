package de.dafuqs.spectrum.enums;

import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;

public enum BuiltinGemstoneColor implements GemstoneColor {
	CYAN(DyeColor.CYAN),
	MAGENTA(DyeColor.MAGENTA),
	YELLOW(DyeColor.YELLOW),
	BLACK(DyeColor.BLACK),
	WHITE(DyeColor.WHITE);
	
	final DyeColor dyeColor;
	
	BuiltinGemstoneColor(DyeColor dyeColor) {
		this.dyeColor = dyeColor;
	}
	
	@Override
	public DyeColor getDyeColor() {
		return this.dyeColor;
	}
	
	public Item getGemstonePowderItem() {
		switch (this) {
			case CYAN -> {
				return SpectrumItems.TOPAZ_POWDER;
			}
			case MAGENTA -> {
				return SpectrumItems.AMETHYST_POWDER;
			}
			case YELLOW -> {
				return SpectrumItems.CITRINE_POWDER;
			}
			case BLACK -> {
				return SpectrumItems.ONYX_POWDER;
			}
			default -> {
				return SpectrumItems.MOONSTONE_POWDER;
			}
		}
	}
	
}