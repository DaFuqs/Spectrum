package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.Entity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.EntityPredicate.Extended;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MemoryManifestingCriterion extends AbstractCriterion<MemoryManifestingCriterion.Conditions> {
	
	static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "memory_manifesting");
	
	public Identifier getId() {
		return ID;
	}
	
	public MemoryManifestingCriterion.Conditions conditionsFromJson(JsonObject jsonObject, Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		return new MemoryManifestingCriterion.Conditions(ID, extended, Extended.getInJson(jsonObject, "manifested_entity", advancementEntityPredicateDeserializer));
	}
	
	public void trigger(ServerPlayerEntity player, Entity manifestedEntity) {
		LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, manifestedEntity);
		this.trigger(player, (conditions) -> conditions.matches(lootContext));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final Extended manifestedEntity;
		
		public Conditions(Identifier id, Extended player, Extended manifestedEntity) {
			super(id, player);
			this.manifestedEntity = manifestedEntity;
		}
		
		public boolean matches(LootContext context) {
			return this.manifestedEntity.test(context);
		}
		
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("manifested_entity", this.manifestedEntity.toJson(predicateSerializer));
			return jsonObject;
		}
	}
	
}
