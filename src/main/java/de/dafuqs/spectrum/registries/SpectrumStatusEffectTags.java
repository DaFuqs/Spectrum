package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.registries.*;
import net.minecraft.tags.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;

public class SpectrumStatusEffectTags {
	
	public static TagKey<MobEffect> BYPASSES_WHISPY_CIRCLET = of("bypasses_whispy_circlet");
	public static TagKey<MobEffect> BYPASSES_NECTAR_GLOVES = of("bypasses_nectar_gloves");
	public static TagKey<MobEffect> BYPASSES_IMMUNITY = of("bypasses_immunity");
	public static TagKey<MobEffect> CANNOT_BE_INCURABLE = of("cannot_be_incurable");
	public static TagKey<MobEffect> NO_DURATION_EXTENSION = of("no_duration_extension");
	public static TagKey<MobEffect> SOPORIFIC = of("soporific");
	public static TagKey<MobEffect> NIGHT_ALCHEMY = of("night_alchemy");
	public static TagKey<MobEffect> STACKING = of("stacking");
	
	private static TagKey<MobEffect> of(String id) {
		return TagKey.create(Registries.MOB_EFFECT, SpectrumCommon.locate(id));
	}
	
	public static boolean hasEffectWithTag(LivingEntity livingEntity, TagKey<MobEffect> tag) {
		for (var statusEffect : livingEntity.getActiveEffectsMap().keySet()) {
			if (statusEffect.is(tag)) {
				return true;
			}
		}
		return false;
	}
	
}
