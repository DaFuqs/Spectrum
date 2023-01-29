package de.dafuqs.spectrum.recipe.potion_workshop;

import com.google.gson.*;
import net.minecraft.network.*;
import net.minecraft.util.*;

public class PotionMod {
	public int flatDurationBonusTicks = 0;
	public float flatPotencyBonus = 0.0F;
	
	public float multiplicativeDurationModifier = 1.0F;
	public float multiplicativePotencyModifier = 1.0F;
	
	public float flatPotencyBonusPositiveEffects = 0.0F;
	public float flatPotencyBonusNegativeEffects = 0.0F;
	
	public float additionalRandomPositiveEffectCount = 0;
	public float additionalRandomNegativeEffectCount = 0;
	
	public float chanceToAddLastEffect = 0.0F;
	public float lastEffectPotencyModifier = 1.0F;

	public float flatYieldBonus = 0;

	public boolean makeSplashing = false;
	public boolean makeLingering = false;

	public boolean noParticles = false;
	public boolean unidentifiable = false;
	public boolean makeEffectsPositive = false;
	public boolean potentDecreasingEffect = false;
	public boolean negateDecreasingDuration = false;
	public boolean randomColor = false;
	public int additionalDrinkDurationTicks = 0;

	public static PotionMod fromJson(JsonObject jsonObject) {
		PotionMod mod = new PotionMod();

		if (JsonHelper.hasNumber(jsonObject, "flat_duration_bonus_ticks")) {
			mod.flatDurationBonusTicks += JsonHelper.getInt(jsonObject, "flat_duration_bonus_ticks");
		}
		if (JsonHelper.hasNumber(jsonObject, "flat_potency_bonus")) {
			mod.flatPotencyBonus += JsonHelper.getFloat(jsonObject, "flat_potency_bonus");
		}
		if (JsonHelper.hasNumber(jsonObject, "multiplicative_duration_modifier")) {
			mod.multiplicativeDurationModifier = JsonHelper.getFloat(jsonObject, "multiplicative_duration_modifier");
		}
		if (JsonHelper.hasNumber(jsonObject, "multiplicative_potency_modifier")) {
			mod.multiplicativePotencyModifier = JsonHelper.getFloat(jsonObject, "multiplicative_potency_modifier");
		}
		if (JsonHelper.hasNumber(jsonObject, "flat_potency_bonus_positive_effects")) {
			mod.flatPotencyBonusPositiveEffects += JsonHelper.getFloat(jsonObject, "flat_potency_bonus_positive_effects");
		}
		if (JsonHelper.hasNumber(jsonObject, "flat_potency_bonus_negative_effects")) {
			mod.flatPotencyBonusNegativeEffects += JsonHelper.getFloat(jsonObject, "flat_potency_bonus_negative_effects");
		}
		if (JsonHelper.hasNumber(jsonObject, "additional_random_positive_effect_count")) {
			mod.additionalRandomPositiveEffectCount += JsonHelper.getFloat(jsonObject, "additional_random_positive_effect_count");
		}
		if (JsonHelper.hasNumber(jsonObject, "additional_random_negative_effect_count")) {
			mod.additionalRandomNegativeEffectCount += JsonHelper.getFloat(jsonObject, "additional_random_negative_effect_count");
		}
		if (JsonHelper.hasNumber(jsonObject, "chance_to_add_last_effect")) {
			mod.chanceToAddLastEffect += JsonHelper.getFloat(jsonObject, "chance_to_add_last_effect");
		}
		if (JsonHelper.hasNumber(jsonObject, "last_effect_potency_modifier")) {
			mod.lastEffectPotencyModifier = JsonHelper.getFloat(jsonObject, "last_effect_potency_modifier");
		}
		if (JsonHelper.hasNumber(jsonObject, "flat_yield_bonus")) {
			mod.flatYieldBonus += JsonHelper.getFloat(jsonObject, "flat_yield_bonus");
		}
		if (JsonHelper.hasBoolean(jsonObject, "make_splashing")) {
			mod.makeSplashing = JsonHelper.getBoolean(jsonObject, "make_splashing");
		}
		if (JsonHelper.hasBoolean(jsonObject, "make_lingering")) {
			mod.makeLingering = JsonHelper.getBoolean(jsonObject, "make_lingering");
		}
		if (JsonHelper.hasBoolean(jsonObject, "no_particles")) {
			mod.noParticles = JsonHelper.getBoolean(jsonObject, "no_particles");
		}
		if (JsonHelper.hasBoolean(jsonObject, "unidentifiable")) {
			mod.unidentifiable = JsonHelper.getBoolean(jsonObject, "unidentifiable");
		}
		if (JsonHelper.hasBoolean(jsonObject, "make_effects_positive")) {
			mod.makeEffectsPositive = JsonHelper.getBoolean(jsonObject, "make_effects_positive");
		}
		if (JsonHelper.hasBoolean(jsonObject, "potent_decreasing_effect")) {
			mod.potentDecreasingEffect = JsonHelper.getBoolean(jsonObject, "potent_decreasing_effect");
		}
		if (JsonHelper.hasBoolean(jsonObject, "negate_decreasing_duration")) {
			mod.negateDecreasingDuration = JsonHelper.getBoolean(jsonObject, "negate_decreasing_duration");
		}
		if (JsonHelper.hasNumber(jsonObject, "additional_drink_duration_ticks")) {
			mod.additionalDrinkDurationTicks = JsonHelper.getInt(jsonObject, "additional_drink_duration_ticks");
		}
		if (JsonHelper.hasBoolean(jsonObject, "random_color")) {
			mod.randomColor = JsonHelper.getBoolean(jsonObject, "random_color");
		}

		return mod;
	}
	
	public void write(PacketByteBuf packetByteBuf) {
		packetByteBuf.writeInt(flatDurationBonusTicks);
		packetByteBuf.writeFloat(flatPotencyBonus);
		packetByteBuf.writeFloat(multiplicativeDurationModifier);
		packetByteBuf.writeFloat(multiplicativePotencyModifier);
		packetByteBuf.writeFloat(flatPotencyBonusPositiveEffects);
		packetByteBuf.writeFloat(flatPotencyBonusNegativeEffects);
		packetByteBuf.writeFloat(additionalRandomPositiveEffectCount);
		packetByteBuf.writeFloat(additionalRandomNegativeEffectCount);
		packetByteBuf.writeFloat(chanceToAddLastEffect);
		packetByteBuf.writeFloat(lastEffectPotencyModifier);
		packetByteBuf.writeFloat(flatYieldBonus);
		packetByteBuf.writeBoolean(makeSplashing);
		packetByteBuf.writeBoolean(makeLingering);
		packetByteBuf.writeBoolean(noParticles);
		packetByteBuf.writeBoolean(unidentifiable);
		packetByteBuf.writeBoolean(makeEffectsPositive);
		packetByteBuf.writeBoolean(potentDecreasingEffect);
		packetByteBuf.writeBoolean(negateDecreasingDuration);
		packetByteBuf.writeInt(additionalDrinkDurationTicks);
		packetByteBuf.writeBoolean(randomColor);
	}
	
	public static PotionMod fromPacket(PacketByteBuf packetByteBuf) {
		PotionMod potionMod = new PotionMod();
		potionMod.flatDurationBonusTicks = packetByteBuf.readInt();
		potionMod.flatPotencyBonus = packetByteBuf.readFloat();
		potionMod.multiplicativeDurationModifier = packetByteBuf.readFloat();
		potionMod.multiplicativePotencyModifier = packetByteBuf.readFloat();
		potionMod.flatPotencyBonusPositiveEffects = packetByteBuf.readFloat();
		potionMod.flatPotencyBonusNegativeEffects = packetByteBuf.readFloat();
		potionMod.additionalRandomPositiveEffectCount = packetByteBuf.readFloat();
		potionMod.additionalRandomNegativeEffectCount = packetByteBuf.readFloat();
		potionMod.chanceToAddLastEffect = packetByteBuf.readFloat();
		potionMod.lastEffectPotencyModifier = packetByteBuf.readFloat();
		potionMod.flatYieldBonus = packetByteBuf.readFloat();
		potionMod.makeSplashing = packetByteBuf.readBoolean();
		potionMod.makeLingering = packetByteBuf.readBoolean();
		potionMod.noParticles = packetByteBuf.readBoolean();
		potionMod.unidentifiable = packetByteBuf.readBoolean();
		potionMod.makeEffectsPositive = packetByteBuf.readBoolean();
		potionMod.potentDecreasingEffect = packetByteBuf.readBoolean();
		potionMod.negateDecreasingDuration = packetByteBuf.readBoolean();
		potionMod.additionalDrinkDurationTicks = packetByteBuf.readInt();
		potionMod.randomColor = packetByteBuf.readBoolean();
		return potionMod;
	}
	
	public PotionMod modify(PotionMod potionMod) {
		potionMod.flatDurationBonusTicks += this.flatDurationBonusTicks;
		potionMod.flatPotencyBonus += this.flatPotencyBonus;
		potionMod.multiplicativeDurationModifier *= this.multiplicativeDurationModifier;
		potionMod.multiplicativePotencyModifier *= this.multiplicativePotencyModifier;
		potionMod.flatPotencyBonusPositiveEffects += this.flatPotencyBonusPositiveEffects;
		potionMod.flatPotencyBonusNegativeEffects += this.flatPotencyBonusNegativeEffects;
		potionMod.additionalRandomPositiveEffectCount += this.additionalRandomPositiveEffectCount;
		potionMod.additionalRandomNegativeEffectCount += this.additionalRandomNegativeEffectCount;
		potionMod.chanceToAddLastEffect += this.chanceToAddLastEffect;
		potionMod.lastEffectPotencyModifier *= this.lastEffectPotencyModifier;
		potionMod.flatYieldBonus += this.flatYieldBonus;
		potionMod.makeSplashing |= this.makeSplashing;
		potionMod.makeLingering |= this.makeLingering;
		potionMod.noParticles |= this.noParticles;
		potionMod.unidentifiable |= this.unidentifiable;
		potionMod.makeEffectsPositive |= this.makeEffectsPositive;
		potionMod.potentDecreasingEffect |= this.potentDecreasingEffect;
		potionMod.negateDecreasingDuration |= this.negateDecreasingDuration;
		potionMod.additionalDrinkDurationTicks += this.additionalDrinkDurationTicks;
		potionMod.randomColor |= this.randomColor;
		return potionMod;
	}
	
}