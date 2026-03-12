package de.dafuqs.spectrum.registries;

import de.dafuqs.additionalentityattributes.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.mob_effect.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

import java.util.function.*;

import static de.dafuqs.spectrum.SpectrumCommon.*;

public class SpectrumMobEffects {
	
	public static final int ETERNAL_SLUMBER_COLOR = 0xc35fee;
	public static boolean effectsAreGettingStacked = false;
	
	private static final DeferredRegister<MobEffect> REGISTRAR = DeferredRegister.create(Registries.MOB_EFFECT, SpectrumCommon.MOD_ID);
	
	/**
	 * Clears negative effects on the entity
	 * and makes it immune against new ones
	 */
	public static final Holder<MobEffect> IMMUNITY = register("immunity", () -> new ImmunityMobEffect(MobEffectCategory.NEUTRAL, 0x4bbed5)
			.addAttributeModifier(SpectrumEntityAttributes.MENTAL_PRESENCE, locate("effect.immunity"), 1.0, AttributeModifier.Operation.ADD_VALUE));
	
	/**
	 * Like Saturation, but not OP
	 */
	public static final Holder<MobEffect> NOURISHING = register("nourishing", () -> new NourishingMobEffect(MobEffectCategory.BENEFICIAL, 0x2324f8));
	
	/**
	 * Rerolls loot table entry counts based on chance (like with fortune/looting) and takes the best one
	 */
	public static final Holder<MobEffect> ANOTHER_ROLL = register("another_roll", () -> new NoopMobEffect(MobEffectCategory.BENEFICIAL, 0xa1ce00));
	
	/**
	 * Stops natural regeneration
	 * and prevents sprinting
	 */
	public static final Holder<MobEffect> SCARRED = register("scarred", () -> new ScarredMobEffect(MobEffectCategory.HARMFUL, 0x5b1d1d));
	
	/**
	 * Increases all incoming damage by potency %
	 */
	public static final float VULNERABILITY_ADDITIONAL_DAMAGE_PERCENT_PER_LEVEL = 0.25F;
	public static final Holder<MobEffect> VULNERABILITY = register("vulnerability", () -> new NoopMobEffect(MobEffectCategory.HARMFUL, 0x353535));
	
	/**
	 * Removes gravity to the entity
	 * entities will fall slower or start levitating with high potency
	 */
	public static final Holder<MobEffect> LIGHTWEIGHT = register("lightweight", () -> new GravityMobEffect(MobEffectCategory.NEUTRAL, 0x00dde4, 0.02F));
	
	/**
	 * Adds gravity to the entity
	 * flying mobs will fall and be nearly unable to fall (phantoms, ghasts)
	 */
	public static final Holder<MobEffect> DENSITY = register("density", () -> new GravityMobEffect(MobEffectCategory.HARMFUL, 0x671a25, -0.02F));
	
	/**
	 * Increases attack speed
	 */
	public static final Holder<MobEffect> SWIFTNESS = register("swiftness", () -> new NoopMobEffect(MobEffectCategory.BENEFICIAL, 0xffe566)
			.addAttributeModifier(Attributes.ATTACK_SPEED, locate("effect.swiftness"), 0.2D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
	
	/**
	 * Decreases attack speed
	 */
	public static final Holder<MobEffect> STIFFNESS = register("stiffness", () -> new NoopMobEffect(MobEffectCategory.HARMFUL, 0x7e7549)
			.addAttributeModifier(Attributes.ATTACK_SPEED, locate("effect.stiffness"), -0.2D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
	
	/**
	 * Reduces incoming magic damage by 1 point / level
	 */
	public static final Holder<MobEffect> MAGIC_ANNULATION = register("magic_annulation", () -> new NoopMobEffect(MobEffectCategory.BENEFICIAL, 0x7a1082)
			.addAttributeModifier(AdditionalEntityAttributes.MAGIC_PROTECTION, locate("effect.magic_annulation"), 1, AttributeModifier.Operation.ADD_VALUE));
	
	/**
	 * Like poison, but is able to kill
	 */
	public static final Holder<MobEffect> DEADLY_POISON = register("deadly_poison", () -> new DeadlyPoisonMobEffect(MobEffectCategory.HARMFUL, 5149489));
	
	/**
	 * Increased toughness. Simple, effective
	 */
	public static final Holder<MobEffect> TOUGHNESS = register("toughness", () -> new NoopMobEffect(MobEffectCategory.BENEFICIAL, 0x28bbe0)
			.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, locate("effect.toughness"), 1.0, AttributeModifier.Operation.ADD_VALUE));
	
	/**
	 * Increases the durations of other effects
	 */
	public static final Holder<MobEffect> EFFECT_PROLONGING = register("effect_prolonging", () -> new NoopMobEffect(MobEffectCategory.BENEFICIAL, 0xc081d5));
	
	/**
	 * Reduced health over time
	 */
	public static final Holder<MobEffect> LIFE_DRAIN = register("life_drain", () -> new LifeDrainMobEffect(MobEffectCategory.HARMFUL, 0x222222)
			.addAttributeModifier(Attributes.MAX_HEALTH, LifeDrainMobEffect.ATTRIBUTE_ID, -1.0, AttributeModifier.Operation.ADD_VALUE));
	
	/**
	 * Gives loads of buffs, but the player will be handled as if they were playing hardcore
	 */
	public static final Holder<MobEffect> ASCENSION = register("ascension", () -> new AscensionMobEffect(MobEffectCategory.BENEFICIAL, 0xdff9fc));
	public static final Holder<MobEffect> DIVINITY = register("divinity", () -> new DivinityMobEffect(MobEffectCategory.BENEFICIAL, 0xdff9fc)
			.addAttributeModifier(Attributes.ATTACK_SPEED, locate("effect.divinity.attack_speed"), 0.1D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
			.addAttributeModifier(Attributes.MOVEMENT_SPEED, locate("effect.divinity.movement_speed"), 0.2D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
			.addAttributeModifier(Attributes.ATTACK_DAMAGE, locate("effect.divinity.attack_damage"), 2.0D, AttributeModifier.Operation.ADD_VALUE)
			.addAttributeModifier(Attributes.ATTACK_KNOCKBACK, locate("effect.divinity.attack_knockback"), 1.0D, AttributeModifier.Operation.ADD_VALUE)
			.addAttributeModifier(Attributes.ARMOR, locate("effect.divinity.armor"), 2.0D, AttributeModifier.Operation.ADD_VALUE)
			.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, locate("effect.divinity.armor_toughness"), 2.0D, AttributeModifier.Operation.ADD_VALUE)
			.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, locate("effect.divinity.knockback_resistance"), 1.0D, AttributeModifier.Operation.ADD_VALUE)
			.addAttributeModifier(SpectrumEntityAttributes.MENTAL_PRESENCE, locate("effect.divinity.mental_presence"), 0.25, AttributeModifier.Operation.ADD_VALUE));
	
	/**
	 * Damage, attack speed, speed & knockback resistance are buffed the more the player kills.
	 * But if they do not score a kill in 20 seconds, they get negative effects.
	 * Stacking $(thing)Frenzy$() (applying the effect while they already have it) increases this effects amplitude
	 */
	public static final Holder<MobEffect> FRENZY = register("frenzy", () -> new FrenzyMobEffect(MobEffectCategory.NEUTRAL, 0x990000)
			.addAttributeModifier(Attributes.ATTACK_SPEED, locate("effect.frenzy.attack_speed"), 0.1D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
			.addAttributeModifier(Attributes.ATTACK_DAMAGE, locate("effect.frenzy.attack_damage"), 0.5D, AttributeModifier.Operation.ADD_VALUE)
			.addAttributeModifier(Attributes.MOVEMENT_SPEED, locate("effect.frenzy.movement_speed"), 0.1D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
			.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, locate("effect.frenzy.knockback_resistance"), 0.25D, AttributeModifier.Operation.ADD_VALUE)
			.addAttributeModifier(SpectrumEntityAttributes.MENTAL_PRESENCE, locate("effect.frenzy.mental_presence"), 5, AttributeModifier.Operation.ADD_VALUE));
	
	/**
	 * Increases speed and visibility in lava
	 */
	public static final Holder<MobEffect> LAVA_GLIDING = register("lava_gliding", () -> new NoopMobEffect(MobEffectCategory.BENEFICIAL, 0xc42e0e)
			.addAttributeModifier(AdditionalEntityAttributes.LAVA_SPEED, locate("effect.lava_gliding.lava_speed"), 0.2D, AttributeModifier.Operation.ADD_VALUE)
			.addAttributeModifier(AdditionalEntityAttributes.LAVA_VISIBILITY, locate("effect.lava_gliding.lava_visibility"), 8.0D, AttributeModifier.Operation.ADD_VALUE));
	
	/**
	 * Reduces detection range and enemy spawn rates
	 */
	public static final Holder<MobEffect> CALMING = register("calming", () -> new SleepMobEffect(MobEffectCategory.BENEFICIAL, 0x5fd7b3, true)
			.addAttributeModifier(AdditionalEntityAttributes.MOB_DETECTION_RANGE, locate("effect.calming.mob_detection_range"), -0.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
			.addAttributeModifier(SpectrumEntityAttributes.MENTAL_PRESENCE, locate("effect.calming.mental_presence"), -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
	
	/**
	 * Slows down enemy AI and causes them to forget their target at times.
	 * ON PLAYER: removes UI elements and reduces acceleration
	 */
	public static final Holder<MobEffect> SOMNOLENCE = register("somnolence", () -> new SleepMobEffect(MobEffectCategory.NEUTRAL, 0xae7bec, true)
			.addAttributeModifier(SpectrumEntityAttributes.MENTAL_PRESENCE, locate("effect.somnolence"), -0.5, AttributeModifier.Operation.ADD_VALUE));
	
	/**
	 * Like somnolence, but stronger and does not naturally end most of the time.
	 */
	public static final Holder<MobEffect> ETERNAL_SLUMBER = register("eternal_slumber", () -> new SleepMobEffect(MobEffectCategory.HARMFUL, ETERNAL_SLUMBER_COLOR, false)
			.addAttributeModifier(SpectrumEntityAttributes.MENTAL_PRESENCE, locate("effect.eternal_slumber"), -2.0, AttributeModifier.Operation.ADD_VALUE));
	
	/**
	 * Kills you if it runs out naturally.
	 */
	public static final Holder<MobEffect> FATAL_SLUMBER = register("fatal_slumber", () -> new SleepMobEffect(MobEffectCategory.HARMFUL, 0x8136c2, false)
			.addAttributeModifier(SpectrumEntityAttributes.MENTAL_PRESENCE, locate("effect.fatal_slumber"), -2.0, AttributeModifier.Operation.ADD_VALUE));
	
	/**
	 * % Chance to protect from projectiles per level
	 */
	public static final float PROJECTILE_REBOUND_CHANCE_PER_LEVEL = 0.2F;
	public static final Holder<MobEffect> PROJECTILE_REBOUND = register("projectile_rebound", () -> new NoopMobEffect(MobEffectCategory.BENEFICIAL, 0x77e6df));
	
	
	private static Holder<MobEffect> register(String id, Supplier<MobEffect> entry) {
		return REGISTRAR.register(id, entry);
	}
	
	public static void register(IEventBus modBus) {
		REGISTRAR.register(modBus);
	}
	
}
