package de.dafuqs.spectrum.explosion;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.explosion.modifier.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.damage.*;
import net.minecraft.particle.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;

public class SpectrumExplosionEffects {
	
	// MODIFIER TYPES
	public static final ExplosionModifierType AMALGAM_TYPE = registerModifierType("amalgam", 3); // ???
	public static final ExplosionModifierType DAMAGE_SOURCE_TYPE = registerModifierType("damage_source", 1); // changes the damage source
	public static final ExplosionModifierType COSMETIC_TYPE = registerModifierType("cosmetic", Integer.MAX_VALUE); // adds particles or other cosmetic effects
	
	
	// MODIFIERS
	// Boosters
	public static final ExplosionModifier AMALGAM = registerModifier(new AmalgamModifier(SpectrumCommon.locate("amalgam"), AMALGAM_TYPE, 0xffbf40)); // increases damage, I guess?
	
	// Damage source changers
	public static final ExplosionModifier LIGHTNING = registerModifier(new DamageChangingModifier(SpectrumCommon.locate("lightning_damage"), DAMAGE_SOURCE_TYPE, DamageSource.LIGHTNING_BOLT, SpectrumParticleTypes.WHITE_EXPLOSION, 0xaff3eb));
	public static final ExplosionModifier MAGIC = registerModifier(new DamageChangingModifier(SpectrumCommon.locate("magic_damage"), DAMAGE_SOURCE_TYPE, DamageSource.MAGIC, SpectrumParticleTypes.PURPLE_CRAFTING, 0xff59ff));
	public static final ExplosionModifier LOOTING = registerModifier(new DamageChangingModifier(SpectrumCommon.locate("looting_damage"), DAMAGE_SOURCE_TYPE, SpectrumDamageSources.MIDNIGHT_SOLUTION, ParticleTypes.ENCHANT, 0x5433a5));
	public static final ExplosionModifier PRIMORDIAL_FIRE = registerModifier(new PrimordialFireModifier(SpectrumCommon.locate("primordial_fire_damage"), DAMAGE_SOURCE_TYPE, SpectrumDamageSources.PRIMORDIAL_FIRE, SpectrumParticleTypes.PRIMORDIAL_FLAME_SMALL, 0xff2664));
	
	// Cosmetic
	public static final ExplosionModifier STARRY = registerModifier(new ParticleAddingModifier(SpectrumCommon.locate("starry"), COSMETIC_TYPE, ParticleTypes.END_ROD, 0xc3c8d4));
	
	
	private static <T extends ExplosionModifier> T registerModifier(T modifier) {
		return Registry.register(SpectrumRegistries.EXPLOSION_EFFECT_MODIFIERS, modifier.id, modifier);
	}
	
	private static ExplosionModifierType registerModifierType(String name, int maxModifiersForType) {
		Identifier id = SpectrumCommon.locate(name);
		return Registry.register(SpectrumRegistries.EXPLOSION_EFFECT_FAMILIES, id, new ExplosionModifierType(id, maxModifiersForType));
	}
	
	public static void register() {
	
	}
	
}
