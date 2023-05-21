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

public class FusionShrineCraftingCriterion extends AbstractCriterion<FusionShrineCraftingCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("crafted_with_fusion_shrine");
	
	public static FusionShrineCraftingCriterion.Conditions create(ItemPredicate[] item, NumberRange.IntRange experienceRange) {
		return new FusionShrineCraftingCriterion.Conditions(EntityPredicate.Extended.EMPTY, item, experienceRange);
	}
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
	public FusionShrineCraftingCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		ItemPredicate[] itemPredicates = ItemPredicate.deserializeAll(jsonObject.get("items"));
		NumberRange.IntRange experienceRange = NumberRange.IntRange.fromJson(jsonObject.get("gained_experience"));
		return new FusionShrineCraftingCriterion.Conditions(extended, itemPredicates, experienceRange);
	}
	
	public void trigger(ServerPlayerEntity player, ItemStack itemStack, int experience) {
		this.trigger(player, (conditions) -> conditions.matches(itemStack, experience));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate[] itemPredicates;
		private final NumberRange.IntRange experienceRange;
		
		public Conditions(EntityPredicate.Extended player, ItemPredicate[] itemPredicates, NumberRange.IntRange experienceRange) {
			super(ID, player);
			this.itemPredicates = itemPredicates;
			this.experienceRange = experienceRange;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.addProperty("items", this.itemPredicates.toString());
			jsonObject.add("gained_experience", this.experienceRange.toJson());
			return jsonObject;
		}
		
		public boolean matches(ItemStack itemStack, int experience) {
			if (this.experienceRange.test(experience)) {
				List<ItemPredicate> list = new ObjectArrayList<>(this.itemPredicates);
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
