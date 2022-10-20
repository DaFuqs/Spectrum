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

public class BloodOrchidPluckingCriterion extends AbstractCriterion<BloodOrchidPluckingCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("blood_orchid_plucking");
	
	public Identifier getId() {
		return ID;
	}
	
	public BloodOrchidPluckingCriterion.Conditions conditionsFromJson(JsonObject jsonObject, Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		return new BloodOrchidPluckingCriterion.Conditions(extended);
	}
	
	public void trigger(ServerPlayerEntity player) {
		this.trigger(player, Conditions::matches);
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		
		public Conditions(Extended player) {
			super(BloodOrchidPluckingCriterion.ID, player);
		}
		
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			return super.toJson(predicateSerializer);
		}
		
		public boolean matches() {
			return true;
		}
	}
	
}
