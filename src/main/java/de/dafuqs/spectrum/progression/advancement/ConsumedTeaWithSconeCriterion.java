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

public class ConsumedTeaWithSconeCriterion extends AbstractCriterion<ConsumedTeaWithSconeCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("consumed_tea_with_scone");
	
	public static Conditions create(ItemPredicate teaItemPredicate, ItemPredicate sconeItemPredicate) {
		return new Conditions(EntityPredicate.Extended.EMPTY, teaItemPredicate, sconeItemPredicate);
	}
	
	public Identifier getId() {
		return ID;
	}
	
	public Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer deserializer) {
		ItemPredicate teaItemPredicate = ItemPredicate.fromJson(jsonObject.get("tea_items"));
		ItemPredicate sconeItemPredicate = ItemPredicate.fromJson(jsonObject.get("scone_items"));
		return new Conditions(extended, teaItemPredicate, sconeItemPredicate);
	}
	
	public void trigger(ServerPlayerEntity player, ItemStack teaStack, ItemStack sconeStack) {
		this.trigger(player, (conditions) -> conditions.matches(teaStack, sconeStack));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate teaItemPredicate;
		private final ItemPredicate sconeItemPredicate;
		
		public Conditions(EntityPredicate.Extended player, ItemPredicate teaItemPredicate, ItemPredicate sconeItemPredicate) {
			super(ID, player);
			this.teaItemPredicate = teaItemPredicate;
			this.sconeItemPredicate = sconeItemPredicate;
		}
		
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.addProperty("tea_items", this.teaItemPredicate.toString());
			jsonObject.addProperty("scone_items", this.sconeItemPredicate.toString());
			return jsonObject;
		}
		
		public boolean matches(ItemStack teaStack, ItemStack sconeStack) {
			return teaItemPredicate.test(teaStack) && sconeItemPredicate.test(sconeStack);
		}
		
	}
	
}