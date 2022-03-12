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

public class CreatureSpawnHatchCriterion extends AbstractCriterion<CreatureSpawnHatchCriterion.Conditions> {
    
    static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "creature_spawn_hatching");
    
    public Identifier getId() {
        return ID;
    }
    
    public CreatureSpawnHatchCriterion.Conditions conditionsFromJson(JsonObject jsonObject, Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        return new CreatureSpawnHatchCriterion.Conditions(ID, extended, Extended.getInJson(jsonObject, "hatched_entity", advancementEntityPredicateDeserializer));
    }
    
    public void trigger(ServerPlayerEntity player, Entity hatchedEntity) {
        LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, hatchedEntity);
        this.trigger(player, (conditions) -> conditions.matches(lootContext));
    }
    
    public static class Conditions extends AbstractCriterionConditions {
        private final Extended hatchedEntity;
        
        public Conditions(Identifier id, Extended player, Extended hatchedEntity) {
            super(id, player);
            this.hatchedEntity = hatchedEntity;
        }
        
        public boolean matches(LootContext context) {
            return this.hatchedEntity.test(context);
        }
        
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            jsonObject.add("hatched_entity", this.hatchedEntity.toJson(predicateSerializer));
            return jsonObject;
        }
    }
    
}
