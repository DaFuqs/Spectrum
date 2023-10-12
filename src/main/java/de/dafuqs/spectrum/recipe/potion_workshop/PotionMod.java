package de.dafuqs.spectrum.recipe.potion_workshop;

import com.google.gson.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.util.math.random.Random;

import java.util.*;

public class PotionMod {
	
	public int flatDurationBonusTicks = 0;
	public float flatPotencyBonus = 0.0F;
	
	public float durationMultiplier = 1.0F;
	public float potencyMultiplier = 1.0F;
	
	public float flatPotencyBonusPositiveEffects = 0.0F;
	public float flatPotencyBonusNegativeEffects = 0.0F;
	public int flatDurationBonusPositiveEffects = 0;
	public int flatDurationBonusNegativeEffects = 0;
	
	public float additionalRandomPositiveEffectCount = 0;
	public float additionalRandomNegativeEffectCount = 0;
	
	public float chanceToAddLastEffect = 0.0F;
	public float lastEffectDurationMultiplier = 1.0F;
	public float lastEffectPotencyMultiplier = 1.0F;
	
	public float yield = 0;
	public int additionalDrinkDurationTicks = 0;
	
	public boolean makeSplashing = false;
	public boolean makeLingering = false;
	
	public boolean noParticles = false;
	public boolean unidentifiable = false;
	public boolean makeEffectsPositive = false;
	public boolean potentDecreasingEffect = false;
	public boolean negateDecreasingDuration = false;
	public boolean randomColor = false;
	
	public List<Pair<PotionRecipeEffect, Float>> additionalEffects = new ArrayList<>();
	
	
	public static PotionMod fromJson(JsonObject jsonObject) {
		PotionMod mod = new PotionMod();
		
		if (JsonHelper.hasNumber(jsonObject, "flat_duration_bonus_ticks")) {
			mod.flatDurationBonusTicks += JsonHelper.getInt(jsonObject, "flat_duration_bonus_ticks");
		}
		if (JsonHelper.hasNumber(jsonObject, "flat_potency_bonus")) {
			mod.flatPotencyBonus += JsonHelper.getFloat(jsonObject, "flat_potency_bonus");
		}
		if (JsonHelper.hasNumber(jsonObject, "duration_multiplier")) {
			mod.durationMultiplier = JsonHelper.getFloat(jsonObject, "duration_multiplier");
		}
		if (JsonHelper.hasNumber(jsonObject, "potency_multiplier")) {
			mod.potencyMultiplier = JsonHelper.getFloat(jsonObject, "potency_multiplier");
		}
		if (JsonHelper.hasNumber(jsonObject, "flat_potency_bonus_positive_effects")) {
			mod.flatPotencyBonusPositiveEffects += JsonHelper.getFloat(jsonObject, "flat_potency_bonus_positive_effects");
		}
		if (JsonHelper.hasNumber(jsonObject, "flat_potency_bonus_negative_effects")) {
			mod.flatPotencyBonusNegativeEffects += JsonHelper.getFloat(jsonObject, "flat_potency_bonus_negative_effects");
		}
		if (JsonHelper.hasNumber(jsonObject, "flat_duration_bonus_positive_effects")) {
			mod.flatDurationBonusPositiveEffects += JsonHelper.getInt(jsonObject, "flat_duration_bonus_positive_effects");
		}
		if (JsonHelper.hasNumber(jsonObject, "flat_duration_bonus_negative_effects")) {
			mod.flatDurationBonusNegativeEffects += JsonHelper.getInt(jsonObject, "flat_duration_bonus_negative_effects");
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
			mod.lastEffectPotencyMultiplier = JsonHelper.getFloat(jsonObject, "last_effect_potency_modifier");
		}
		if (JsonHelper.hasNumber(jsonObject, "last_effect_duration_modifier")) {
			mod.lastEffectDurationMultiplier = JsonHelper.getFloat(jsonObject, "last_effect_duration_modifier");
		}
		if (JsonHelper.hasNumber(jsonObject, "flat_yield_bonus")) {
			mod.yield += JsonHelper.getFloat(jsonObject, "flat_yield_bonus");
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
		if (JsonHelper.hasArray(jsonObject, "additional_effects")) {
			for (JsonElement e : JsonHelper.getArray(jsonObject, "additional_effects")) {
				if (e instanceof JsonObject effectObject) {
					float chance = JsonHelper.getFloat(effectObject, "chance", 1.0F);
					PotionRecipeEffect effect = PotionRecipeEffect.read(effectObject);
					mod.additionalEffects.add(new Pair<>(effect, chance));
				}
			}
		}
		
		return mod;
	}
	
	public void write(PacketByteBuf packetByteBuf) {
		packetByteBuf.writeInt(flatDurationBonusTicks);
		packetByteBuf.writeFloat(flatPotencyBonus);
		packetByteBuf.writeFloat(durationMultiplier);
		packetByteBuf.writeFloat(potencyMultiplier);
		packetByteBuf.writeFloat(flatPotencyBonusPositiveEffects);
		packetByteBuf.writeFloat(flatPotencyBonusNegativeEffects);
		packetByteBuf.writeInt(flatDurationBonusPositiveEffects);
		packetByteBuf.writeInt(flatDurationBonusNegativeEffects);
		packetByteBuf.writeFloat(additionalRandomPositiveEffectCount);
		packetByteBuf.writeFloat(additionalRandomNegativeEffectCount);
		packetByteBuf.writeFloat(chanceToAddLastEffect);
		packetByteBuf.writeFloat(lastEffectDurationMultiplier);
		packetByteBuf.writeFloat(lastEffectPotencyMultiplier);
		packetByteBuf.writeFloat(yield);
		packetByteBuf.writeBoolean(makeSplashing);
		packetByteBuf.writeBoolean(makeLingering);
		packetByteBuf.writeBoolean(noParticles);
		packetByteBuf.writeBoolean(unidentifiable);
		packetByteBuf.writeBoolean(makeEffectsPositive);
		packetByteBuf.writeBoolean(potentDecreasingEffect);
		packetByteBuf.writeBoolean(negateDecreasingDuration);
		packetByteBuf.writeInt(additionalDrinkDurationTicks);
		packetByteBuf.writeBoolean(randomColor);
		
		packetByteBuf.writeInt(additionalEffects.size());
		for (Pair<PotionRecipeEffect, Float> effectAndChance : additionalEffects) {
			effectAndChance.getLeft().write(packetByteBuf);
			packetByteBuf.writeFloat(effectAndChance.getRight());
		}
	}
	
	public static PotionMod fromPacket(PacketByteBuf packetByteBuf) {
		PotionMod potionMod = new PotionMod();
		potionMod.flatDurationBonusTicks = packetByteBuf.readInt();
		potionMod.flatPotencyBonus = packetByteBuf.readFloat();
		potionMod.durationMultiplier = packetByteBuf.readFloat();
		potionMod.potencyMultiplier = packetByteBuf.readFloat();
		potionMod.flatPotencyBonusPositiveEffects = packetByteBuf.readFloat();
		potionMod.flatPotencyBonusNegativeEffects = packetByteBuf.readFloat();
		potionMod.flatDurationBonusPositiveEffects = packetByteBuf.readInt();
		potionMod.flatDurationBonusNegativeEffects = packetByteBuf.readInt();
		potionMod.additionalRandomPositiveEffectCount = packetByteBuf.readFloat();
		potionMod.additionalRandomNegativeEffectCount = packetByteBuf.readFloat();
		potionMod.chanceToAddLastEffect = packetByteBuf.readFloat();
		potionMod.lastEffectDurationMultiplier = packetByteBuf.readFloat();
		potionMod.lastEffectPotencyMultiplier = packetByteBuf.readFloat();
		potionMod.yield = packetByteBuf.readFloat();
		potionMod.makeSplashing = packetByteBuf.readBoolean();
		potionMod.makeLingering = packetByteBuf.readBoolean();
		potionMod.noParticles = packetByteBuf.readBoolean();
		potionMod.unidentifiable = packetByteBuf.readBoolean();
		potionMod.makeEffectsPositive = packetByteBuf.readBoolean();
		potionMod.potentDecreasingEffect = packetByteBuf.readBoolean();
		potionMod.negateDecreasingDuration = packetByteBuf.readBoolean();
		potionMod.additionalDrinkDurationTicks = packetByteBuf.readInt();
		potionMod.randomColor = packetByteBuf.readBoolean();
		
		int statusEffectCount = packetByteBuf.readInt();
		for (int i = 0; i < statusEffectCount; i++) {
			potionMod.additionalEffects.add(new Pair<>(PotionRecipeEffect.read(packetByteBuf), packetByteBuf.readFloat()));
		}
		
		return potionMod;
	}
	
	public void modifyFrom(PotionMod potionMod) {
		this.flatDurationBonusTicks += potionMod.flatDurationBonusTicks;
		this.flatPotencyBonus += potionMod.flatPotencyBonus;
		this.durationMultiplier += potionMod.durationMultiplier - 1;
		this.potencyMultiplier += potionMod.potencyMultiplier - 1;
		this.flatPotencyBonusPositiveEffects += potionMod.flatPotencyBonusPositiveEffects;
		this.flatPotencyBonusNegativeEffects += potionMod.flatPotencyBonusNegativeEffects;
		this.flatDurationBonusPositiveEffects += potionMod.flatDurationBonusPositiveEffects;
		this.flatDurationBonusNegativeEffects += potionMod.flatDurationBonusNegativeEffects;
		this.additionalRandomPositiveEffectCount += potionMod.additionalRandomPositiveEffectCount;
		this.additionalRandomNegativeEffectCount += potionMod.additionalRandomNegativeEffectCount;
		this.chanceToAddLastEffect += potionMod.chanceToAddLastEffect;
		this.lastEffectPotencyMultiplier += potionMod.lastEffectPotencyMultiplier - 1;
		this.lastEffectDurationMultiplier += potionMod.lastEffectDurationMultiplier - 1;
		this.yield += potionMod.yield;
		this.additionalDrinkDurationTicks += potionMod.additionalDrinkDurationTicks;
		this.makeSplashing |= potionMod.makeSplashing;
		this.makeLingering |= potionMod.makeLingering;
		this.noParticles |= potionMod.noParticles;
		this.unidentifiable |= potionMod.unidentifiable;
		this.makeEffectsPositive |= potionMod.makeEffectsPositive;
		this.potentDecreasingEffect |= potionMod.potentDecreasingEffect;
		this.negateDecreasingDuration |= potionMod.negateDecreasingDuration;
		this.randomColor |= potionMod.randomColor;
		this.additionalEffects.addAll(potionMod.additionalEffects);
	}
	
	public int getColor(Random random) {
		return this.randomColor ? java.awt.Color.getHSBColor(random.nextFloat(), 0.7F, 0.9F).getRGB() : this.unidentifiable ? 0x2f2f2f : -1; // dark gray
	}
	
}