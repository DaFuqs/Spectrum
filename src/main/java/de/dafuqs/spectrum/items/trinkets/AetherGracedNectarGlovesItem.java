package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.cca.azure_dike.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AetherGracedNectarGlovesItem extends AzureDikeTrinketItem implements SlotBackgroundEffectProvider {

	public static final int HARMFUL_EFFECT_COST = 3;

	public AetherGracedNectarGlovesItem(Settings settings) {
		super(settings);
	}

	@Override
	public int maxAzureDike(ItemStack stack) {
		return 10;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.aether_graced_nectar_gloves.tooltip"));
		tooltip.add(Text.translatable("item.spectrum.aether_graced_nectar_gloves.tooltip2"));
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
		Multimap<EntityAttribute, EntityAttributeModifier> modifiers = super.getModifiers(stack, slot, entity, uuid);
		modifiers.put(SpectrumEntityAttributes.MENTAL_PRESENCE, new EntityAttributeModifier(uuid, "spectrum:nectar_gloves_sleep", -1, EntityAttributeModifier.Operation.ADDITION));
		return modifiers;
	}

	public static boolean testEffectFor(LivingEntity entity, StatusEffect effect) {
		if (effect.isBeneficial())
			return false;

		if (SpectrumStatusEffectTags.isIn(SpectrumStatusEffectTags.SOPORIFIC, effect))
			return false;

		var trinkets = TrinketsApi.getTrinketComponent(entity);

		if (trinkets.isEmpty())
			return false;

		var component = trinkets.get();
		return component.isEquipped(SpectrumItems.AETHER_GRACED_NECTAR_GLOVES) && (effect.getCategory() == StatusEffectCategory.HARMFUL || effect == SpectrumStatusEffects.FRENZY);
	}

	public static boolean tryBlockEffect(LivingEntity entity, int cost) {
		if (AzureDikeProvider.getAzureDikeCharges(entity) == 0)
			return false;

		return AzureDikeProvider.absorbDamage(entity, cost) == 0;
	}

	@Override
	public SlotEffect backgroundType(@Nullable PlayerEntity player, ItemStack stack) {
		return SlotEffect.BORDER_FADE;
	}

	@Override
	public int getBackgroundColor(@Nullable PlayerEntity player, ItemStack stack, float tickDelta) {
		return SpectrumStatusEffects.ETERNAL_SLUMBER_COLOR;
	}
}
