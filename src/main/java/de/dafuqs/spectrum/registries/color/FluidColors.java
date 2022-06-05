package de.dafuqs.spectrum.registries.color;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Optional;

public class FluidColors extends ColorRegistry<Fluid> {
	
	private static final HashMap<Fluid, DyeColor> COLORS = new HashMap<>() {{
		put(Fluids.WATER, DyeColor.BLUE);
		put(Fluids.LAVA, DyeColor.ORANGE);
	}};
	
	@Override
	public void registerColorMapping(Identifier identifier, DyeColor dyeColor) {
		Fluid fluid = Registry.FLUID.get(identifier);
		if (fluid != Fluids.EMPTY) {
			COLORS.put(fluid, dyeColor);
		}
	}
	
	@Override
	public void registerColorMapping(Fluid fluid, DyeColor dyeColor) {
		COLORS.put(fluid, dyeColor);
	}
	
	@Override
	public Optional<DyeColor> getMapping(Fluid fluid) {
		if (COLORS.containsKey(fluid)) {
			return Optional.of(COLORS.get(fluid));
		} else {
			return Optional.empty();
		}
	}
	
}
