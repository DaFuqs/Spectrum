package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.predicate.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.predicate.entity.EntityPredicate.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;

public class DivinityTickCriterion extends AbstractCriterion<DivinityTickCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("divinity_tick");
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
	public DivinityTickCriterion.Conditions conditionsFromJson(JsonObject jsonObject, Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		NumberRange.FloatRange healthRange = NumberRange.FloatRange.fromJson(jsonObject.get("health"));
		
		return new DivinityTickCriterion.Conditions(extended, healthRange);
	}
	
	public void trigger(ServerPlayerEntity player) {
		this.trigger(player, (conditions) -> conditions.matches(player.getHealth()));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		
		private final NumberRange.FloatRange healthRange;
		
		public Conditions(Extended player, NumberRange.FloatRange healthRange) {
			super(DivinityTickCriterion.ID, player);
			this.healthRange = healthRange;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("health", this.healthRange.toJson());
			return jsonObject;
		}
		
		public boolean matches(float health) {
			return this.healthRange.test(health);
		}
	}
	
}
