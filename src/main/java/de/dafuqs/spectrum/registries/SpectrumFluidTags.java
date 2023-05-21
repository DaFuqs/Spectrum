package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.fluid.*;
import net.minecraft.tag.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;

public class SpectrumFluidTags {
	
	public static final TagKey<Fluid> SWIMMABLE_FLUID = register("swimmable_fluid");
	public static final TagKey<Fluid> MIDNIGHT_SOLUTION_CONVERTED = register("midnight_solution_converted");
	public static final TagKey<Fluid> ACTIVATES_WEEPING_CIRCLET = register("activates_weeping_circlet");
	
	public static final TagKey<Fluid> LAGOON_ROD_FISHABLE_IN = register("lagoon_rod_fishable_in");
	public static final TagKey<Fluid> MOLTEN_ROD_FISHABLE_IN = register("molten_rod_fishable_in");
	public static final TagKey<Fluid> BEDROCK_ROD_FISHABLE_IN = register("bedrock_rod_fishable_in");
	
	public static final TagKey<Fluid> LIQUID_CRYSTAL = register("liquid_crystal");
	public static final TagKey<Fluid> MUD = register("mud");
	public static final TagKey<Fluid> MIDNIGHT_SOLUTION = register("midnight_solution");
	public static final TagKey<Fluid> DRAGONROT = register("dragonrot");
	
	private static TagKey<Fluid> register(String id) {
		return TagKey.of(Registry.FLUID_KEY, new Identifier(SpectrumCommon.MOD_ID + ":" + id));
	}
	
}
