package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class EnchantmentUpgradedCriterion extends AbstractCriterion<EnchantmentUpgradedCriterion.Conditions> {
	
	static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "enchantment_upgraded");
	
	public static EnchantmentUpgradedCriterion.Conditions create(Enchantment enchantment, NumberRange.IntRange enchantmentLevelRange, NumberRange.IntRange experienceRange) {
		return new EnchantmentUpgradedCriterion.Conditions(EntityPredicate.Extended.EMPTY, enchantment, enchantmentLevelRange, experienceRange);
	}
	
	public Identifier getId() {
		return ID;
	}
	
	public EnchantmentUpgradedCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		Identifier identifier = new Identifier(JsonHelper.getString(jsonObject, "enchantment_identifier"));
		Enchantment enchantment = Registry.ENCHANTMENT.get(identifier);
		NumberRange.IntRange enchantmentLevelRange = NumberRange.IntRange.fromJson(jsonObject.get("enchantment_level"));
		NumberRange.IntRange experienceRange = NumberRange.IntRange.fromJson(jsonObject.get("spent_experience"));
		return new EnchantmentUpgradedCriterion.Conditions(extended, enchantment, enchantmentLevelRange, experienceRange);
	}
	
	public void trigger(ServerPlayerEntity player, Enchantment enchantment, int enchantmentLevel, int spentExperience) {
		this.trigger(player, (conditions) -> {
			return conditions.matches(enchantment, enchantmentLevel, spentExperience);
		});
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
		
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.addProperty("enchantment_identifier", Registry.ENCHANTMENT.getId(enchantment).toString());
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
