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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PotionWorkshopBrewingCriterion extends AbstractCriterion<PotionWorkshopBrewingCriterion.Conditions> {
	
	static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "potion_workshop_brewing");
	
	public static PotionWorkshopBrewingCriterion.Conditions create(ItemPredicate itemPredicate, EntityEffectPredicate effectsPredicate, NumberRange.IntRange maxAmplifierRange, NumberRange.IntRange maxDurationRange, NumberRange.IntRange effectCountRange) {
		return new PotionWorkshopBrewingCriterion.Conditions(EntityPredicate.Extended.EMPTY, itemPredicate, effectsPredicate, maxAmplifierRange, maxDurationRange, effectCountRange);
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
		return new PotionWorkshopBrewingCriterion.Conditions(extended, itemPredicate, statusEffectsPredicate, maxAmplifierRange, maxDurationRange, effectCountRange);
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
			
			return conditions.matches(itemStack, effects, maxAmplifier, maxDuration, effects.size());
		});
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate itemPredicate;
		private final EntityEffectPredicate statusEffectsPredicate;
		private final NumberRange.IntRange maxEffectAmplifierRange;
		private final NumberRange.IntRange maxEffectDurationRange;
		private final NumberRange.IntRange effectCountRange;
		
		public Conditions(EntityPredicate.Extended player, ItemPredicate itemPredicate, EntityEffectPredicate statusEffectsPredicate, NumberRange.IntRange maxEffectAmplifierRange, NumberRange.IntRange maxEffectDurationRange, NumberRange.IntRange effectCountRange) {
			super(ID, player);
			this.itemPredicate = itemPredicate;
			this.statusEffectsPredicate = statusEffectsPredicate;
			this.maxEffectAmplifierRange = maxEffectAmplifierRange;
			this.maxEffectDurationRange = maxEffectDurationRange;
			this.effectCountRange = effectCountRange;
		}
		
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("items", this.itemPredicate.toJson());
			jsonObject.add("effects", this.statusEffectsPredicate.toJson());
			jsonObject.add("max_amplifier", this.maxEffectAmplifierRange.toJson());
			jsonObject.add("max_duration", this.maxEffectDurationRange.toJson());
			jsonObject.add("effect_count", this.effectCountRange.toJson());
			return jsonObject;
		}
		
		public boolean matches(ItemStack stack, List<StatusEffectInstance> effects, int maxAmplifier, int maxDuration, int effectCount) {
			if (this.maxEffectAmplifierRange.test(maxAmplifier) && this.maxEffectDurationRange.test(maxDuration) && this.effectCountRange.test(effectCount)) {
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
