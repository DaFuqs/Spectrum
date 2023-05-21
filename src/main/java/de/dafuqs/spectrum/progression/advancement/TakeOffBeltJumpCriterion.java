package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.item.*;
import net.minecraft.predicate.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.predicate.item.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;

import java.util.*;

public class TakeOffBeltJumpCriterion extends AbstractCriterion<TakeOffBeltJumpCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("take_off_belt_jump");
	
	public static TakeOffBeltJumpCriterion.Conditions create(ItemPredicate itemPredicate, NumberRange.IntRange chargesRange) {
		return new TakeOffBeltJumpCriterion.Conditions(EntityPredicate.Extended.EMPTY, itemPredicate, chargesRange);
	}
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
	public TakeOffBeltJumpCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		ItemPredicate itemPredicate = ItemPredicate.fromJson(jsonObject.get("item"));
		NumberRange.IntRange chargesRange = NumberRange.IntRange.fromJson(jsonObject.get("charges"));
		return new TakeOffBeltJumpCriterion.Conditions(extended, itemPredicate, chargesRange);
	}
	
	public void trigger(ServerPlayerEntity player) {
		this.trigger(player, (conditions) -> {
			Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);
			if (component.isPresent()) {
				List<Pair<SlotReference, ItemStack>> equipped = component.get().getEquipped(SpectrumItems.TAKE_OFF_BELT);
				if (!equipped.isEmpty()) {
					ItemStack firstBelt = equipped.get(0).getRight();
					if (firstBelt != null) {
						int charge = TakeOffBeltItem.getCurrentCharge(player);
						if (charge > 0) {
							return conditions.matches(firstBelt, charge);
						}
					}
				}
			}
			return false;
		});
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate itemPredicate;
		private final NumberRange.IntRange chargesRange;
		
		public Conditions(EntityPredicate.Extended player, ItemPredicate itemPredicate, NumberRange.IntRange chargesRange) {
			super(ID, player);
			this.itemPredicate = itemPredicate;
			this.chargesRange = chargesRange;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.addProperty("item", this.itemPredicate.toString());
			jsonObject.addProperty("charges", this.chargesRange.toString());
			return jsonObject;
		}
		
		public boolean matches(ItemStack beltStack, int charge) {
			return itemPredicate.test(beltStack) && this.chargesRange.test(charge);
		}
	}
	
}
