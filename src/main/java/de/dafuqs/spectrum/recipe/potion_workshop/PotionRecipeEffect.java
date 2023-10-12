package de.dafuqs.spectrum.recipe.potion_workshop;

import com.google.gson.*;
import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.*;
import net.minecraft.entity.effect.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.registry.*;
import org.jetbrains.annotations.*;

public record PotionRecipeEffect(boolean applicableToPotions, boolean applicableToTippedArrows,
								 boolean applicableToPotionFillabes, boolean applicableToWeapons,
								 int baseDurationTicks, float potencyModifier, StatusEffect statusEffect,
								 InkColor inkColor, int inkCost) {
	
	public static PotionRecipeEffect read(JsonObject jsonObject) {
		boolean applicableToPotions = JsonHelper.getBoolean(jsonObject, "applicable_to_potions", true);
		boolean applicableToTippedArrows = JsonHelper.getBoolean(jsonObject, "applicable_to_tipped_arrows", true);
		boolean applicableToPotionFillabes = JsonHelper.getBoolean(jsonObject, "applicable_to_potion_fillables", true);
		boolean applicableToWeapons = JsonHelper.getBoolean(jsonObject, "applicable_to_potion_weapons", true);
		
		int baseDurationTicks = JsonHelper.getInt(jsonObject, "base_duration_ticks", 1600);
		float potencyModifier = JsonHelper.getFloat(jsonObject, "potency_modifier", 1.0F);
		
		Identifier statusEffectIdentifier = Identifier.tryParse(JsonHelper.getString(jsonObject, "effect"));
		if (!Registry.STATUS_EFFECT.containsId(statusEffectIdentifier)) {
			throw new JsonParseException("Potion Workshop Recipe has a status effect set that does not exist or is disabled: " + statusEffectIdentifier); // otherwise, recipe sync would break multiplayer joining with the non-existing status effect
		}
		StatusEffect statusEffect = Registry.STATUS_EFFECT.get(statusEffectIdentifier);
		
		InkColor inkColor = InkColor.of(JsonHelper.getString(jsonObject, "ink_color"));
		int inkCost = JsonHelper.getInt(jsonObject, "ink_cost");
		
		return new PotionRecipeEffect(applicableToPotions, applicableToTippedArrows, applicableToPotionFillabes, applicableToWeapons, baseDurationTicks, potencyModifier, statusEffect, inkColor, inkCost);
	}
	
	public void write(PacketByteBuf packetByteBuf) {
		packetByteBuf.writeIdentifier(Registry.STATUS_EFFECT.getId(statusEffect));
		packetByteBuf.writeInt(baseDurationTicks);
		packetByteBuf.writeFloat(potencyModifier);
		packetByteBuf.writeBoolean(applicableToPotions);
		packetByteBuf.writeBoolean(applicableToTippedArrows);
		packetByteBuf.writeBoolean(applicableToPotionFillabes);
		packetByteBuf.writeBoolean(applicableToWeapons);
		packetByteBuf.writeString(inkColor.toString());
		packetByteBuf.writeInt(inkCost);
	}
	
	public static PotionRecipeEffect read(PacketByteBuf packetByteBuf) {
		StatusEffect statusEffect = Registry.STATUS_EFFECT.get(packetByteBuf.readIdentifier());
		int baseDurationTicks = packetByteBuf.readInt();
		float potencyModifier = packetByteBuf.readFloat();
		boolean applicableToPotions = packetByteBuf.readBoolean();
		boolean applicableToTippedArrows = packetByteBuf.readBoolean();
		boolean applicableToPotionFillabes = packetByteBuf.readBoolean();
		boolean applicableToWeapons = packetByteBuf.readBoolean();
		InkColor inkColor = InkColor.of(packetByteBuf.readString());
		int inkCost = packetByteBuf.readInt();
		
		return new PotionRecipeEffect(applicableToPotions, applicableToTippedArrows, applicableToPotionFillabes, applicableToWeapons, baseDurationTicks, potencyModifier, statusEffect, inkColor, inkCost);
	}
	
	public @Nullable InkPoweredStatusEffectInstance getStatusEffectInstance(@NotNull PotionMod potionMod, Random random) {
		float potency = potionMod.flatPotencyBonus;
		int durationTicks = baseDurationTicks() + potionMod.flatDurationBonusTicks;
		switch (statusEffect().getCategory()) {
			case BENEFICIAL -> {
				potency += potionMod.flatPotencyBonusPositiveEffects;
				durationTicks += potionMod.flatDurationBonusPositiveEffects;
			}
			case HARMFUL -> {
				potency += potionMod.flatPotencyBonusNegativeEffects;
				durationTicks += potionMod.flatDurationBonusNegativeEffects;
			}
			default -> {
			}
		}
		durationTicks = statusEffect().isInstant() ? 1 : (int) (durationTicks * potionMod.durationMultiplier);
		
		if (potencyModifier() == 0.0F) {
			potency = 0; // effects that only have 1 level, like night vision
		} else {
			potency = (((1 + potency) * potionMod.potencyMultiplier) - 1) * potencyModifier();
			potency = Support.getIntFromDecimalWithChance(potency, random);
			
			// if the result of the potency calculation was negative because of a very low recipe base potencyModifier
			// (not because the player was greedy and got mali because of low multiplicativePotencyModifier)
			// => set to 0 again
			if (potency < 0 && potionMod.potencyMultiplier == 0.0) {
				potency = 0;
			}
		}
		
		if (potency >= 0 && durationTicks > 0) {
			int effectColor = potionMod.getColor(random);
			return new InkPoweredStatusEffectInstance(new StatusEffectInstance(statusEffect(), durationTicks, (int) potency, !potionMod.noParticles, !potionMod.noParticles), new InkCost(inkColor(), inkCost()), effectColor, potionMod.unidentifiable);
		} else {
			// the effect is so borked that the effect would be too weak
			return null;
		}
	}
	
	public boolean isApplicableTo(ItemStack baseIngredient, PotionMod potionMod) {
		if (baseIngredient.isOf(Items.ARROW)) { // arrows require lingering potions as base
			return applicableToTippedArrows && potionMod.makeSplashing && potionMod.makeLingering;
		} else if (baseIngredient.getItem() instanceof InkPoweredPotionFillable inkPoweredPotionFillable) {
			return applicableToPotionFillabes && !inkPoweredPotionFillable.isFull(baseIngredient) || applicableToWeapons && inkPoweredPotionFillable.isWeapon();
		} else {
			return applicableToPotions;
		}
	}
	
}
