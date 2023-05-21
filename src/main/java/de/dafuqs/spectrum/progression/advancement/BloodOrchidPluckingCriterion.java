package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.predicate.entity.EntityPredicate.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;

public class BloodOrchidPluckingCriterion extends AbstractCriterion<BloodOrchidPluckingCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("blood_orchid_plucking");
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
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
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			return super.toJson(predicateSerializer);
		}
		
		public boolean matches() {
			return true;
		}
	}
	
}
