package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.predicate.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;

public class DivinityTickCriterion extends AbstractCriterion<DivinityTickCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("divinity_tick");
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
	public DivinityTickCriterion.Conditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate predicate, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		NumberRange.FloatRange healthRange = NumberRange.FloatRange.fromJson(jsonObject.get("health"));

		Boolean isAlive = null;
		JsonElement isAliveElement = jsonObject.get("is_alive");
		if (isAliveElement != null && isAliveElement.isJsonPrimitive() && isAliveElement.getAsJsonPrimitive().isBoolean()) {
			isAlive = isAliveElement.getAsBoolean();
		}
		
		return new DivinityTickCriterion.Conditions(predicate, healthRange, isAlive);
	}
	
	public void trigger(ServerPlayerEntity player) {
		this.trigger(player, (conditions) -> conditions.matches(player.isAlive(), player.getHealth()));
	}
	
	public static class Conditions extends AbstractCriterionConditions {

		private final Boolean isAlive;
		private final NumberRange.FloatRange healthRange;
		
		public Conditions(LootContextPredicate player, NumberRange.FloatRange healthRange, Boolean isAlive) {
			super(DivinityTickCriterion.ID, player);
			this.isAlive = isAlive;
			this.healthRange = healthRange;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			if (this.isAlive != null) {
				jsonObject.addProperty("is_alive", this.isAlive);
			}
			jsonObject.add("health", this.healthRange.toJson());
			return jsonObject;
		}
		
		public boolean matches(boolean isPlayerAlive, float health) {
			return (this.isAlive == null || this.isAlive == isPlayerAlive)
					&& this.healthRange.test(health);
		}
	}
	
}
