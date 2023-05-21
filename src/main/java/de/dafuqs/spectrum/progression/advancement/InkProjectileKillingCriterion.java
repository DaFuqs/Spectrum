package de.dafuqs.spectrum.progression.advancement;

import com.google.common.collect.*;
import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.entity.*;
import net.minecraft.loot.context.*;
import net.minecraft.predicate.NumberRange.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;

import java.util.*;

public class InkProjectileKillingCriterion extends AbstractCriterion<InkProjectileKillingCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("ink_projectile_killing");
	
	public InkProjectileKillingCriterion() {
	}
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
	public InkProjectileKillingCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		EntityPredicate.Extended[] extendeds = EntityPredicate.Extended.requireInJson(jsonObject, "victims", advancementEntityPredicateDeserializer);
		IntRange intRange = IntRange.fromJson(jsonObject.get("unique_entity_types"));
		return new InkProjectileKillingCriterion.Conditions(extended, extendeds, intRange);
	}
	
	public void trigger(ServerPlayerEntity player, Collection<Entity> piercingKilledEntities) {
		List<LootContext> list = Lists.newArrayList();
		Set<EntityType<?>> set = Sets.newHashSet();
		
		for (Entity entity : piercingKilledEntities) {
			set.add(entity.getType());
			list.add(EntityPredicate.createAdvancementEntityLootContext(player, entity));
		}
		
		this.trigger(player, (conditions) -> conditions.matches(list, set.size()));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final EntityPredicate.Extended[] victims;
		private final IntRange uniqueEntityTypes;
		
		public Conditions(EntityPredicate.Extended player, EntityPredicate.Extended[] victims, IntRange uniqueEntityTypes) {
			super(InkProjectileKillingCriterion.ID, player);
			this.victims = victims;
			this.uniqueEntityTypes = uniqueEntityTypes;
		}
		
		public static InkProjectileKillingCriterion.Conditions create(EntityPredicate.Builder... victimPredicates) {
			EntityPredicate.Extended[] extendeds = new EntityPredicate.Extended[victimPredicates.length];
			
			for (int i = 0; i < victimPredicates.length; ++i) {
				EntityPredicate.Builder builder = victimPredicates[i];
				extendeds[i] = EntityPredicate.Extended.ofLegacy(builder.build());
			}
			
			return new InkProjectileKillingCriterion.Conditions(EntityPredicate.Extended.EMPTY, extendeds, IntRange.ANY);
		}
		
		public static InkProjectileKillingCriterion.Conditions create(IntRange uniqueEntityTypes) {
			EntityPredicate.Extended[] extendeds = new EntityPredicate.Extended[0];
			return new InkProjectileKillingCriterion.Conditions(EntityPredicate.Extended.EMPTY, extendeds, uniqueEntityTypes);
		}
		
		public boolean matches(Collection<LootContext> victimContexts, int uniqueEntityTypeCount) {
			if (this.victims.length > 0) {
				List<LootContext> list = Lists.newArrayList(victimContexts);
				
				for (EntityPredicate.Extended extended : this.victims) {
					boolean bl = false;
					
					Iterator<LootContext> iterator = list.iterator();
					while (iterator.hasNext()) {
						LootContext lootContext = iterator.next();
						if (extended.test(lootContext)) {
							iterator.remove();
							bl = true;
							break;
						}
					}
					
					if (!bl) {
						return false;
					}
				}
			}
			
			return this.uniqueEntityTypes.test(uniqueEntityTypeCount);
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("victims", EntityPredicate.Extended.toPredicatesJsonArray(this.victims, predicateSerializer));
			jsonObject.add("unique_entity_types", this.uniqueEntityTypes.toJson());
			return jsonObject;
		}
	}
}
