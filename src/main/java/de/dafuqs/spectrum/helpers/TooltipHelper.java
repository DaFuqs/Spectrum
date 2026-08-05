package de.dafuqs.spectrum.helpers;

import com.google.common.collect.*;
import com.mojang.datafixers.util.*;
import net.minecraft.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.food.*;
import net.minecraft.world.inventory.tooltip.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;

import java.util.*;

public class TooltipHelper {
	
	public static void addFoodComponentEffectTooltip(Minecraft minecraft, ItemStack stack, List<Either<FormattedText, TooltipComponent>> tooltip, float updateTickRate) {
		FoodProperties foodComponent = stack.get(DataComponents.FOOD);
		if (foodComponent != null) {
			buildEffectTooltipWithChance(minecraft, tooltip, foodComponent.effects(), stack.getUseAnimation() == UseAnim.DRINK ? Component.translatable("spectrum.food.whenDrunk") : Component.translatable("spectrum.food.whenEaten"), updateTickRate);
		}
	}
	
	public static void buildEffectTooltipWithChance(Minecraft minecraft, List<Either<FormattedText, TooltipComponent>> tooltip, List<FoodProperties.PossibleEffect> entries, MutableComponent attributeModifierText, float updateTickRate) {
		if (entries.isEmpty()) {
			return;
		}
		
		List<Pair<Attribute, AttributeModifier>> modifiersList = Lists.newArrayList();
		for (FoodProperties.PossibleEffect entry : entries) {
			MobEffectInstance statusEffectInstance = entry.effect();
			float chance = entry.probability();
			MutableComponent translatableText = Component.translatable(statusEffectInstance.getDescriptionId());
			Holder<MobEffect> statusEffect = statusEffectInstance.getEffect();
			
			statusEffect.value().createModifiers(statusEffectInstance.getAmplifier(), (attribute, modifier) ->
					modifiersList.add(new Pair<>(attribute.value(), modifier)));
			
			if (statusEffectInstance.getAmplifier() > 0) {
				translatableText = Component.translatable("potion.withAmplifier", translatableText, Component.translatable("potion.potency." + statusEffectInstance.getAmplifier()));
			}
			if (statusEffectInstance.getDuration() > 20) {
				translatableText = Component.translatable("potion.withDuration", translatableText, MobEffectUtil.formatDuration(entry.effect(), 1.0F, minecraft.level.tickRateManager().tickrate()));
			}
			if (chance < 1.0F) {
				translatableText = Component.translatable("spectrum.food.withChance", translatableText, Math.round(chance * 100));
			}
			
			tooltip.add(Either.left(translatableText.withStyle(statusEffect.value().getCategory().getTooltipFormatting())));
		}
		
		if (!modifiersList.isEmpty()) {
			tooltip.add(Either.left(Component.empty()));
			tooltip.add(Either.left(attributeModifierText.withStyle(ChatFormatting.DARK_PURPLE)));
			
			for (var pair : modifiersList) {
				var modifier = pair.getSecond();
				double d = modifier.amount();
				double e;
				if (modifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_BASE && modifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
					e = modifier.amount();
				} else {
					e = modifier.amount() * 100.0D;
				}
				
				if (d > 0.0D) {
					tooltip.add(Either.left((Component.translatable("attribute.modifier.plus." + modifier.operation().id(), ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(e), Component.translatable((pair.getFirst()).getDescriptionId()))).withStyle(ChatFormatting.BLUE)));
				} else if (d < 0.0D) {
					e *= -1.0D;
					tooltip.add(Either.left((Component.translatable("attribute.modifier.take." + modifier.operation().id(), ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(e), Component.translatable((pair.getFirst()).getDescriptionId()))).withStyle(ChatFormatting.RED)));
				}
			}
		}
	}
	
}
