package de.dafuqs.spectrum.registries.color;

import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.Optional;

public abstract class ColorRegistry<T> {
	
	public static ItemColorRegistry ITEM_COLORS;
	public static FluidColorRegistry FLUID_COLORS;
	
	public static void registerColorRegistries() {
		ITEM_COLORS = new ItemColorRegistry();
		FLUID_COLORS = new FluidColorRegistry();
	}
	
	public abstract void registerColorMapping(Identifier identifier, DyeColor dyeColor);
	
	public abstract void registerColorMapping(T object, DyeColor dyeColor);
	
	public abstract Optional<DyeColor> getMapping(T element);
	
	
	
}
