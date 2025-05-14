package de.dafuqs.spectrum.recipe.potion_workshop;

import com.google.gson.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

// TODO - Refactor
public record PotionRecipeEffect(
		boolean applicableToPotions,
		boolean applicableToTippedArrows,
		boolean applicableToPotionFillabes,
		boolean applicableToWeapons,
		int baseDurationTicks,
		float baseYield,
		int potencyHardCap,
		float potencyModifier,
		Holder<MobEffect> statusEffect,
		InkColor inkColor,
		int inkCost
) {
	public static MapCodec<PotionRecipeEffect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.BOOL.optionalFieldOf("applicable_to_potions", true).forGetter(PotionRecipeEffect::applicableToPotions),
			Codec.BOOL.optionalFieldOf("applicable_to_tipped_arrows", true).forGetter(PotionRecipeEffect::applicableToTippedArrows),
			Codec.BOOL.optionalFieldOf("applicable_to_potion_fillabes", true).forGetter(PotionRecipeEffect::applicableToPotionFillabes),
			Codec.BOOL.optionalFieldOf("applicable_to_weapons", true).forGetter(PotionRecipeEffect::applicableToWeapons),
			Codec.INT.optionalFieldOf("base_duration_ticks", 1600).forGetter(PotionRecipeEffect::baseDurationTicks),
			Codec.FLOAT.optionalFieldOf("base_yield", (float) PotionWorkshopBrewingRecipe.BASE_POTION_COUNT_ON_BREWING).forGetter(PotionRecipeEffect::baseYield),
			Codec.INT.optionalFieldOf("potency_hard_cap", -1).forGetter(PotionRecipeEffect::potencyHardCap),
			Codec.FLOAT.optionalFieldOf("potency_modifier", 1f).forGetter(PotionRecipeEffect::potencyModifier),
			BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("effect").forGetter(PotionRecipeEffect::statusEffect),
			InkColor.CODEC.fieldOf("ink_color").forGetter(PotionRecipeEffect::inkColor),
			Codec.INT.fieldOf("ink_cost").forGetter(PotionRecipeEffect::inkCost)
	).apply(i, PotionRecipeEffect::new));
	
	public static StreamCodec<RegistryFriendlyByteBuf, PotionRecipeEffect> PACKET_CODEC = PacketCodecHelper.tuple(
			ByteBufCodecs.BOOL, PotionRecipeEffect::applicableToPotions,
			ByteBufCodecs.BOOL, PotionRecipeEffect::applicableToTippedArrows,
			ByteBufCodecs.BOOL, PotionRecipeEffect::applicableToPotionFillabes,
			ByteBufCodecs.BOOL, PotionRecipeEffect::applicableToWeapons,
			ByteBufCodecs.VAR_INT, PotionRecipeEffect::baseDurationTicks,
			ByteBufCodecs.FLOAT, PotionRecipeEffect::baseYield,
			ByteBufCodecs.VAR_INT, PotionRecipeEffect::potencyHardCap,
			ByteBufCodecs.FLOAT, PotionRecipeEffect::potencyModifier,
			ByteBufCodecs.holderRegistry(Registries.MOB_EFFECT), PotionRecipeEffect::statusEffect,
			InkColor.PACKET_CODEC, PotionRecipeEffect::inkColor,
			ByteBufCodecs.VAR_INT, PotionRecipeEffect::inkCost,
			PotionRecipeEffect::new
	);
	
	public static PotionRecipeEffect read(JsonObject jsonObject) {
		return CodecHelper.fromJson(CODEC.codec(), jsonObject).orElseThrow();
	}
	
	public void write(RegistryFriendlyByteBuf buf) {
		PACKET_CODEC.encode(buf, this);
	}
	
	public static PotionRecipeEffect read(RegistryFriendlyByteBuf buf) {
		return PACKET_CODEC.decode(buf);
	}
	
	public @Nullable InkPoweredStatusEffectInstance getStatusEffectInstance(@NotNull PotionMod potionMod, RandomSource random) {
		float potency = potionMod.flatPotencyBonus();
		int durationTicks = baseDurationTicks() + potionMod.flatDurationBonusTicks();
		switch (statusEffect().value().getCategory()) {
			case BENEFICIAL -> {
				potency += potionMod.flatPotencyBonusPositiveEffects();
				durationTicks += potionMod.flatDurationBonusPositiveEffects();
			}
			case HARMFUL -> {
				potency += potionMod.flatPotencyBonusNegativeEffects();
				durationTicks += potionMod.flatDurationBonusNegativeEffects();
			}
			default -> {
			}
		}
		durationTicks = statusEffect().value().isInstantenous() ? 1 : (int) (durationTicks * potionMod.durationMultiplier());
		
		if (potencyModifier() == 0.0F) {
			potency = 0; // effects that only have 1 level, like night vision
		} else {
			potency = (((1 + potency) * potionMod.potencyMultiplier()) - 1) * potencyModifier();
			potency = Support.getIntFromDecimalWithChance(potency, random);
			
			// if the result of the potency calculation was negative because of a very low recipe base potencyModifier
			// (not because the player was greedy and got mali because of low multiplicativePotencyModifier)
			// => set to 0 again
			if (potency < 0 && potionMod.potencyMultiplier() == 0.0) {
				potency = 0;
			}
		}
		
		// Prevents some status effects from getting out of hand.
		// While strong potions are always fun, there are things the player should not be able to make,
		// such as resistance 5 which would grant invulnerability.
		if (potencyHardCap > -1 && potency > potencyHardCap) {
			potency = potencyHardCap;
		}
		
		if (potency >= 0 && durationTicks > 0) {
			int effectColor = potionMod.getColor(random);
			return new InkPoweredStatusEffectInstance(new MobEffectInstance(statusEffect, durationTicks, (int) potency, !potionMod.flags().noParticles(), !potionMod.flags().noParticles()), new InkCost(inkColor(), inkCost()), effectColor, potionMod.flags().unidentifiable(), potionMod.flags().incurable());
		} else {
			// the effect is so borked that the effect would be too weak
			return null;
		}
	}
	
	public boolean isApplicableTo(ItemStack baseIngredient, PotionMod potionMod) {
		if (baseIngredient.is(Items.ARROW)) { // arrows require lingering potions as base
			return applicableToTippedArrows && potionMod.flags().makeSplashing() && potionMod.flags().makeLingering();
		} else if (baseIngredient.getItem() instanceof InkPoweredPotionFillable inkPoweredPotionFillable) {
			return applicableToPotionFillabes && !inkPoweredPotionFillable.isFull(baseIngredient) || applicableToWeapons && inkPoweredPotionFillable.isWeapon();
		} else {
			return applicableToPotions;
		}
	}
	
}
