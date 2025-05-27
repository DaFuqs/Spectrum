package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.cca.azure_dike.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.*;
import net.minecraft.registry.entry.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AetherGracedNectarGlovesItem extends AzureDikeTrinketItem implements SlotBackgroundEffectProvider {

	public static final int HARMFUL_EFFECT_COST = 7;
	public static Identifier MENTAL_PRESENCE_ATTRIBUTE_ID = SpectrumCommon.locate("nectar_gloves_sleep");
	
	public AetherGracedNectarGlovesItem(Settings settings, Identifier unlockIdentifier) {
		super(settings, unlockIdentifier);
	}

	@Override
	public int maxAzureDike(ItemStack stack) {
		return 10;
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		super.appendTooltip(stack, context, tooltip, type);
		tooltip.add(Text.translatable("item.spectrum.aether_graced_nectar_gloves.tooltip"));
		tooltip.add(Text.translatable("item.spectrum.aether_graced_nectar_gloves.tooltip2"));
	}

	@Override
	public Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, Identifier slotIdentifier) {
		Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> modifiers = super.getModifiers(stack, slot, entity, slotIdentifier);
		modifiers.put(SpectrumEntityAttributes.MENTAL_PRESENCE, new EntityAttributeModifier(MENTAL_PRESENCE_ATTRIBUTE_ID, -1F, EntityAttributeModifier.Operation.ADD_VALUE));
		return modifiers;
	}
	
	public static boolean testEffectFor(LivingEntity entity, RegistryEntry<StatusEffect> effect) {
		if (effect.value().isBeneficial())
			return false;
		
		if (effect.isIn(SpectrumStatusEffectTags.BYPASSES_NECTAR_GLOVES))
			return false;

		var trinkets = TrinketsApi.getTrinketComponent(entity);

		if (trinkets.isEmpty())
			return false;

		var component = trinkets.get();
		return component.isEquipped(SpectrumItems.AETHER_GRACED_NECTAR_GLOVES) && (effect.value().getCategory() == StatusEffectCategory.HARMFUL || effect == SpectrumStatusEffects.FRENZY);
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
