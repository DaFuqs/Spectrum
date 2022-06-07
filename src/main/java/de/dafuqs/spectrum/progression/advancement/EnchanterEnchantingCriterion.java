package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class EnchanterEnchantingCriterion extends AbstractCriterion<EnchanterEnchantingCriterion.Conditions> {
	
	static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "enchanter_enchanting");
	
	public static EnchanterEnchantingCriterion.Conditions create(ItemPredicate itemPredicate, NumberRange.IntRange experienceRange) {
		return new EnchanterEnchantingCriterion.Conditions(EntityPredicate.Extended.EMPTY, itemPredicate, experienceRange);
	}
	
	public Identifier getId() {
		return ID;
	}
	
	public EnchanterEnchantingCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		ItemPredicate itemPredicate = ItemPredicate.fromJson(jsonObject.get("item"));
		NumberRange.IntRange experienceRange = NumberRange.IntRange.fromJson(jsonObject.get("spent_experience"));
		return new EnchanterEnchantingCriterion.Conditions(extended, itemPredicate, experienceRange);
	}
	
	public void trigger(ServerPlayerEntity player, ItemStack itemStack, int spentExperience) {
		this.trigger(player, (conditions) -> {
			return conditions.matches(itemStack, spentExperience);
		});
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate itemPredicate;
		private final NumberRange.IntRange experienceRange;
		
		public Conditions(EntityPredicate.Extended player, ItemPredicate itemPredicate, NumberRange.IntRange experienceRange) {
			super(ID, player);
			this.itemPredicate = itemPredicate;
			this.experienceRange = experienceRange;
		}
		
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("item", this.itemPredicate.toJson());
			jsonObject.add("spent_experience", this.experienceRange.toJson());
			return jsonObject;
		}
		
		public boolean matches(ItemStack stack, int spentExperience) {
			if (!this.itemPredicate.test(stack)) {
				return false;
			} else {
				return this.experienceRange.test(spentExperience);
			}
		}
	}
	
}
