package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.loot.context.*;
import net.minecraft.predicate.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.predicate.item.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

import java.util.*;

/**
 * Advanced fishing criterion that can also:
 * - match the fluid that was fished in
 * - fished entities
 */
public class SpectrumFishingRodHookedCriterion extends AbstractCriterion<SpectrumFishingRodHookedCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("fishing_rod_hooked");
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
	public SpectrumFishingRodHookedCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		ItemPredicate itemPredicate = ItemPredicate.fromJson(jsonObject.get("rod"));
		EntityPredicate.Extended bobber = EntityPredicate.Extended.getInJson(jsonObject, "bobber", advancementEntityPredicateDeserializer);
		EntityPredicate.Extended hookedEntity = EntityPredicate.Extended.getInJson(jsonObject, "hooked_entity", advancementEntityPredicateDeserializer);
		EntityPredicate.Extended fishedEntity = EntityPredicate.Extended.getInJson(jsonObject, "fished_entity", advancementEntityPredicateDeserializer);
		ItemPredicate itemPredicate2 = ItemPredicate.fromJson(jsonObject.get("item"));
		FluidPredicate fluidPredicate = FluidPredicate.fromJson(jsonObject.get("fluid"));
		return new SpectrumFishingRodHookedCriterion.Conditions(extended, itemPredicate, bobber, hookedEntity, fishedEntity, itemPredicate2, fluidPredicate);
	}
	
	public void trigger(ServerPlayerEntity player, ItemStack rod, SpectrumFishingBobberEntity bobber, Entity fishedEntity, Collection<ItemStack> fishingLoots) {
		LootContext bobberContext = EntityPredicate.createAdvancementEntityLootContext(player, bobber);
		LootContext hookedEntityContext = bobber.getHookedEntity() == null ? null : EntityPredicate.createAdvancementEntityLootContext(player, bobber.getHookedEntity());
		LootContext fishedEntityContext = fishedEntity == null ? null : EntityPredicate.createAdvancementEntityLootContext(player, fishedEntity);
		this.trigger(player, (conditions) -> conditions.matches(rod, bobberContext, hookedEntityContext, fishedEntityContext, fishingLoots, (ServerWorld) bobber.getWorld(), bobber.getBlockPos()));
		
		// also trigger vanilla fishing criterion
		// since that one requires a FishingBobberEntity and SpectrumFishingBobberEntity
		// does not extend that we have to do some hacky shenanigans running trigger() directly
		LootContext hookedEntityOrBobberContext = EntityPredicate.createAdvancementEntityLootContext(player, (bobber.getHookedEntity() != null ? bobber.getHookedEntity() : bobber));
		Criteria.FISHING_ROD_HOOKED.trigger(player, (conditions) -> conditions.matches(rod, hookedEntityOrBobberContext, fishingLoots));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate rod;
		private final EntityPredicate.Extended bobber;
		private final EntityPredicate.Extended hookedEntity;
		private final EntityPredicate.Extended fishedEntity;
		private final ItemPredicate caughtItem;
		private final FluidPredicate fluidPredicate;
		
		public Conditions(EntityPredicate.Extended player, ItemPredicate rod, EntityPredicate.Extended bobber, EntityPredicate.Extended hookedEntity, EntityPredicate.Extended fishedEntity, ItemPredicate caughtItem, FluidPredicate fluidPredicate) {
			super(SpectrumFishingRodHookedCriterion.ID, player);
			this.rod = rod;
			this.bobber = bobber;
			this.hookedEntity = hookedEntity;
			this.fishedEntity = fishedEntity;
			this.caughtItem = caughtItem;
			this.fluidPredicate = fluidPredicate;
		}
		
		public static SpectrumFishingRodHookedCriterion.Conditions create(ItemPredicate rod, EntityPredicate bobber, EntityPredicate hookedEntity, EntityPredicate fishedEntity, ItemPredicate item, FluidPredicate fluidPredicate) {
			return new SpectrumFishingRodHookedCriterion.Conditions(EntityPredicate.Extended.EMPTY, rod, EntityPredicate.Extended.ofLegacy(bobber), EntityPredicate.Extended.ofLegacy(hookedEntity), EntityPredicate.Extended.ofLegacy(fishedEntity), item, fluidPredicate);
		}
		
		public boolean matches(ItemStack rod, LootContext bobberContext, LootContext hookedEntityContext, LootContext fishedEntityContext, Collection<ItemStack> fishingLoots, ServerWorld world, BlockPos blockPos) {
			if (!this.rod.test(rod)) {
				return false;
			} else if (!this.bobber.test(bobberContext)) {
				return false;
			} else if ((hookedEntityContext == null && !hookedEntity.equals(EntityPredicate.Extended.EMPTY)) || !this.hookedEntity.test(hookedEntityContext)) {
				return false;
			} else if ((fishedEntityContext == null && !fishedEntity.equals(EntityPredicate.Extended.EMPTY)) || !this.fishedEntity.test(fishedEntityContext)) {
				return false;
			} else if (!this.fluidPredicate.test(world, blockPos)) {
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
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("rod", this.rod.toJson());
			jsonObject.add("bobber", this.bobber.toJson(predicateSerializer));
			jsonObject.add("hooked_entity", this.hookedEntity.toJson(predicateSerializer));
			jsonObject.add("fished_entity", this.fishedEntity.toJson(predicateSerializer));
			jsonObject.add("item", this.caughtItem.toJson());
			jsonObject.add("fluid", this.fluidPredicate.toJson());
			return jsonObject;
		}
	}
	
}
