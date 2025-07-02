package de.dafuqs.spectrum.items.trinkets;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.cca.azure_dike.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AetherGracedNectarGlovesItem extends AzureDikeTrinketItem implements SlotBackgroundEffectProvider {

	public static final int HARMFUL_EFFECT_COST = 7;
	public static ResourceLocation MENTAL_PRESENCE_ATTRIBUTE_ID = SpectrumCommon.locate("nectar_gloves_sleep");
	
	public AetherGracedNectarGlovesItem(Properties settings, ResourceLocation unlockIdentifier) {
		super(settings, unlockIdentifier);
	}

	@Override
	public int maxAzureDike(ItemStack stack) {
		return 10;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.aether_graced_nectar_gloves.tooltip"));
		tooltip.add(Component.translatable("item.spectrum.aether_graced_nectar_gloves.tooltip2"));
	}

	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, ResourceLocation slotIdentifier) {
		Multimap<Holder<Attribute>, AttributeModifier> modifiers = super.getModifiers(stack, slot, entity, slotIdentifier);
		modifiers.put(SpectrumEntityAttributes.MENTAL_PRESENCE, new AttributeModifier(MENTAL_PRESENCE_ATTRIBUTE_ID, -1F, AttributeModifier.Operation.ADD_VALUE));
		return modifiers;
	}
	
	public static boolean testEffectFor(LivingEntity entity, Holder<MobEffect> effect) {
		if (effect.value().isBeneficial())
			return false;
		
		if (effect.is(SpectrumStatusEffectTags.BYPASSES_NECTAR_GLOVES))
			return false;

		var trinkets = TrinketsApi.getTrinketComponent(entity);

		if (trinkets.isEmpty())
			return false;

		var component = trinkets.get();
		return component.isEquipped(SpectrumItems.AETHER_GRACED_NECTAR_GLOVES) && (effect.value().getCategory() == MobEffectCategory.HARMFUL || effect == SpectrumStatusEffects.FRENZY);
	}

	public static boolean tryBlockEffect(LivingEntity entity, int cost) {
		if (AzureDikeProvider.getAzureDikeCharges(entity) == 0)
			return false;

		return AzureDikeProvider.absorbDamage(entity, cost) == 0;
	}

	@Override
	public SlotEffect backgroundType(@Nullable Player player, ItemStack stack) {
		return SlotEffect.BORDER_FADE;
	}

	@Override
	public int getBackgroundColor(@Nullable Player player, ItemStack stack, float tickDelta) {
		return SpectrumStatusEffects.ETERNAL_SLUMBER_COLOR;
	}
}
