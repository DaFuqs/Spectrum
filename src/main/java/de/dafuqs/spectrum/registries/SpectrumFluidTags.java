package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.fluid.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.*;
import net.minecraft.registry.*;

public class SpectrumFluidTags {

	public static TagKey<Fluid> SWIMMABLE_FLUID = register("swimmable_fluid");
	public static TagKey<Fluid> MIDNIGHT_SOLUTION_CONVERTED = register("midnight_solution_converted");
	public static TagKey<Fluid> ACTIVATES_WEEPING_CIRCLET = register("activates_weeping_circlet");

	public static TagKey<Fluid> LAGOON_ROD_FISHABLE_IN = register("lagoon_rod_fishable_in");
	public static TagKey<Fluid> MOLTEN_ROD_FISHABLE_IN = register("molten_rod_fishable_in");
	public static TagKey<Fluid> BEDROCK_ROD_FISHABLE_IN = register("bedrock_rod_fishable_in");

	public static TagKey<Fluid> LIQUID_CRYSTAL = register("liquid_crystal");
	public static TagKey<Fluid> MUD = register("mud");
	public static TagKey<Fluid> MIDNIGHT_SOLUTION = register("midnight_solution");
	public static TagKey<Fluid> DRAGONROT = register("dragonrot");

	private static TagKey<Fluid> register(String id) {
		return TagKey.of(Registries.FLUID.getKey(), new Identifier(SpectrumCommon.MOD_ID + ":" + id));
	}

}
