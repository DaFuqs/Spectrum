package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.item.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.predicate.item.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;

public class CrystalApothecaryCollectingCriterion extends AbstractCriterion<CrystalApothecaryCollectingCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("collect_using_crystal_apothecary");
	
	public static CrystalApothecaryCollectingCriterion.Conditions create(ItemPredicate item) {
		return new CrystalApothecaryCollectingCriterion.Conditions(EntityPredicate.Extended.EMPTY, item);
	}
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
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
		
		@Override
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
