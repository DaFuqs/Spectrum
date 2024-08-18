package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.registry.*;

public class SpectrumEntityAttributes {
	
	/**
	 * How resistant the entity is to sleep effects. The sleep effects use this value as a multiplier
	 * <1 means it is more vulnerable than the default, getting stronger effects
	 * >1 means it is more resistant
	 */
	public static final EntityAttribute INDUCED_SLEEP_RESISTANCE = register("induced_sleep_resistance", new ClampedEntityAttribute("attribute.name.spectrum.induced_sleep_resistance", 1.0, 0.0, 1024.0));
	
	
	private static EntityAttribute register(String name, EntityAttribute attribute) {
		return Registry.register(Registries.ATTRIBUTE, SpectrumCommon.locate(name), attribute);
	}
	
	public static void register() {
	
	}
	
}
