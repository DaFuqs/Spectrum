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

	@Override
	protected Conditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
		ItemPredicate itemPredicate = ItemPredicate.fromJson(jsonObject.get("rod"));
		LootContextPredicate bobber = LootContextPredicate.fromJson("bobber", predicateDeserializer, jsonObject, LootContextTypes.FISHING);
		bobber = bobber == null ? LootContextPredicate.EMPTY : bobber;
		LootContextPredicate hookedEntity = LootContextPredicate.fromJson("fishing", predicateDeserializer, jsonObject, LootContextTypes.ENTITY);
		hookedEntity = hookedEntity == null ? LootContextPredicate.EMPTY : hookedEntity;
		LootContextPredicate fishedEntity = LootContextPredicate.fromJson("fished_entity", predicateDeserializer, jsonObject, LootContextTypes.ENTITY);
		fishedEntity = fishedEntity == null ? LootContextPredicate.EMPTY : fishedEntity;
		ItemPredicate itemPredicate2 = ItemPredicate.fromJson(jsonObject.get("item"));
		FluidPredicate fluidPredicate = FluidPredicate.fromJson(jsonObject.get("fluid"));
		return new SpectrumFishingRodHookedCriterion.Conditions(playerPredicate, itemPredicate, bobber, hookedEntity, fishedEntity, itemPredicate2, fluidPredicate);
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate rod;
		private final LootContextPredicate bobber;
		private final LootContextPredicate hookedEntity;
		private final LootContextPredicate fishedEntity;
		private final ItemPredicate caughtItem;
		private final FluidPredicate fluidPredicate;
		
		public Conditions(LootContextPredicate player, ItemPredicate rod, LootContextPredicate bobber, LootContextPredicate hookedEntity, LootContextPredicate fishedEntity, ItemPredicate caughtItem, FluidPredicate fluidPredicate) {
			super(SpectrumFishingRodHookedCriterion.ID, player);
			this.rod = rod;
			this.bobber = bobber;
			this.hookedEntity = hookedEntity;
			this.fishedEntity = fishedEntity;
			this.caughtItem = caughtItem;
			this.fluidPredicate = fluidPredicate;
		}
		
		public static SpectrumFishingRodHookedCriterion.Conditions create(ItemPredicate rod, LootContextPredicate bobber, LootContextPredicate hookedEntity, LootContextPredicate fishedEntity, ItemPredicate item, FluidPredicate fluidPredicate) {
			return new SpectrumFishingRodHookedCriterion.Conditions(LootContextPredicate.EMPTY, rod, bobber, hookedEntity, fishedEntity, item, fluidPredicate);
		}
		
		public boolean matches(ItemStack rod, LootContext bobberContext, LootContext hookedEntityContext, LootContext fishedEntityContext, Collection<ItemStack> fishingLoots, ServerWorld world, BlockPos blockPos) {
			if (!this.rod.test(rod)) return false;
			if (!this.bobber.test(bobberContext)) return false;
			if (!this.fluidPredicate.test(world, blockPos)) return false;
			if (fishedEntityContext == null && !fishedEntity.equals(LootContextPredicate.EMPTY) ||
					!this.fishedEntity.test(fishedEntityContext)) return false;
			if (hookedEntityContext == null && !hookedEntity.equals(LootContextPredicate.EMPTY) ||
					!this.hookedEntity.test(hookedEntityContext)) return false;
			
			if (this.caughtItem != ItemPredicate.ANY) {
				if (hookedEntityContext != null) {
					Entity entity = hookedEntityContext.get(LootContextParameters.THIS_ENTITY);
					if (entity instanceof ItemEntity itemEntity &&
							this.caughtItem.test(itemEntity.getStack())) return true;
				}
				for (ItemStack itemStack : fishingLoots) {
					if (this.caughtItem.test(itemStack)) return true;
				}
				
				return false;
			}
			
			return true;
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
