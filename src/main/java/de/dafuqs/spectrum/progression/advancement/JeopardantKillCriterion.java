package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.EntityPredicate.Extended;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

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
