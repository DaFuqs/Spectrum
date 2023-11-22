package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.loot.context.*;
import net.minecraft.predicate.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;

public class JeopardantKillCriterion extends AbstractCriterion<JeopardantKillCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("jeopardant_kill");
	
	@Override
	public Identifier getId() {
		return ID;
	}

	@Override
	protected Conditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
		return new JeopardantKillCriterion.Conditions(ID, playerPredicate,
			LootContextPredicate.fromJson("entity", predicateDeserializer, jsonObject, LootContextTypes.ENTITY),
			DamageSourcePredicate.fromJson(jsonObject.get("killing_blow")),
			NumberRange.IntRange.fromJson(jsonObject.get("lives_left"))
		);
	}

	public void trigger(ServerPlayerEntity player, Entity entity, DamageSource killingDamage) {
		LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, entity);
		this.trigger(player, (conditions) -> conditions.test(player, lootContext, killingDamage));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final LootContextPredicate entity;
		private final DamageSourcePredicate killingBlow;
		private final NumberRange.IntRange livesLeft;
		
		public Conditions(Identifier id, LootContextPredicate player, LootContextPredicate entity, DamageSourcePredicate killingBlow, NumberRange.IntRange livesLeft) {
			super(id, player);
			this.entity = entity;
			this.killingBlow = killingBlow;
			this.livesLeft = livesLeft;
		}
		
		public boolean test(ServerPlayerEntity player, LootContext killedEntityContext, DamageSource killingBlow) {
			return this.killingBlow.test(player, killingBlow) && this.entity.test(killedEntityContext) && this.livesLeft.test(Math.round(player.getHealth()));
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("entity", this.entity.toJson(predicateSerializer));
			jsonObject.add("killing_blow", this.killingBlow.toJson());
			jsonObject.add("lives_left", this.livesLeft.toJson());
			return jsonObject;
		}
	}
	
}
