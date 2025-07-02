package de.dafuqs.spectrum.registries;

import de.dafuqs.additionalentityattributes.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;

import static de.dafuqs.spectrum.SpectrumCommon.*;

public class SpectrumStatusEffects {

	public static final int ETERNAL_SLUMBER_COLOR = 0xc35fee;
	public static boolean effectsAreGettingStacked = false;
	
	/**
	 * Clears negative effects on the entity
	 * and makes it immune against new ones
	 */
	public static final Holder<MobEffect> IMMUNITY = registerStatusEffect("immunity", new ImmunityStatusEffect(MobEffectCategory.NEUTRAL, 0x4bbed5)
			.addAttributeModifier(SpectrumEntityAttributes.MENTAL_PRESENCE, locate("effect.immunity"), 1.0, AttributeModifier.Operation.ADD_VALUE));
	
	/**
	 * Like Saturation, but not OP
	 */
	public static final Holder<MobEffect> NOURISHING = registerStatusEffect("nourishing", new NourishingStatusEffect(MobEffectCategory.BENEFICIAL, 0x2324f8));
	
	/**
	 * Rerolls loot table entry counts based on chance (like with fortune/looting) and takes the best one
	 */
	public static final Holder<MobEffect> ANOTHER_ROLL = registerStatusEffect("another_roll", new NoopStatusEffect(MobEffectCategory.BENEFICIAL, 0xa1ce00));
	
	/**
	 * Stops natural regeneration
	 * and prevents sprinting
	 */
	public static final Holder<MobEffect> SCARRED = registerStatusEffect("scarred", new ScarredStatusEffect(MobEffectCategory.HARMFUL, 0x5b1d1d));
	
	/**
	 * Increases all incoming damage by potency %
	 */
	public static final float VULNERABILITY_ADDITIONAL_DAMAGE_PERCENT_PER_LEVEL = 0.25F;
	public static final Holder<MobEffect> VULNERABILITY = registerStatusEffect("vulnerability", new NoopStatusEffect(MobEffectCategory.HARMFUL, 0x353535));
	
	/**
	 * Removes gravity to the entity
	 * entities will fall slower or start levitating with high potency
	 */
	public static final Holder<MobEffect> LIGHTWEIGHT = registerStatusEffect("lightweight", new GravityStatusEffect(MobEffectCategory.NEUTRAL, 0x00dde4, 0.02F));
	
	/**
	 * Adds gravity to the entity
	 * flying mobs will fall and be nearly unable to fall (phantoms, ghasts)
	 */
	public static final Holder<MobEffect> DENSITY = registerStatusEffect("density", new GravityStatusEffect(MobEffectCategory.HARMFUL, 0x671a25, -0.02F));
	
	/**
	 * Increases attack speed
	 */
	public static final Holder<MobEffect> SWIFTNESS = registerStatusEffect("swiftness", new NoopStatusEffect(MobEffectCategory.BENEFICIAL, 0xffe566)
			.addAttributeModifier(Attributes.ATTACK_SPEED, locate("effect.swiftness"), 0.2D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
	
	/**
	 * Decreases attack speed
	 */
	public static final Holder<MobEffect> STIFFNESS = registerStatusEffect("stiffness", new NoopStatusEffect(MobEffectCategory.HARMFUL, 0x7e7549)
			.addAttributeModifier(Attributes.ATTACK_SPEED, locate("effect.stiffness"), -0.2D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
	
	/**
	 * Reduces incoming magic damage by 1 point / level
	 */
	public static final Holder<MobEffect> MAGIC_ANNULATION = registerStatusEffect("magic_annulation", new NoopStatusEffect(MobEffectCategory.BENEFICIAL, 0x7a1082)
			.addAttributeModifier(AdditionalEntityAttributes.MAGIC_PROTECTION, locate("effect.magic_annulation"), 1, AttributeModifier.Operation.ADD_VALUE));
	
	/**
	 * Like poison, but is able to kill
	 */
	public static final Holder<MobEffect> DEADLY_POISON = registerStatusEffect("deadly_poison", new DeadlyPoisonStatusEffect(MobEffectCategory.HARMFUL, 5149489));
	
	/**
	 * Increased toughness. Simple, effective
	 */
	public static final Holder<MobEffect> TOUGHNESS = registerStatusEffect("toughness", new NoopStatusEffect(MobEffectCategory.BENEFICIAL, 0x28bbe0)
			.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, locate("effect.toughness"), 1.0, AttributeModifier.Operation.ADD_VALUE));
	
	/**
	 * Increases the durations of other effects
	 */
	public static final Holder<MobEffect> EFFECT_PROLONGING = registerStatusEffect("effect_prolonging", new EffectProlongingStatusEffect(MobEffectCategory.BENEFICIAL, 0xc081d5));
	
	/**
	 * Reduced health over time
	 */
	public static final Holder<MobEffect> LIFE_DRAIN = registerStatusEffect("life_drain", new LifeDrainStatusEffect(MobEffectCategory.HARMFUL, 0x222222)
			.addAttributeModifier(Attributes.MAX_HEALTH, LifeDrainStatusEffect.ATTRIBUTE_ID, -1.0, AttributeModifier.Operation.ADD_VALUE));
	
	/**
	 * Gives loads of buffs, but the player will be handled as if they were playing hardcore
	 */
	public static final Holder<MobEffect> ASCENSION = registerStatusEffect("ascension", new AscensionStatusEffect(MobEffectCategory.BENEFICIAL, 0xdff9fc));
	public static final Holder<MobEffect> DIVINITY = registerStatusEffect("divinity", new DivinityStatusEffect(MobEffectCategory.BENEFICIAL, 0xdff9fc)
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
	public static final Holder<MobEffect> FRENZY = registerStatusEffect("frenzy", new FrenzyStatusEffect(MobEffectCategory.NEUTRAL, 0x990000)
			.addAttributeModifier(Attributes.ATTACK_SPEED, locate("effect.frenzy.attack_speed"), 0.1D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
			.addAttributeModifier(Attributes.ATTACK_DAMAGE, locate("effect.frenzy.attack_damage"), 0.5D, AttributeModifier.Operation.ADD_VALUE)
			.addAttributeModifier(Attributes.MOVEMENT_SPEED, locate("effect.frenzy.movement_speed"), 0.1D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
			.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, locate("effect.frenzy.knockback_resistance"), 0.25D, AttributeModifier.Operation.ADD_VALUE)
			.addAttributeModifier(SpectrumEntityAttributes.MENTAL_PRESENCE, locate("effect.frenzy.mental_presence"), 5, AttributeModifier.Operation.ADD_VALUE));
	
	/**
	 * Increases speed and visibility in lava
	 */
	public static final Holder<MobEffect> LAVA_GLIDING = registerStatusEffect("lava_gliding", new NoopStatusEffect(MobEffectCategory.BENEFICIAL, 0xc42e0e)
			.addAttributeModifier(AdditionalEntityAttributes.LAVA_SPEED, locate("effect.lava_gliding.lava_speed"), 0.1D, AttributeModifier.Operation.ADD_VALUE)
			.addAttributeModifier(AdditionalEntityAttributes.LAVA_VISIBILITY, locate("effect.lava_gliding.lava_visibility"), 8.0D, AttributeModifier.Operation.ADD_VALUE));

	/**
	 * Reduces detection range and enemy spawn rates
	 */
	public static final Holder<MobEffect> CALMING = registerStatusEffect("calming", new SleepStatusEffect(MobEffectCategory.BENEFICIAL, 0x5fd7b3, true)
			.addAttributeModifier(AdditionalEntityAttributes.MOB_DETECTION_RANGE, locate("effect.calming.mob_detection_range"), -0.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
			.addAttributeModifier(SpectrumEntityAttributes.MENTAL_PRESENCE, locate("effect.calming.mental_presence"), -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

	/**
	 * Slows down enemy AI and causes them to forget their target at times.
	 * ON PLAYER: removes UI elements and reduces acceleration
	 */
	public static final Holder<MobEffect> SOMNOLENCE = registerStatusEffect("somnolence", new SleepStatusEffect(MobEffectCategory.NEUTRAL, 0xae7bec, true)
			.addAttributeModifier(SpectrumEntityAttributes.MENTAL_PRESENCE, locate("effect.somnolence"), -0.5, AttributeModifier.Operation.ADD_VALUE));

	/**
	 * Like somnolence, but stronger and does not naturally end most of the time.
	 */
	public static final Holder<MobEffect> ETERNAL_SLUMBER = registerStatusEffect("eternal_slumber", new SleepStatusEffect(MobEffectCategory.HARMFUL, ETERNAL_SLUMBER_COLOR, false)
			.addAttributeModifier(SpectrumEntityAttributes.MENTAL_PRESENCE, locate("effect.eternal_slumber"), -2.0, AttributeModifier.Operation.ADD_VALUE));

	/**
	 * Kills you if it runs out naturally.
	 */
	public static final Holder<MobEffect> FATAL_SLUMBER = registerStatusEffect("fatal_slumber", new SleepStatusEffect(MobEffectCategory.HARMFUL, 0x8136c2, false)
			.addAttributeModifier(SpectrumEntityAttributes.MENTAL_PRESENCE, locate("effect.fatal_slumber"), -2.0, AttributeModifier.Operation.ADD_VALUE));
	
	/**
	 * % Chance to protect from projectiles per level
	 */
	public static final float PROJECTILE_REBOUND_CHANCE_PER_LEVEL = 0.2F;
	public static final Holder<MobEffect> PROJECTILE_REBOUND = registerStatusEffect("projectile_rebound", new NoopStatusEffect(MobEffectCategory.BENEFICIAL, 0x77e6df));
	
	
	private static Holder<MobEffect> registerStatusEffect(String id, MobEffect entry) {
		return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, locate(id), entry);
	}
	
	public static void register() {
	
	}
	
	public static boolean isStrongSleepEffect(MobEffectInstance instance) {
		return instance.getEffect() == ETERNAL_SLUMBER || instance.getEffect() == FATAL_SLUMBER;
	}

	public static boolean isStrongSleepEffect(InkPoweredStatusEffectInstance instance) {
		return isStrongSleepEffect(instance.getStatusEffectInstance());
	}
	
	public static void cutDuration(LivingEntity instance, MobEffectInstance effect) {
		// new duration = duration - 1min OR duration * 0.4, whichever is the smaller reduction
		int duration = effect.getDuration();
		((StatusEffectInstanceAccessor) effect).setDuration(Math.max(duration - 1200, (int) (duration * 0.4)));
		if (instance.level() instanceof ServerLevel serverWorld) {
			serverWorld.getChunkSource().broadcastAndSend(instance, new ClientboundUpdateMobEffectPacket(instance.getId(), effect, false));
		}
	}
	
}
