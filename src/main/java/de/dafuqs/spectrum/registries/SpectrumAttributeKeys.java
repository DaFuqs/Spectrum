package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.registries.*;
import net.minecraft.tags.*;
import net.minecraft.world.entity.ai.attributes.*;

public class SpectrumAttributeKeys {
	
	public static final TagKey<Attribute> INEXORABLE_ARMOR_EFFECTIVE = TagKey.create(BuiltInRegistries.ATTRIBUTE.key(), SpectrumCommon.locate("inexorable_armor_effective"));
	public static final TagKey<Attribute> INEXORABLE_HANDHELD_EFFECTIVE = TagKey.create(BuiltInRegistries.ATTRIBUTE.key(), SpectrumCommon.locate("inexorable_handheld_effective"));

}
