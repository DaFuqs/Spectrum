package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.items.trinkets.TakeOffBeltItem;
import de.dafuqs.spectrum.registries.SpectrumItems;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.List;
import java.util.Optional;

public class TakeOffBeltJumpCriterion extends AbstractCriterion<TakeOffBeltJumpCriterion.Conditions> {
	
	static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "take_off_belt_jump");
	
	public static TakeOffBeltJumpCriterion.Conditions create(ItemPredicate itemPredicate, NumberRange.IntRange chargesRange) {
		return new TakeOffBeltJumpCriterion.Conditions(EntityPredicate.Extended.EMPTY, itemPredicate, chargesRange);
	}
	
	public Identifier getId() {
		return ID;
	}
	
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
