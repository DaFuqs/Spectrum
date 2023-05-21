package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.enchantment.*;
import net.minecraft.predicate.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.*;
import net.minecraft.util.*;
import net.minecraft.registry.*;

public class EnchantmentUpgradedCriterion extends AbstractCriterion<EnchantmentUpgradedCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("enchantment_upgraded");
	
	public static EnchantmentUpgradedCriterion.Conditions create(Enchantment enchantment, NumberRange.IntRange enchantmentLevelRange, NumberRange.IntRange experienceRange) {
		return new EnchantmentUpgradedCriterion.Conditions(EntityPredicate.Extended.EMPTY, enchantment, enchantmentLevelRange, experienceRange);
	}
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
	public EnchantmentUpgradedCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		Identifier identifier = new Identifier(JsonHelper.getString(jsonObject, "enchantment_identifier"));
		Enchantment enchantment = Registries.ENCHANTMENT.get(identifier);
		NumberRange.IntRange enchantmentLevelRange = NumberRange.IntRange.fromJson(jsonObject.get("enchantment_level"));
		NumberRange.IntRange experienceRange = NumberRange.IntRange.fromJson(jsonObject.get("spent_experience"));
		return new EnchantmentUpgradedCriterion.Conditions(extended, enchantment, enchantmentLevelRange, experienceRange);
	}
	
	public void trigger(ServerPlayerEntity player, Enchantment enchantment, int enchantmentLevel, int spentExperience) {
		this.trigger(player, (conditions) -> conditions.matches(enchantment, enchantmentLevel, spentExperience));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final Enchantment enchantment;
		private final NumberRange.IntRange enchantmentLevelRange;
		private final NumberRange.IntRange experienceRange;
		
		public Conditions(EntityPredicate.Extended player, Enchantment enchantment, NumberRange.IntRange enchantmentLevelRange, NumberRange.IntRange experienceRange) {
			super(ID, player);
			this.enchantment = enchantment;
			this.enchantmentLevelRange = enchantmentLevelRange;
			this.experienceRange = experienceRange;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.addProperty("enchantment_identifier", Registries.ENCHANTMENT.getId(enchantment).toString());
			jsonObject.add("enchantment_level", this.enchantmentLevelRange.toJson());
			jsonObject.add("spent_experience", this.experienceRange.toJson());
			return jsonObject;
		}
		
		public boolean matches(Enchantment enchantment, int enchantmentLevel, int spentExperience) {
			if (this.enchantment == null || this.enchantment.equals(enchantment)) {
				return this.enchantmentLevelRange.test(enchantmentLevel) && this.experienceRange.test(spentExperience);
			}
			return false;
		}
	}
	
}
