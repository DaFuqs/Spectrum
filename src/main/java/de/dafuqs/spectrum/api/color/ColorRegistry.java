package de.dafuqs.spectrum.api.color;

import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.data_loaders.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.material.*;
import net.neoforged.neoforge.event.*;

import java.util.*;

public abstract class ColorRegistry<T> {
	
	public static ColorMappingDataLoader<Item> ITEM_COLORS = new ColorMappingDataLoader<>("item", BuiltInRegistries.ITEM);
	public static ColorMappingDataLoader<Fluid> FLUID_COLORS = new ColorMappingDataLoader<>("fluid", BuiltInRegistries.FLUID);
	
	public static void registerColorRegistries(AddReloadListenerEvent event) {
		event.addListener(ITEM_COLORS);
		event.addListener(FLUID_COLORS);
	}
	
}
