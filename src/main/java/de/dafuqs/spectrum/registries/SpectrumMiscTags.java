package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;

public class SpectrumMiscTags {

    public static final TagKey<EntityAttribute> INEXORABLE_ARMOR_EFFECTIVE = TagKey.of(Registries.ATTRIBUTE.getKey(), SpectrumCommon.locate("inexorable_armor_effective"));
    public static final TagKey<EntityAttribute> INEXORABLE_HANDHELD_EFFECTIVE = TagKey.of(Registries.ATTRIBUTE.getKey(), SpectrumCommon.locate("inexorable_handheld_effective"));
}
