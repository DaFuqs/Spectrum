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
	
	public static TitrationBarrelTappingCriterion.Conditions create(ItemPredicate[] item, NumberRange.IntRange ingameDaysAgeRange) {
		return new TitrationBarrelTappingCriterion.Conditions(EntityPredicate.Extended.EMPTY, item, ingameDaysAgeRange);
	}
	
	public Identifier getId() {
		return ID;
	}
	
	public TitrationBarrelTappingCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		ItemPredicate[] tappedItemPredicates = ItemPredicate.deserializeAll(jsonObject.get("items"));
		NumberRange.IntRange ingameDaysAgeRange = NumberRange.IntRange.fromJson(jsonObject.get("age_ingame_days"));
		return new TitrationBarrelTappingCriterion.Conditions(extended, tappedItemPredicates, ingameDaysAgeRange);
	}
	
	public void trigger(ServerPlayerEntity player, ItemStack itemStack, int ingameDaysAge) {
		this.trigger(player, (conditions) -> {
			return conditions.matches(itemStack, ingameDaysAge);
		});
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate[] tappedItemPredicates;
		private final NumberRange.IntRange ingameDaysAgeRange;
		
		public Conditions(EntityPredicate.Extended player, ItemPredicate[] tappedItemPredicates, NumberRange.IntRange ingameDaysAgeRange) {
			super(ID, player);
			this.tappedItemPredicates = tappedItemPredicates;
			this.ingameDaysAgeRange = ingameDaysAgeRange;
		}
		
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.addProperty("items", this.tappedItemPredicates.toString());
			jsonObject.add("age_ingame_days", this.ingameDaysAgeRange.toJson());
			return jsonObject;
		}
		
		public boolean matches(ItemStack itemStack, int experience) {
			if (this.ingameDaysAgeRange.test(experience)) {
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
