package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.item.*;
import net.minecraft.predicate.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.predicate.item.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;

public class PedestalRecipeCalculatedCriterion extends AbstractCriterion<PedestalCraftingCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("pedestal_recipe_calculated");
	
	public static PedestalCraftingCriterion.Conditions create(ItemPredicate[] item, NumberRange.IntRange experienceRange, NumberRange.IntRange durationTicks) {
		return new PedestalCraftingCriterion.Conditions(ID, EntityPredicate.Extended.EMPTY, item, experienceRange, durationTicks);
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
		return new PedestalCraftingCriterion.Conditions(ID, extended, itemPredicates, experienceRange, craftingDurationTicksRange);
	}
	
	public void trigger(ServerPlayerEntity player, ItemStack itemStack, int experience, int durationTicks) {
		this.trigger(player, (conditions) -> conditions.matches(itemStack, experience, durationTicks));
	}
	
}
