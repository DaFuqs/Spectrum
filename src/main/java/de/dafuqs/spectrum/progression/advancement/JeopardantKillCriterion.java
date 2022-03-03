package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import net.minecraft.advancement.criterion.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.DamagePredicate;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.EntityPredicate.Extended;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntPredicate;

public class JeopardantKillCriterion extends AbstractCriterion<JeopardantKillCriterion.Conditions> {
    
    static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "jeopardant_kill");
    
    public Identifier getId() {
        return ID;
    }
    
    public JeopardantKillCriterion.Conditions conditionsFromJson(JsonObject jsonObject, Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        return new JeopardantKillCriterion.Conditions(ID, extended,
                Extended.getInJson(jsonObject, "entity", advancementEntityPredicateDeserializer),
                DamageSourcePredicate.fromJson(jsonObject.get("killing_blow")),
                NumberRange.IntRange.fromJson(jsonObject.get("lives_left"))
        );
    }
    
    public void trigger(ServerPlayerEntity player, Entity entity, DamageSource killingDamage) {
        LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, entity);
        this.trigger(player, (conditions) -> conditions.test(player, lootContext, killingDamage));
    }
    
    public static class Conditions extends AbstractCriterionConditions {
        private final Extended entity;
        private final DamageSourcePredicate killingBlow;
        private final NumberRange.IntRange livesLeft;
        
        public Conditions(Identifier id, Extended player, Extended entity, DamageSourcePredicate killingBlow, NumberRange.IntRange livesLeft) {
            super(id, player);
            this.entity = entity;
            this.killingBlow = killingBlow;
            this.livesLeft = livesLeft;
        }
        
        public boolean test(ServerPlayerEntity player, LootContext killedEntityContext, DamageSource killingBlow) {
            return this.killingBlow.test(player, killingBlow) && this.entity.test(killedEntityContext) && this.livesLeft.test(Math.round(player.getHealth()));
        }
        
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            jsonObject.add("entity", this.entity.toJson(predicateSerializer));
            jsonObject.add("killing_blow", this.killingBlow.toJson());
            jsonObject.add("lives_left", this.livesLeft.toJson());
            return jsonObject;
        }
    }
    
}
