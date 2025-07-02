package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.ai.attributes.*;

public class SpectrumEntityAttributes {
	
	public static final ResourceLocation CRIT_MODIFIER_ID = SpectrumCommon.locate("crit_modifier");
	public static final ResourceLocation REACH_MODIFIER_ID = SpectrumCommon.locate("reach_modifier");
	
	/**
	 * How vulnerable the entity is to sleep effects. The sleep effects use this value as a multiplier
	 * <1 means it is more resistant than the default, getting weaker effects
	 * >1 means it is more vulnerable
	 */
	public static final Holder<Attribute> MENTAL_PRESENCE = register("mental_presence", new RangedAttribute("attribute.name.spectrum.mental_presence", 1.0, 0, 1024));
	
	
	private static Holder<Attribute> register(String name, Attribute attribute) {
		return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, SpectrumCommon.locate(name), attribute);
	}
	
	public static void register() {
	
	}
	
}
