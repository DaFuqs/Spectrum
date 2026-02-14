package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

public class SpectrumEntityAttributes {
	
	private static final DeferredRegister<Attribute> REGISTRAR = DeferredRegister.create(Registries.ATTRIBUTE, SpectrumCommon.MOD_ID);
	
	// TODO: this is entirely the wrong location for this
	public static final ResourceLocation CRIT_MODIFIER_ID = SpectrumCommon.locate("crit_modifier");
	public static final ResourceLocation REACH_MODIFIER_ID = SpectrumCommon.locate("reach_modifier");
	
	/**
	 * How vulnerable the entity is to sleep effects. The sleep effects use this value as a multiplier
	 * <1 means it is more resistant than the default, getting weaker effects
	 * >1 means it is more vulnerable
	 */
	// TODO: remove? Both hardly used and hard to understand
	public static final Holder<Attribute> MENTAL_PRESENCE = REGISTRAR.register("mental_presence", () -> new RangedAttribute("attribute.name.spectrum.mental_presence", 1.0, 0, 1024));
	
	public static void register(IEventBus modBus) {
		REGISTRAR.register(modBus);
	}
	
}
