package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.predicate.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.predicate.entity.EntityPredicate.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;

public class AzureDikeChargeCriterion extends AbstractCriterion<AzureDikeChargeCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("azure_dike_charge_change");
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
	public AzureDikeChargeCriterion.Conditions conditionsFromJson(JsonObject jsonObject, Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		NumberRange.IntRange chargesRange = NumberRange.IntRange.fromJson(jsonObject.get("charges"));
		NumberRange.IntRange rechargeRateRange = NumberRange.IntRange.fromJson(jsonObject.get("recharge_rate"));
		NumberRange.IntRange changeRange = NumberRange.IntRange.fromJson(jsonObject.get("change"));
		
		return new AzureDikeChargeCriterion.Conditions(extended, chargesRange, rechargeRateRange, changeRange);
	}
	
	public void trigger(ServerPlayerEntity player, int charges, int rechargeRate, int change) {
		this.trigger(player, (conditions) -> conditions.matches(charges, rechargeRate, change));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		
		private final NumberRange.IntRange chargesRange;
		private final NumberRange.IntRange rechargeRateRange;
		private final NumberRange.IntRange changeRange;
		
		public Conditions(Extended player, NumberRange.IntRange chargesRange, NumberRange.IntRange rechargeRateRange, NumberRange.IntRange changeRange) {
			super(AzureDikeChargeCriterion.ID, player);
			this.chargesRange = chargesRange;
			this.rechargeRateRange = rechargeRateRange;
			this.changeRange = changeRange;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("charges", this.chargesRange.toJson());
			jsonObject.add("recharge_rate", this.rechargeRateRange.toJson());
			jsonObject.add("change", this.changeRange.toJson());
			return jsonObject;
		}
		
		public boolean matches(int charges, int rechargeRate, int change) {
			return this.chargesRange.test(charges) && this.rechargeRateRange.test(rechargeRate) && this.changeRange.test(change);
		}
	}
	
}
