package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.items.trinkets.SpectrumTrinketItem;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate.Extended;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrinketChangeCriterion extends AbstractCriterion<TrinketChangeCriterion.Conditions> {
	
	static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "trinket_change");
	
	public Identifier getId() {
		return ID;
	}
	
	public TrinketChangeCriterion.Conditions conditionsFromJson(JsonObject jsonObject, Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		ItemPredicate[] itemPredicates = ItemPredicate.deserializeAll(jsonObject.get("items"));
		NumberRange.IntRange totalCountRange = NumberRange.IntRange.fromJson(jsonObject.get("total_count"));
		NumberRange.IntRange spectrumCountRange = NumberRange.IntRange.fromJson(jsonObject.get("spectrum_count"));
		
		return new TrinketChangeCriterion.Conditions(extended, itemPredicates, totalCountRange, spectrumCountRange);
	}
	
	public void trigger(ServerPlayerEntity player) {
		this.trigger(player, (conditions) -> {
			Optional<TrinketComponent> trinketComponent = TrinketsApi.getTrinketComponent(player);
			if (trinketComponent.isPresent()) {
				List<ItemStack> equippedStacks = new ArrayList<>();
				int spectrumStacks = 0;
				for (Pair<SlotReference, ItemStack> t : trinketComponent.get().getAllEquipped()) {
					equippedStacks.add(t.getRight());
					if (t.getRight().getItem() instanceof SpectrumTrinketItem) {
						spectrumStacks++;
					}
				}
				return conditions.matches(equippedStacks, equippedStacks.size(), spectrumStacks);
			}
			return false;
		});
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		
		private final ItemPredicate[] itemPredicates;
		private final NumberRange.IntRange totalCountRange;
		private final NumberRange.IntRange spectrumCountRange;
		
		public Conditions(Extended player, ItemPredicate[] itemPredicates, NumberRange.IntRange totalCountRange, NumberRange.IntRange spectrumCountRange) {
			super(TrinketChangeCriterion.ID, player);
			this.itemPredicates = itemPredicates;
			this.totalCountRange = totalCountRange;
			this.spectrumCountRange = spectrumCountRange;
		}
		
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			
			if (this.itemPredicates.length > 0) {
				JsonArray jsonObject2 = new JsonArray();
				for (ItemPredicate itemPredicate : this.itemPredicates) {
					jsonObject2.add(itemPredicate.toJson());
				}
				
				jsonObject.add("items", jsonObject2);
			}
			jsonObject.add("total_count", this.totalCountRange.toJson());
			jsonObject.add("spectrum_count", this.spectrumCountRange.toJson());
			return jsonObject;
		}
		
		public boolean matches(List<ItemStack> trinketStacks, int totalCount, int spectrumCount) {
			if (this.totalCountRange.test(totalCount) && this.spectrumCountRange.test(spectrumCount)) {
				int i = this.itemPredicates.length;
				if (i == 0) {
					return true;
				} else if (i != 1) {
					List<ItemPredicate> list = new ObjectArrayList(this.itemPredicates);
					
					for (ItemStack trinketStack : trinketStacks) {
						if (list.isEmpty()) {
							return true;
						}
						if (!trinketStack.isEmpty()) {
							list.removeIf((item) -> item.test(trinketStack));
						}
					}
					
					return list.isEmpty();
				}
			}
			return false;
		}
	}
	
}
