package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;

public class PreservationCheckCriterion extends AbstractCriterion<PreservationCheckCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("preservation_check");
	
	public static PreservationCheckCriterion.Conditions create(String checkName, boolean checkPassed) {
		return new PreservationCheckCriterion.Conditions(EntityPredicate.Extended.EMPTY, checkName, checkPassed);
	}
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
	public PreservationCheckCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		String checkName = JsonHelper.getString(jsonObject, "check_name", "");
		boolean checkPassed = JsonHelper.getBoolean(jsonObject, "check_passed", true);
		return new PreservationCheckCriterion.Conditions(extended, checkName, checkPassed);
	}
	
	public void trigger(ServerPlayerEntity player, String checkName, boolean checkPassed) {
		this.trigger(player, (conditions) -> conditions.matches(checkName, checkPassed));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		
		private final String checkName;
		private final boolean checkPassed;
		
		public Conditions(EntityPredicate.Extended player, String checkName, boolean checkPassed) {
			super(ID, player);
			this.checkName = checkName;
			this.checkPassed = checkPassed;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.addProperty("check_name", this.checkName);
			jsonObject.addProperty("check_passed", this.checkPassed);
			return jsonObject;
		}
		
		public boolean matches(String name, boolean checkPassed) {
			return this.checkPassed == checkPassed && (this.checkName.isEmpty() || this.checkName.equals(name));
		}
	}
	
}
