package de.dafuqs.spectrum.registries;

import net.minecraft.entity.damage.*;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.*;

import static de.dafuqs.spectrum.SpectrumCommon.*;

public class SpectrumDamageTypeTags {
	
	public static final TagKey<DamageType> DROPS_LOOT_LIKE_PLAYERS = TagKey.of(RegistryKeys.DAMAGE_TYPE, locate("drops_loot_like_players"));
	public static final TagKey<DamageType> USES_SET_HEALTH = TagKey.of(RegistryKeys.DAMAGE_TYPE, locate("uses_set_health"));
	public static final TagKey<DamageType> BYPASSES_DIKE = TagKey.of(RegistryKeys.DAMAGE_TYPE, locate("bypasses_dike"));
	public static final TagKey<DamageType> IS_PRIMORDIAL_FIRE = TagKey.of(RegistryKeys.DAMAGE_TYPE, locate("is_primordial_fire"));
	
}
