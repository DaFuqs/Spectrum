package de.dafuqs.spectrum.helpers;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringHelper;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TooltipHelper {
	
	public static void addFoodComponentEffectTooltip(ItemStack stack, List<Text> tooltip) {
		FoodComponent foodComponent = stack.getItem().getFoodComponent();
		if(foodComponent != null) {
			buildFoodEffectTooltip(tooltip, foodComponent.getStatusEffects());
		}
	}
	
	public static void buildFoodEffectTooltip(List<Text> tooltip, List<Pair<StatusEffectInstance, Float>> effectsWithChance) {
		if(effectsWithChance.isEmpty()) {
			return;
		}
		
		List<Pair<EntityAttribute, EntityAttributeModifier>> modifiersList = Lists.newArrayList();
		TranslatableText translatableText;
		StatusEffect statusEffect;
		for(Iterator<Pair<StatusEffectInstance, Float>> var5 = effectsWithChance.iterator(); var5.hasNext(); tooltip.add(translatableText.formatted(statusEffect.getCategory().getFormatting()))) {
			Pair<StatusEffectInstance, Float> entry = var5.next();
			StatusEffectInstance statusEffectInstance = entry.getFirst();
			Float chance = entry.getSecond();
			
			translatableText = new TranslatableText(statusEffectInstance.getTranslationKey());
			statusEffect = statusEffectInstance.getEffectType();
			Map<EntityAttribute, EntityAttributeModifier> map = statusEffect.getAttributeModifiers();
			if (!map.isEmpty()) {
				for (Map.Entry<EntityAttribute, EntityAttributeModifier> entityAttributeEntityAttributeModifierEntry : map.entrySet()) {
					EntityAttributeModifier entityAttributeModifier = entityAttributeEntityAttributeModifierEntry.getValue();
					EntityAttributeModifier entityAttributeModifier2 = new EntityAttributeModifier(entityAttributeModifier.getName(), statusEffect.adjustModifierAmount(statusEffectInstance.getAmplifier(), entityAttributeModifier), entityAttributeModifier.getOperation());
					modifiersList.add(new Pair<>(entityAttributeEntityAttributeModifierEntry.getKey(), entityAttributeModifier2));
				}
			}
			
			if (statusEffectInstance.getAmplifier() > 0) {
				translatableText = new TranslatableText("potion.withAmplifier", translatableText, new TranslatableText("potion.potency." + statusEffectInstance.getAmplifier()));
			}
			if (statusEffectInstance.getDuration() > 20) {
				translatableText = new TranslatableText("potion.withDuration", translatableText, StringHelper.formatTicks(statusEffectInstance.getDuration()));
			}
			if(chance < 1.0F) {
				translatableText = new TranslatableText("spectrum.food.withChance", translatableText, Math.round(chance * 100));
			}
		}
		
		if (!modifiersList.isEmpty()) {
			tooltip.add(LiteralText.EMPTY);
			tooltip.add((new TranslatableText("spectrum.food.whenEaten")).formatted(Formatting.DARK_PURPLE));
			
			for (Pair<EntityAttribute, EntityAttributeModifier> entityAttributeEntityAttributeModifierPair : modifiersList) {
				EntityAttributeModifier entityAttributeModifier3 = entityAttributeEntityAttributeModifierPair.getSecond();
				double d = entityAttributeModifier3.getValue();
				double e;
				if (entityAttributeModifier3.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_BASE && entityAttributeModifier3.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_TOTAL) {
					e = entityAttributeModifier3.getValue();
				} else {
					e = entityAttributeModifier3.getValue() * 100.0D;
				}
				
				if (d > 0.0D) {
					tooltip.add((new TranslatableText("attribute.modifier.plus." + entityAttributeModifier3.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(e), new TranslatableText((entityAttributeEntityAttributeModifierPair.getFirst()).getTranslationKey()))).formatted(Formatting.BLUE));
				} else if (d < 0.0D) {
					e *= -1.0D;
					tooltip.add((new TranslatableText("attribute.modifier.take." + entityAttributeModifier3.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(e), new TranslatableText((entityAttributeEntityAttributeModifierPair.getFirst()).getTranslationKey()))).formatted(Formatting.RED));
				}
			}
		}
	}
	
}
