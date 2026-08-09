package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.registries.*;
import net.minecraft.tags.*;
import net.minecraft.world.level.material.*;

public class SpectrumFluidTags {
	
	public static final TagKey<Fluid> MIDNIGHT_SOLUTION_CONVERTED = of("midnight_solution_converted");
	
	public static final TagKey<Fluid> LAGOON_ROD_FISHABLE_IN = of("lagoon_rod_fishable_in");
	public static final TagKey<Fluid> MOLTEN_ROD_FISHABLE_IN = of("molten_rod_fishable_in");
	public static final TagKey<Fluid> BEDROCK_ROD_FISHABLE_IN = of("bedrock_rod_fishable_in");
	
	public static final TagKey<Fluid> LIQUID_CRYSTAL = of("liquid_crystal");
	public static final TagKey<Fluid> MIDNIGHT_SOLUTION = of("midnight_solution");
	public static final TagKey<Fluid> DRAGONROT = of("dragonrot");
	
	private static TagKey<Fluid> of(String id) {
		return TagKey.create(BuiltInRegistries.FLUID.key(), SpectrumCommon.locate(id));
	}
	
}
