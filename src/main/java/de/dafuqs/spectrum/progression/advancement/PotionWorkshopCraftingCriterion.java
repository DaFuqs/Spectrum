package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.item.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.predicate.item.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;

import java.util.*;

public class PotionWorkshopCraftingCriterion extends AbstractCriterion<PotionWorkshopCraftingCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("crafted_with_potion_workshop");
	
	public static PotionWorkshopCraftingCriterion.Conditions create(ItemPredicate[] item) {
		return new PotionWorkshopCraftingCriterion.Conditions(EntityPredicate.Extended.EMPTY, item);
	}
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
	public PotionWorkshopCraftingCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		ItemPredicate[] itemPredicates = ItemPredicate.deserializeAll(jsonObject.get("items"));
		return new PotionWorkshopCraftingCriterion.Conditions(extended, itemPredicates);
	}
	
	public void trigger(ServerPlayerEntity player, ItemStack itemStack) {
		this.trigger(player, (conditions) -> conditions.matches(itemStack));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate[] itemPredicates;
		
		public Conditions(EntityPredicate.Extended player, ItemPredicate[] itemPredicates) {
			super(ID, player);
			this.itemPredicates = itemPredicates;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.addProperty("items", this.itemPredicates.toString());
			return jsonObject;
		}
		
		public boolean matches(ItemStack itemStack) {
			List<ItemPredicate> list = new ObjectArrayList<>(this.itemPredicates);
			if (list.isEmpty()) {
				return true;
			} else {
				if (!itemStack.isEmpty()) {
					list.removeIf((itemPredicate) -> itemPredicate.test(itemStack));
				}
				return list.isEmpty();
			}
		}
	}
	
}
