package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TreasureHunterDropCriterion extends AbstractCriterion<TreasureHunterDropCriterion.Conditions> {
	
	static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "treasure_hunter_drop");
	
	public Identifier getId() {
		return ID;
	}
	
	public TreasureHunterDropCriterion.Conditions conditionsFromJson(@NotNull JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		ItemPredicate droppedItemPredicate = ItemPredicate.fromJson(jsonObject.get("dropped_item"));
		return new TreasureHunterDropCriterion.Conditions(extended, droppedItemPredicate);
	}
	
	public void trigger(ServerPlayerEntity player, ItemStack droppedStack) {
		this.trigger(player, (conditions) -> {
			return conditions.matches(droppedStack);
		});
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		
		private final ItemPredicate droppedItemPredicate;
		
		public Conditions(EntityPredicate.Extended player, @Nullable ItemPredicate droppedItemPredicate) {
			super(ID, player);
			this.droppedItemPredicate = droppedItemPredicate;
		}
		
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
