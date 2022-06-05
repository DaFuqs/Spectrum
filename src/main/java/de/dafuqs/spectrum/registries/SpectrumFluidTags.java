package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumFluidTags {
	
	public static TagKey<Fluid> SWIMMABLE_FLUID;
	public static TagKey<Fluid> MIDNIGHT_SOLUTION_CONVERTED;
	
	public static TagKey<Fluid> LIQUID_CRYSTAL;
	public static TagKey<Fluid> MUD;
	public static TagKey<Fluid> MIDNIGHT_SOLUTION;
	
	private static TagKey<Fluid> register(String id) {
		return TagKey.of(Registry.FLUID_KEY, new Identifier(SpectrumCommon.MOD_ID + ":" + id));
	}
	
	public static void register() {
		SWIMMABLE_FLUID = register("swimmable_fluid");
		MIDNIGHT_SOLUTION_CONVERTED = register("midnight_solution_converted");
		
		LIQUID_CRYSTAL = register("liquid_crystal");
		MUD = register("mud");
		MIDNIGHT_SOLUTION = register("midnight_solution");
	}
}
