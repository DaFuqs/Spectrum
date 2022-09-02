package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
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

import java.util.List;

public class TitrationBarrelTappingCriterion extends AbstractCriterion<TitrationBarrelTappingCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("titration_barrel_tapping");
	
	public static TitrationBarrelTappingCriterion.Conditions create(ItemPredicate[] item, NumberRange.IntRange ingameDaysAgeRange, NumberRange.IntRange ingredientCountRange) {
		return new TitrationBarrelTappingCriterion.Conditions(EntityPredicate.Extended.EMPTY, item, ingameDaysAgeRange, ingredientCountRange);
	}
	
	public Identifier getId() {
		return ID;
	}
	
	public TitrationBarrelTappingCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		ItemPredicate[] tappedItemPredicates = ItemPredicate.deserializeAll(jsonObject.get("items"));
		NumberRange.IntRange ingameDaysAgeRange = NumberRange.IntRange.fromJson(jsonObject.get("age_ingame_days"));
		NumberRange.IntRange ingredientCountRange = NumberRange.IntRange.fromJson(jsonObject.get("ingredient_count"));
		return new TitrationBarrelTappingCriterion.Conditions(extended, tappedItemPredicates, ingameDaysAgeRange, ingredientCountRange);
	}
	
	public void trigger(ServerPlayerEntity player, ItemStack itemStack, int ingameDaysAge, int ingredientCount) {
		this.trigger(player, (conditions) -> {
			return conditions.matches(itemStack, ingameDaysAge, ingredientCount);
		});
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate[] tappedItemPredicates;
		private final NumberRange.IntRange ingameDaysAgeRange;
		private final NumberRange.IntRange ingredientCountRange;
		
		public Conditions(EntityPredicate.Extended player, ItemPredicate[] tappedItemPredicates, NumberRange.IntRange ingameDaysAgeRange, NumberRange.IntRange ingredientCountRange) {
			super(ID, player);
			this.tappedItemPredicates = tappedItemPredicates;
			this.ingameDaysAgeRange = ingameDaysAgeRange;
			this.ingredientCountRange = ingredientCountRange;
		}
		
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.addProperty("items", this.tappedItemPredicates.toString());
			jsonObject.add("age_ingame_days", this.ingameDaysAgeRange.toJson());
			jsonObject.add("ingredient_count", this.ingredientCountRange.toJson());
			return jsonObject;
		}
		
		public boolean matches(ItemStack itemStack, int experience, int ingredientCount) {
			if (this.ingameDaysAgeRange.test(experience) && this.ingredientCountRange.test(ingredientCount)) {
				List<ItemPredicate> list = new ObjectArrayList(this.tappedItemPredicates);
				if (list.isEmpty()) {
					return true;
				} else {
					if (!itemStack.isEmpty()) {
						list.removeIf((itemPredicate) -> itemPredicate.test(itemStack));
					}
					return list.isEmpty();
				}
			} else {
				return false;
			}
		}
	}
	
}
