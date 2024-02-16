package de.dafuqs.spectrum.api.energy.color;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.registry.tag.*;

public class InkColorTags {
	
	public static final TagKey<InkColor> ELEMENTAL_COLORS = getReference("elementals");
	public static final TagKey<InkColor> COMPOUND_COLORS = getReference("compounds");
	
	private static TagKey<InkColor> getReference(String name) {
		return TagKey.of(SpectrumRegistries.INK_COLORS_KEY, SpectrumCommon.locate(name));
	}
	
}
