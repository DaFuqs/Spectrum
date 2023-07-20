package de.dafuqs.spectrum.registries;

import de.dafuqs.additionalentityattributes.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.*;
import net.minecraft.registry.*;

public class SpectrumStatusEffects {
	
	public static boolean effectsAreGettingStacked = false;
	
	/**
	 * Clears negative effects on the entity
	 * and makes it immune against new ones
	 */
	public static final StatusEffect IMMUNITY = registerStatusEffect("immunity", new ImmunityStatusEffect(StatusEffectCategory.NEUTRAL, 0x4bbed5));
	
	/**
	 * Like Saturation, but not OP
	 */
	public static final StatusEffect NOURISHING = registerStatusEffect("nourishing", new NourishingStatusEffect(StatusEffectCategory.BENEFICIAL, 0x2324f8));
	
	/**
	 * Rerolls loot table entry counts based on chance (like with fortune/looting) and takes the best one
	 */
	public static final StatusEffect ANOTHER_ROLL = registerStatusEffect("another_roll", new SpectrumStatusEffect(StatusEffectCategory.BENEFICIAL, 0xa1ce00));
	
	/**
	 * Stops natural regeneration
	 * and prevents sprinting
	 */
	public static final StatusEffect SCARRED = registerStatusEffect("scarred", new ScarredStatusEffect(StatusEffectCategory.HARMFUL, 0x5b1d1d));
	
	/**
	 * Increases all incoming damage by potency %
	 */
	public static final float VULNERABILITY_ADDITIONAL_DAMAGE_PERCENT_PER_LEVEL = 0.25F;
	public static final StatusEffect VULNERABILITY = registerStatusEffect("vulnerability", new SpectrumStatusEffect(StatusEffectCategory.HARMFUL, 0x353535));
	
	/**
	 * Removes gravity to the entity
	 * entities will fall slower and with high potencies start levitating
	 */
	public static final StatusEffect LIGHTWEIGHT = registerStatusEffect("lightweight", new GravityStatusEffect(StatusEffectCategory.NEUTRAL, 0x00dde4, 0.02F));
	
	/**
	 * Adds gravity to the entity
	 * flying mobs will fall and be nearly unable to fall (phantom, ghast)
	 */
	public static final StatusEffect DENSITY = registerStatusEffect("density", new GravityStatusEffect(StatusEffectCategory.HARMFUL, 0x671a25, -0.02F));
	
	/**
	 * Increases attack speed
	 */
	public static final StatusEffect SWIFTNESS = registerStatusEffect("swiftness", new SpectrumStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffe566)
			.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "3c2c6c5e-0a9f-4a0a-8ded-314ae028a753", 2 * 0.10000000149011612D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
	
	/**
	 * Decreases attack speed
	 */
	public static final StatusEffect STIFFNESS = registerStatusEffect("stiffness", new SpectrumStatusEffect(StatusEffectCategory.HARMFUL, 0x7e7549))
			.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "91e58b5a-d8d9-4037-a520-18c3d7230502", 2 * -0.10000000149011612D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
	
	/**
	 * Reduces incoming magic damage by 1 point / level
	 */
	public static final StatusEffect MAGIC_ANNULATION = registerStatusEffect("magic_annulation", new SpectrumStatusEffect(StatusEffectCategory.BENEFICIAL, 0x7a1082))
			.addAttributeModifier(AdditionalEntityAttributes.MAGIC_PROTECTION, "2d307e1f-fcc5-4c53-9821-3a7da4a6ef19", 1, EntityAttributeModifier.Operation.ADDITION);
	
	/**
	 * Like poison, but is able to kill
	 */
	public static final StatusEffect DEADLY_POISON = registerStatusEffect("deadly_poison", new DeadlyPoisonStatusEffect(StatusEffectCategory.HARMFUL, 5149489));
	
	/**
	 * Increases toughness. Simple, effective
	 */
	public static final StatusEffect TOUGHNESS = registerStatusEffect("toughness", new SpectrumStatusEffect(StatusEffectCategory.BENEFICIAL, 0x28bbe0)
			.addAttributeModifier(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, "599817d7-e8d2-4cbc-962b-59b7050ca59c", 1.0, EntityAttributeModifier.Operation.ADDITION));
	
	/**
	 * Ouch.
	 */
	public static final StatusEffect EFFECT_PROLONGING = registerStatusEffect("effect_prolonging", new EffectProlongingStatusEffect(StatusEffectCategory.BENEFICIAL, 0xc081d5));
	
	/**
	 * Ouch.
	 */
	public static final StatusEffect LIFE_DRAIN = registerStatusEffect("life_drain", new LifeDrainStatusEffect(StatusEffectCategory.NEUTRAL, 0x222222)
			.addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, LifeDrainStatusEffect.ATTRIBUTE_UUID_STRING, -1.0, EntityAttributeModifier.Operation.ADDITION));
	
	/**
	 * Gives loads of buffs, but the player will be handled as if they were playing hardcore
	 */
	public static final StatusEffect ASCENSION = registerStatusEffect("ascension", new AscensionStatusEffect(StatusEffectCategory.BENEFICIAL, 0xdff9fc));
	public static final StatusEffect DIVINITY = registerStatusEffect("divinity", new DivinityStatusEffect(StatusEffectCategory.BENEFICIAL, 0xdff9fc)
			.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "2a0a2299-1387-47eb-a120-58bc70a739d8", 2 * 0.10000000149011612D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
			.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "b8b33b2c-1804-4ec6-9430-7d1a85f9b13b", 2 * 0.20000000298023224D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
			.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "b03b6e37-1dc5-4a93-bbae-0ea96c5bd8f8", 5.0D, EntityAttributeModifier.Operation.ADDITION)
			.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, "f9e4ae93-2cf5-4ef5-b06a-ae4fefd5c035", 2.0D, EntityAttributeModifier.Operation.ADDITION)
			.addAttributeModifier(EntityAttributes.GENERIC_ARMOR, "ce69cebb-c3fe-4f00-8d4a-0e3d524f237e", 5.0D, EntityAttributeModifier.Operation.ADDITION)
			.addAttributeModifier(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, "5af92757-cdf2-4443-856c-9f5eb633b1ef", 5.0D, EntityAttributeModifier.Operation.ADDITION)
			.addAttributeModifier(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, "924896a5-8538-4b83-a510-509bccf0a897", 2.0D, EntityAttributeModifier.Operation.ADDITION)
			.addAttributeModifier(AdditionalEntityAttributes.CRITICAL_BONUS_DAMAGE, "9812c88f-dc8e-47d1-a092-38339da9891e", 5.0D, EntityAttributeModifier.Operation.ADDITION)
			.addAttributeModifier(AdditionalEntityAttributes.WATER_SPEED, "9812c88f-dc8e-47d1-a092-38339da9891e", 0.1D, EntityAttributeModifier.Operation.ADDITION));
	
	/**
	 * damage, attack speed, speed & knockback resistance are buffed the more the player kills.
	 * But if they do not score a kill in 20 seconds they get negative effects.
	 * Stacking $(thing)Frenzy$() (applying the effect while they already have it) increases these effects amplitude
	 */
	public static final StatusEffect FRENZY = registerStatusEffect("frenzy", new FrenzyStatusEffect(StatusEffectCategory.NEUTRAL, 0x990000))
			.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, FrenzyStatusEffect.ATTACK_SPEED_UUID_STRING, FrenzyStatusEffect.ATTACK_SPEED_PER_STAGE, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
			.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, FrenzyStatusEffect.ATTACK_DAMAGE_UUID_STRING, FrenzyStatusEffect.ATTACK_DAMAGE_PER_STAGE, EntityAttributeModifier.Operation.ADDITION)
			.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, FrenzyStatusEffect.MOVEMENT_SPEED_UUID_STRING, FrenzyStatusEffect.MOVEMENT_SPEED_PER_STAGE, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
			.addAttributeModifier(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, FrenzyStatusEffect.KNOCKBACK_RESISTANCE_UUID_STRING, FrenzyStatusEffect.KNOCKBACK_RESISTANCE_PER_STAGE, EntityAttributeModifier.Operation.ADDITION);
	
	/**
	 * Increases speed and visibility in lava
	 */
	public static final StatusEffect LAVA_GLIDING = registerStatusEffect("lava_gliding", new SpectrumStatusEffect(StatusEffectCategory.BENEFICIAL, 0xc42e0e)
			.addAttributeModifier(AdditionalEntityAttributes.LAVA_SPEED, "9812c88f-dc8e-47d1-a092-38339da9891e", 0.1D, EntityAttributeModifier.Operation.ADDITION)
			.addAttributeModifier(AdditionalEntityAttributes.LAVA_VISIBILITY, "9812c88f-dc8e-47d1-a092-38339da9891e", 8.0D, EntityAttributeModifier.Operation.ADDITION));
	
	/**
	 * % Chance to protect from projectiles per level
	 */
	public static final float PROJECTILE_REBOUND_CHANCE_PER_LEVEL = 0.2F;
	public static final StatusEffect PROJECTILE_REBOUND = registerStatusEffect("projectile_rebound", new SpectrumStatusEffect(StatusEffectCategory.BENEFICIAL, 0x77e6df));
	
	
	private static StatusEffect registerStatusEffect(String id, StatusEffect entry) {
		return Registry.register(Registries.STATUS_EFFECT, SpectrumCommon.locate(id), entry);
	}
	
	public static void register() {
	
	}
	
}
