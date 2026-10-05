package de.dafuqs.spectrum.blocks.pedestal;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;

public enum BuiltinGemstoneColor implements GemstoneColor, StringRepresentable {
	CYAN("cyan", InkColors.CYAN_COLOR),
	MAGENTA("magenta", InkColors.MAGENTA_COLOR),
	YELLOW("yellow", InkColors.YELLOW_COLOR),
	BLACK("black", InkColors.BLACK_COLOR),
	WHITE("white", InkColors.WHITE_COLOR);
	
	private final int color;
	
	BuiltinGemstoneColor(String name, int color) {
		Registry.register(SpectrumRegistries.GEMSTONE_COLOR, SpectrumCommon.locate(name), this);
		this.color = color;
	}
	
	@Override
	public int getColor() {
		return this.color;
	}
	
	@Override
	public Item getGemstonePowderItem() {
		switch (this) {
			case CYAN -> {
				return SpectrumItems.TOPAZ_POWDER.get();
			}
			case MAGENTA -> {
				return SpectrumItems.AMETHYST_POWDER.get();
			}
			case YELLOW -> {
				return SpectrumItems.CITRINE_POWDER.get();
			}
			case BLACK -> {
				return SpectrumItems.ONYX_POWDER.get();
			}
			case WHITE -> {
				return SpectrumItems.MOONSTONE_POWDER.get();
			}
			default -> throw new RuntimeException("Tried getting powder item for a color which does not have one");
		}
	}
	
	@Override
	public String getSerializedName() {
		return name();
	}
}