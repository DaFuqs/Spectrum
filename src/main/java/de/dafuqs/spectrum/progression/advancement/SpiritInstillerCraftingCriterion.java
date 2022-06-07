package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
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

import java.util.List;

public class SpiritInstillerCraftingCriterion extends AbstractCriterion<SpiritInstillerCraftingCriterion.Conditions> {
	
	static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "crafted_with_spirit_instiller");
	
	public static Conditions create(ItemPredicate[] item, NumberRange.IntRange experienceRange) {
		return new Conditions(EntityPredicate.Extended.EMPTY, item, experienceRange);
	}
	
	public Identifier getId() {
		return ID;
	}
	
	public Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		ItemPredicate[] itemPredicates = ItemPredicate.deserializeAll(jsonObject.get("items"));
		NumberRange.IntRange experienceRange = NumberRange.IntRange.fromJson(jsonObject.get("gained_experience"));
		return new Conditions(extended, itemPredicates, experienceRange);
	}
	
	public void trigger(ServerPlayerEntity player, ItemStack itemStack, int experience) {
		this.trigger(player, (conditions) -> {
			return conditions.matches(itemStack, experience);
		});
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate[] itemPredicates;
		private final NumberRange.IntRange experienceRange;
		
		public Conditions(EntityPredicate.Extended player, ItemPredicate[] itemPredicates, NumberRange.IntRange experienceRange) {
			super(ID, player);
			this.itemPredicates = itemPredicates;
			this.experienceRange = experienceRange;
		}
		
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.addProperty("items", this.itemPredicates.toString());
			jsonObject.add("gained_experience", this.experienceRange.toJson());
			return jsonObject;
		}
		
		public boolean matches(ItemStack itemStack, int experience) {
			if (this.experienceRange.test(experience)) {
				List<ItemPredicate> list = new ObjectArrayList(this.itemPredicates);
				if (list.isEmpty()) {
					return true;
				} else {
					if (!itemStack.isEmpty()) {
						list.removeIf((itemPredicate) -> itemPredicate.test(itemStack));
					}
					return list.isEmpty();
				}
			} else {
				return false;
			}
		}
	}
	
}