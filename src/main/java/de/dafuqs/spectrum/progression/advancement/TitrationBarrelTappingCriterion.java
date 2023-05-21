package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.item.*;
import net.minecraft.predicate.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.predicate.item.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;

import java.util.*;

public class TitrationBarrelTappingCriterion extends AbstractCriterion<TitrationBarrelTappingCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("titration_barrel_tapping");
	
	public static TitrationBarrelTappingCriterion.Conditions create(ItemPredicate[] item, NumberRange.IntRange ingameDaysAgeRange, NumberRange.IntRange ingredientCountRange) {
		return new TitrationBarrelTappingCriterion.Conditions(EntityPredicate.Extended.EMPTY, item, ingameDaysAgeRange, ingredientCountRange);
	}
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
	public TitrationBarrelTappingCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		ItemPredicate[] tappedItemPredicates = ItemPredicate.deserializeAll(jsonObject.get("items"));
		NumberRange.IntRange ingameDaysAgeRange = NumberRange.IntRange.fromJson(jsonObject.get("age_ingame_days"));
		NumberRange.IntRange ingredientCountRange = NumberRange.IntRange.fromJson(jsonObject.get("ingredient_count"));
		return new TitrationBarrelTappingCriterion.Conditions(extended, tappedItemPredicates, ingameDaysAgeRange, ingredientCountRange);
	}
	
	public void trigger(ServerPlayerEntity player, ItemStack itemStack, int ingameDaysAge, int ingredientCount) {
		this.trigger(player, (conditions) -> conditions.matches(itemStack, ingameDaysAge, ingredientCount));
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
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.addProperty("items", this.tappedItemPredicates.toString());
			jsonObject.add("age_ingame_days", this.ingameDaysAgeRange.toJson());
			jsonObject.add("ingredient_count", this.ingredientCountRange.toJson());
			return jsonObject;
		}
		
		public boolean matches(ItemStack itemStack, int experience, int ingredientCount) {
			if (this.ingameDaysAgeRange.test(experience) && this.ingredientCountRange.test(ingredientCount)) {
				List<ItemPredicate> list = new ObjectArrayList<>(this.tappedItemPredicates);
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
