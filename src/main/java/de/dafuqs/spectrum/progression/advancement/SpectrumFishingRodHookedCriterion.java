package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.entity.entity.SpectrumFishingBobberEntity;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Collection;

public class SpectrumFishingRodHookedCriterion extends AbstractCriterion<SpectrumFishingRodHookedCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("bedrock_fishing_rod_hooked");
	
	public Identifier getId() {
		return ID;
	}
	
	public SpectrumFishingRodHookedCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		ItemPredicate itemPredicate = ItemPredicate.fromJson(jsonObject.get("rod"));
		EntityPredicate.Extended extended2 = EntityPredicate.Extended.getInJson(jsonObject, "entity", advancementEntityPredicateDeserializer);
		ItemPredicate itemPredicate2 = ItemPredicate.fromJson(jsonObject.get("item"));
		return new SpectrumFishingRodHookedCriterion.Conditions(extended, itemPredicate, extended2, itemPredicate2);
	}
	
	public void trigger(ServerPlayerEntity player, ItemStack rod, SpectrumFishingBobberEntity bobber, Collection<ItemStack> fishingLoots) {
		LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, (bobber.getHookedEntity() != null ? bobber.getHookedEntity() : bobber));
		this.trigger(player, (conditions) -> conditions.matches(rod, lootContext, fishingLoots));
		
		// also trigger vanilla fishing criterion
		// since that one requires a FishingBobberEntity and SpectrumFishingBobberEntity
		// does not extend that we have to do some hacky shenigans running trigger() directly
		Criteria.FISHING_ROD_HOOKED.trigger(player, (conditions) -> {
			return conditions.matches(rod, lootContext, fishingLoots);
		});
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate rod;
		private final EntityPredicate.Extended hookedEntity;
		private final ItemPredicate caughtItem;
		
		public Conditions(EntityPredicate.Extended player, ItemPredicate rod, EntityPredicate.Extended hookedEntity, ItemPredicate caughtItem) {
			super(SpectrumFishingRodHookedCriterion.ID, player);
			this.rod = rod;
			this.hookedEntity = hookedEntity;
			this.caughtItem = caughtItem;
		}
		
		public static SpectrumFishingRodHookedCriterion.Conditions create(ItemPredicate rod, EntityPredicate bobber, ItemPredicate item) {
			return new SpectrumFishingRodHookedCriterion.Conditions(EntityPredicate.Extended.EMPTY, rod, EntityPredicate.Extended.ofLegacy(bobber), item);
		}
		
		public boolean matches(ItemStack rod, LootContext hookedEntityContext, Collection<ItemStack> fishingLoots) {
			if (!this.rod.test(rod)) {
				return false;
			} else if (!this.hookedEntity.test(hookedEntityContext)) {
				return false;
			} else {
				if (this.caughtItem != ItemPredicate.ANY) {
					boolean bl = false;
					Entity entity = hookedEntityContext.get(LootContextParameters.THIS_ENTITY);
					if (entity instanceof ItemEntity itemEntity) {
						if (this.caughtItem.test(itemEntity.getStack())) {
							bl = true;
						}
					}
					for (ItemStack itemStack : fishingLoots) {
						if (this.caughtItem.test(itemStack)) {
							bl = true;
							break;
						}
					}
					return bl;
				}
				return true;
			}
		}
		
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("rod", this.rod.toJson());
			jsonObject.add("entity", this.hookedEntity.toJson(predicateSerializer));
			jsonObject.add("item", this.caughtItem.toJson());
			return jsonObject;
		}
	}
	
}
