package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate.Extended;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class ConfirmationButtonPressedCriterion extends AbstractCriterion<ConfirmationButtonPressedCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("confirmation_button_pressed");
	
	public Identifier getId() {
		return ID;
	}
	
	public ConfirmationButtonPressedCriterion.Conditions conditionsFromJson(JsonObject jsonObject, Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		String confirmation = JsonHelper.getString(jsonObject, "confirmation");
		
		return new ConfirmationButtonPressedCriterion.Conditions(extended, confirmation);
	}
	
	public void trigger(ServerPlayerEntity player, String confirmation) {
		this.trigger(player, (conditions) -> {
			return conditions.matches(confirmation);
		});
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		
		private final String confirmation;
		
		public Conditions(Extended player, String confirmation) {
			super(ConfirmationButtonPressedCriterion.ID, player);
			this.confirmation = confirmation;
		}
		
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
