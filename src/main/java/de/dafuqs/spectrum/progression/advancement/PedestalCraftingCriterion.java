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

public class PedestalCraftingCriterion extends AbstractCriterion<PedestalCraftingCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("crafted_with_pedestal");
	
	public static PedestalCraftingCriterion.Conditions create(ItemPredicate[] item, NumberRange.IntRange experienceRange, NumberRange.IntRange durationTicks) {
		return new PedestalCraftingCriterion.Conditions(EntityPredicate.Extended.EMPTY, item, experienceRange, durationTicks);
	}
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
	public PedestalCraftingCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		ItemPredicate[] itemPredicates = ItemPredicate.deserializeAll(jsonObject.get("items"));
		NumberRange.IntRange experienceRange = NumberRange.IntRange.fromJson(jsonObject.get("gained_experience"));
		NumberRange.IntRange craftingDurationTicksRange = NumberRange.IntRange.fromJson(jsonObject.get("crafting_duration_ticks"));
		return new PedestalCraftingCriterion.Conditions(extended, itemPredicates, experienceRange, craftingDurationTicksRange);
	}
	
	public void trigger(ServerPlayerEntity player, ItemStack itemStack, int experience, int durationTicks) {
		this.trigger(player, (conditions) -> conditions.matches(itemStack, experience, durationTicks));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate[] itemPredicates;
		private final NumberRange.IntRange experienceRange;
		private final NumberRange.IntRange craftingDurationTicksRange;
		
		public Conditions(EntityPredicate.Extended player, ItemPredicate[] itemPredicates, NumberRange.IntRange experienceRange, NumberRange.IntRange craftingDurationTicksRange) {
			this(ID, player, itemPredicates, experienceRange, craftingDurationTicksRange);
		}
		
		public Conditions(Identifier id, EntityPredicate.Extended player, ItemPredicate[] itemPredicates, NumberRange.IntRange experienceRange, NumberRange.IntRange craftingDurationTicksRange) {
			super(id, player);
			this.itemPredicates = itemPredicates;
			this.experienceRange = experienceRange;
			this.craftingDurationTicksRange = craftingDurationTicksRange;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.addProperty("items", this.itemPredicates.toString());
			jsonObject.add("gained_experience", this.experienceRange.toJson());
			jsonObject.add("crafting_duration_ticks", this.experienceRange.toJson());
			return jsonObject;
		}
		
		public boolean matches(ItemStack itemStack, int experience, int durationTicks) {
			if (this.experienceRange.test(experience) && this.craftingDurationTicksRange.test(durationTicks)) {
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
