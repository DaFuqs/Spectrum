package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.item.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.predicate.item.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

public class TreasureHunterDropCriterion extends AbstractCriterion<TreasureHunterDropCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("treasure_hunter_drop");
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
	public TreasureHunterDropCriterion.Conditions conditionsFromJson(@NotNull JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		ItemPredicate droppedItemPredicate = ItemPredicate.fromJson(jsonObject.get("dropped_item"));
		return new TreasureHunterDropCriterion.Conditions(extended, droppedItemPredicate);
	}
	
	public void trigger(ServerPlayerEntity player, ItemStack droppedStack) {
		this.trigger(player, (conditions) -> conditions.matches(droppedStack));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		
		private final ItemPredicate droppedItemPredicate;
		
		public Conditions(EntityPredicate.Extended player, @Nullable ItemPredicate droppedItemPredicate) {
			super(ID, player);
			this.droppedItemPredicate = droppedItemPredicate;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("dropped_item", this.droppedItemPredicate.toJson());
			return jsonObject;
		}
		
		public boolean matches(ItemStack droppedStack) {
			return this.droppedItemPredicate.test(droppedStack);
		}
	}
	
}
