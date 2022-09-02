package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate.Extended;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class DivinityTickCriterion extends AbstractCriterion<DivinityTickCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("divinity_tick");
	
	public Identifier getId() {
		return ID;
	}
	
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
