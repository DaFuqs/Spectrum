package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.entity.*;
import net.minecraft.loot.context.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.predicate.entity.EntityPredicate.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;

public class MemoryManifestingCriterion extends AbstractCriterion<MemoryManifestingCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("memory_manifesting");
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
	public MemoryManifestingCriterion.Conditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		return new MemoryManifestingCriterion.Conditions(ID, extended, LootContextPredicate.fromJson("manifseted_entity", advancementEntityPredicateDeserializer, jsonObject, LootContextTypes.FISHING));
	}
	
	public void trigger(ServerPlayerEntity player, Entity manifestedEntity) {
		LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, manifestedEntity);
		this.trigger(player, (conditions) -> conditions.matches(lootContext));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final LootContextPredicate manifestedEntity;
		
		public Conditions(Identifier id, LootContextPredicate player, LootContextPredicate manifestedEntity) {
			super(id, player);
			this.manifestedEntity = manifestedEntity;
		}
		
		public boolean matches(LootContext context) {
			return this.manifestedEntity.test(context);
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("manifested_entity", this.manifestedEntity.toJson(predicateSerializer));
			return jsonObject;
		}
	}
	
}
