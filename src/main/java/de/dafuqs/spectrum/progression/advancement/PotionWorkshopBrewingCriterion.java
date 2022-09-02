package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityEffectPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PotionWorkshopBrewingCriterion extends AbstractCriterion<PotionWorkshopBrewingCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("potion_workshop_brewing");
	
	public static PotionWorkshopBrewingCriterion.Conditions create(ItemPredicate itemPredicate, EntityEffectPredicate effectsPredicate, NumberRange.IntRange maxAmplifierRange, NumberRange.IntRange maxDurationRange, NumberRange.IntRange effectCountRange, NumberRange.IntRange uniqueEffectCountRange) {
		return new PotionWorkshopBrewingCriterion.Conditions(EntityPredicate.Extended.EMPTY, itemPredicate, effectsPredicate, maxAmplifierRange, maxDurationRange, effectCountRange, uniqueEffectCountRange);
	}
	
	public Identifier getId() {
		return ID;
	}
	
	public PotionWorkshopBrewingCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		ItemPredicate itemPredicate = ItemPredicate.fromJson(jsonObject.get("item"));
		EntityEffectPredicate statusEffectsPredicate = EntityEffectPredicate.fromJson(jsonObject.get("effects"));
		NumberRange.IntRange maxAmplifierRange = NumberRange.IntRange.fromJson(jsonObject.get("max_amplifier"));
		NumberRange.IntRange maxDurationRange = NumberRange.IntRange.fromJson(jsonObject.get("max_duration"));
		NumberRange.IntRange effectCountRange = NumberRange.IntRange.fromJson(jsonObject.get("effect_count"));
		NumberRange.IntRange uniqueEffectCountRange = NumberRange.IntRange.fromJson(jsonObject.get("unique_effect_count"));
		return new PotionWorkshopBrewingCriterion.Conditions(extended, itemPredicate, statusEffectsPredicate, maxAmplifierRange, maxDurationRange, effectCountRange, uniqueEffectCountRange);
	}
	
	public void trigger(ServerPlayerEntity player, ItemStack itemStack) {
		this.trigger(player, conditions -> {
			// instanceof PotionItem is true for Potions, Splash Potions and Lingering Potions
			List<StatusEffectInstance> effects = PotionUtil.getPotionEffects(itemStack);
			int maxAmplifier = 0;
			int maxDuration = 0;
			for (StatusEffectInstance instance : effects) {
				if (instance.getAmplifier() > maxAmplifier) {
					maxAmplifier = instance.getAmplifier();
				}
				if (instance.getDuration() > maxDuration) {
					maxDuration = instance.getDuration();
				}
			}
			
			List<StatusEffect> uniqueEffects = new ArrayList<>();
			for(StatusEffectInstance instance : effects) {
				if(!uniqueEffects.contains(instance.getEffectType())) {
					uniqueEffects.add(instance.getEffectType());
				}
			}
			
			return conditions.matches(itemStack, effects, maxAmplifier, maxDuration, effects.size(), uniqueEffects.size());
		});
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate itemPredicate;
		private final EntityEffectPredicate statusEffectsPredicate;
		private final NumberRange.IntRange maxEffectAmplifierRange;
		private final NumberRange.IntRange maxEffectDurationRange;
		private final NumberRange.IntRange effectCountRange;
		private final NumberRange.IntRange uniqueEffectCountRange;
		
		public Conditions(EntityPredicate.Extended player, ItemPredicate itemPredicate, EntityEffectPredicate statusEffectsPredicate, NumberRange.IntRange maxEffectAmplifierRange, NumberRange.IntRange maxEffectDurationRange, NumberRange.IntRange effectCountRange, NumberRange.IntRange uniqueEffectCountRange) {
			super(ID, player);
			this.itemPredicate = itemPredicate;
			this.statusEffectsPredicate = statusEffectsPredicate;
			this.maxEffectAmplifierRange = maxEffectAmplifierRange;
			this.maxEffectDurationRange = maxEffectDurationRange;
			this.effectCountRange = effectCountRange;
			this.uniqueEffectCountRange = uniqueEffectCountRange;
		}
		
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("items", this.itemPredicate.toJson());
			jsonObject.add("effects", this.statusEffectsPredicate.toJson());
			jsonObject.add("max_amplifier", this.maxEffectAmplifierRange.toJson());
			jsonObject.add("max_duration", this.maxEffectDurationRange.toJson());
			jsonObject.add("effect_count", this.effectCountRange.toJson());
			jsonObject.add("unique_effect_count", this.uniqueEffectCountRange.toJson());
			return jsonObject;
		}
		
		public boolean matches(ItemStack stack, List<StatusEffectInstance> effects, int maxAmplifier, int maxDuration, int effectCount, int uniqueEffectCount) {
			if (this.maxEffectAmplifierRange.test(maxAmplifier) && this.maxEffectDurationRange.test(maxDuration) && this.effectCountRange.test(effectCount) && this.uniqueEffectCountRange.test(uniqueEffectCount)) {
				if (!this.itemPredicate.test(stack)) {
					return false;
				}
				
				Map<StatusEffect, StatusEffectInstance> effectMap = new HashMap<>();
				for (StatusEffectInstance instance : effects) {
					if (!effectMap.containsKey(instance)) {
						effectMap.put(instance.getEffectType(), instance);
					}
				}
				
				return this.statusEffectsPredicate.test(effectMap);
			}
			return false;
		}
	}
	
}
