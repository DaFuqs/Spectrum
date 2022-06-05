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

public class AzureDikeChargeCriterion extends AbstractCriterion<AzureDikeChargeCriterion.Conditions> {
	
	static final Identifier ID = new Identifier(SpectrumCommon.MOD_ID, "azure_dike_charge_change");
	
	public Identifier getId() {
		return ID;
	}
	
	public AzureDikeChargeCriterion.Conditions conditionsFromJson(JsonObject jsonObject, Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		NumberRange.IntRange chargesRange = NumberRange.IntRange.fromJson(jsonObject.get("charges"));
		NumberRange.IntRange rechargeRateRange = NumberRange.IntRange.fromJson(jsonObject.get("recharge_rate"));
		NumberRange.IntRange changeRange = NumberRange.IntRange.fromJson(jsonObject.get("change"));
		
		return new AzureDikeChargeCriterion.Conditions(extended, chargesRange, rechargeRateRange, changeRange);
	}
	
	public void trigger(ServerPlayerEntity player, int charges, int rechargeRate, int change) {
		this.trigger(player, (conditions) -> {
			return conditions.matches(charges, rechargeRate, change);
		});
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
