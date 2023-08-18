package de.dafuqs.spectrum.explosion;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.explosion.modifier.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.damage.*;
import net.minecraft.particle.*;
import net.minecraft.util.registry.*;

public class SpectrumExplosionEffects {
	
	// MODIFIER TYPES
	public static final ExplosionModifierType EFFECTS_TYPE = registerModifierType("effects", new ExplosionModifierType(3)); // all sorts of stuff, like damage improvements
	public static final ExplosionModifierType DAMAGE_SOURCE_TYPE = registerModifierType("damage_source", new ExplosionModifierType(1)); // changes the damage source
	public static final ExplosionModifierType FIRE_TYPE = registerModifierType("fire", new ExplosionModifierType(1)); // adds particles or other cosmetic effects
	public static final ExplosionModifierType COSMETIC_TYPE = registerModifierType("cosmetic", new ExplosionModifierType(Integer.MAX_VALUE)); // adds particles or other cosmetic effects
	
	
	// MODIFIERS
	// Boosters
	public static final ExplosionModifier EXPLOSION_BOOST = registerModifier("explosion_boost", new ExplosionBoostModifier(EFFECTS_TYPE, 0xffbf40));
	
	// Fire type changers
	public static final ExplosionModifier FIRE = registerModifier("fire", new FireModifier(FIRE_TYPE, DamageSource.IN_FIRE, ParticleTypes.FLAME, 0xaff3eb));
	public static final ExplosionModifier SOUL_FIRE = registerModifier("soul_fire", new SoulFireModifier(FIRE_TYPE, DamageSource.IN_FIRE, ParticleTypes.SOUL_FIRE_FLAME, 0xaff3eb));
	public static final ExplosionModifier PRIMORDIAL_FIRE = registerModifier("primordial_fire", new PrimordialFireModifier(DAMAGE_SOURCE_TYPE, SpectrumDamageSources.PRIMORDIAL_FIRE, SpectrumParticleTypes.PRIMORDIAL_FLAME_SMALL, 0xff2664));
	
	// Damage source changers
	public static final ExplosionModifier LIGHTNING = registerModifier("lightning_damage", new DamageChangingModifier(DAMAGE_SOURCE_TYPE, DamageSource.LIGHTNING_BOLT, SpectrumParticleTypes.WHITE_EXPLOSION, 0xaff3eb));
	public static final ExplosionModifier MAGIC = registerModifier("magic_damage", new DamageChangingModifier(DAMAGE_SOURCE_TYPE, DamageSource.MAGIC, SpectrumParticleTypes.PURPLE_CRAFTING, 0xff59ff));
	public static final ExplosionModifier LOOTING = registerModifier("looting_damage", new DamageChangingModifier(DAMAGE_SOURCE_TYPE, SpectrumDamageSources.MIDNIGHT_SOLUTION, ParticleTypes.ENCHANT, 0x5433a5));
	
	// Cosmetic
	public static final ExplosionModifier STARRY = registerModifier("starry", new ParticleAddingModifier(COSMETIC_TYPE, ParticleTypes.END_ROD, 0xc3c8d4));
	
	
	private static <T extends ExplosionModifier> T registerModifier(String name, T modifier) {
		return Registry.register(SpectrumRegistries.EXPLOSION_MODIFIERS, SpectrumCommon.locate(name), modifier);
	}
	
	private static ExplosionModifierType registerModifierType(String name, ExplosionModifierType type) {
		return Registry.register(SpectrumRegistries.EXPLOSION_MODIFIER_TYPES, SpectrumCommon.locate(name), type);
	}
	
	public static void register() {
	
	}
	
}
