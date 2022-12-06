package de.dafuqs.spectrum.helpers;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringHelper;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TooltipHelper {
	
	public static void addFoodComponentEffectTooltip(ItemStack stack, List<Text> tooltip) {
		FoodComponent foodComponent = stack.getItem().getFoodComponent();
		if (foodComponent != null) {
			buildEffectTooltipWithChance(tooltip, foodComponent.getStatusEffects(), Text.translatable("spectrum.food.whenEaten"));
		}
	}
	
	public static void buildEffectTooltipWithChance(List<Text> tooltip, List<Pair<StatusEffectInstance, Float>> effectsWithChance, MutableText attributeModifierText) {
		if (effectsWithChance.isEmpty()) {
			return;
		}
		
		List<Pair<EntityAttribute, EntityAttributeModifier>> modifiersList = Lists.newArrayList();
		MutableText translatableText;
		StatusEffect statusEffect;
		for (Iterator<Pair<StatusEffectInstance, Float>> var5 = effectsWithChance.iterator(); var5.hasNext(); tooltip.add(translatableText.formatted(statusEffect.getCategory().getFormatting()))) {
			Pair<StatusEffectInstance, Float> entry = var5.next();
			StatusEffectInstance statusEffectInstance = entry.getFirst();
			Float chance = entry.getSecond();
			
			translatableText = Text.translatable(statusEffectInstance.getTranslationKey());
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
				translatableText = Text.translatable("potion.withAmplifier", translatableText, Text.translatable("potion.potency." + statusEffectInstance.getAmplifier()));
			}
			if (statusEffectInstance.getDuration() > 20) {
				translatableText = Text.translatable("potion.withDuration", translatableText, StringHelper.formatTicks(statusEffectInstance.getDuration()));
			}
			if (chance < 1.0F) {
				translatableText = Text.translatable("spectrum.food.withChance", translatableText, Math.round(chance * 100));
			}
		}
		
		if (!modifiersList.isEmpty()) {
			tooltip.add(Text.empty());
			tooltip.add(attributeModifierText.formatted(Formatting.DARK_PURPLE));
			
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
					tooltip.add((Text.translatable("attribute.modifier.plus." + entityAttributeModifier3.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(e), Text.translatable((entityAttributeEntityAttributeModifierPair.getFirst()).getTranslationKey()))).formatted(Formatting.BLUE));
				} else if (d < 0.0D) {
					e *= -1.0D;
					tooltip.add((Text.translatable("attribute.modifier.take." + entityAttributeModifier3.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(e), Text.translatable((entityAttributeEntityAttributeModifierPair.getFirst()).getTranslationKey()))).formatted(Formatting.RED));
				}
			}
		}
	}
	
	public static void buildEffectTooltip(List<Text> tooltip, List<StatusEffectInstance> effects, MutableText attributeModifierText) {
		if (effects.size() > 0) {
			List<Pair<EntityAttribute, EntityAttributeModifier>> attributeModifiers = Lists.newArrayList();
			for (StatusEffectInstance effect : effects) {
				MutableText mutableText = Text.translatable(effect.getTranslationKey());
				
				if (effect.getAmplifier() > 0) {
					mutableText = Text.translatable("potion.withAmplifier", mutableText, Text.translatable("potion.potency." + effect.getAmplifier()));
				}
				tooltip.add(mutableText.formatted(effect.getEffectType().getCategory().getFormatting()));
				
				Map<EntityAttribute, EntityAttributeModifier> map = effect.getEffectType().getAttributeModifiers();
				for (Map.Entry<EntityAttribute, EntityAttributeModifier> entityAttributeEntityAttributeModifierEntry : map.entrySet()) {
					EntityAttributeModifier entityAttributeModifier = entityAttributeEntityAttributeModifierEntry.getValue();
					EntityAttributeModifier entityAttributeModifier2 = new EntityAttributeModifier(entityAttributeModifier.getName(), effect.getEffectType().adjustModifierAmount(effect.getAmplifier(), entityAttributeModifier), entityAttributeModifier.getOperation());
					attributeModifiers.add(new Pair<>(entityAttributeEntityAttributeModifierEntry.getKey(), entityAttributeModifier2));
				}
			}
			
			if (!attributeModifiers.isEmpty()) {
				tooltip.add(Text.empty());
				tooltip.add(attributeModifierText.formatted(Formatting.DARK_PURPLE));
				
				for (Pair<EntityAttribute, EntityAttributeModifier> entityAttributeEntityAttributeModifierPair : attributeModifiers) {
					EntityAttributeModifier mutableText = entityAttributeEntityAttributeModifierPair.getSecond();
					double statusEffect = mutableText.getValue();
					double d;
					if (mutableText.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_BASE && mutableText.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_TOTAL) {
						d = mutableText.getValue();
					} else {
						d = mutableText.getValue() * 100.0D;
					}
					
					if (statusEffect > 0.0D) {
						tooltip.add((Text.translatable("attribute.modifier.plus." + mutableText.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(d), Text.translatable((entityAttributeEntityAttributeModifierPair.getFirst()).getTranslationKey()))).formatted(Formatting.BLUE));
					} else if (statusEffect < 0.0D) {
						d *= -1.0D;
						tooltip.add((Text.translatable("attribute.modifier.take." + mutableText.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(d), Text.translatable((entityAttributeEntityAttributeModifierPair.getFirst()).getTranslationKey()))).formatted(Formatting.RED));
					}
				}
			}
		}
	}
	
}
