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

public class CrystalApothecaryCollectingCriterion extends AbstractCriterion<CrystalApothecaryCollectingCriterion.Conditions> {
	
	static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "collect_using_crystal_apothecary");
	
	public static CrystalApothecaryCollectingCriterion.Conditions create(ItemPredicate item) {
		return new CrystalApothecaryCollectingCriterion.Conditions(EntityPredicate.Extended.EMPTY, item);
	}
	
	public Identifier getId() {
		return ID;
	}
	
	public CrystalApothecaryCollectingCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		ItemPredicate itemPredicate = ItemPredicate.fromJson(jsonObject.get("item"));
		return new CrystalApothecaryCollectingCriterion.Conditions(extended, itemPredicate);
	}
	
	public void trigger(ServerPlayerEntity player, ItemStack itemStack) {
		this.trigger(player, (conditions) -> conditions.matches(itemStack));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate itemPredicate;
		
		public Conditions(EntityPredicate.Extended player, ItemPredicate itemPredicate) {
			super(ID, player);
			this.itemPredicate = itemPredicate;
		}
		
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("item", this.itemPredicate.toJson());
			return jsonObject;
		}
		
		public boolean matches(ItemStack stack) {
			return this.itemPredicate.test(stack);
		}
	}
	
}
