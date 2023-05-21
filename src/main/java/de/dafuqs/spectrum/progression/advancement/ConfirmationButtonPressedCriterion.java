package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.predicate.entity.EntityPredicate.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;

public class ConfirmationButtonPressedCriterion extends AbstractCriterion<ConfirmationButtonPressedCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("confirmation_button_pressed");
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
	public ConfirmationButtonPressedCriterion.Conditions conditionsFromJson(JsonObject jsonObject, Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		String confirmation = JsonHelper.getString(jsonObject, "confirmation");
		
		return new ConfirmationButtonPressedCriterion.Conditions(extended, confirmation);
	}
	
	public void trigger(ServerPlayerEntity player, String confirmation) {
		this.trigger(player, (conditions) -> conditions.matches(confirmation));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		
		private final String confirmation;
		
		public Conditions(Extended player, String confirmation) {
			super(ConfirmationButtonPressedCriterion.ID, player);
			this.confirmation = confirmation;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("confirmation", new JsonPrimitive(this.confirmation));
			return jsonObject;
		}
		
		public boolean matches(String confirmation) {
			return this.confirmation.equals(confirmation);
		}
	}
	
}
