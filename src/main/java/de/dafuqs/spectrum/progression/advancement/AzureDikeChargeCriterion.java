package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;

import java.util.*;

public class AzureDikeChargeCriterion extends SimpleCriterionTrigger<AzureDikeChargeCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("azure_dike_charge_change");
	
	public void trigger(ServerPlayer player, float charges, int rechargeRate, float change) {
		this.trigger(player, (conditions) -> conditions.matches(charges, rechargeRate, change));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			MinMaxBounds.Ints charges,
			MinMaxBounds.Ints rechargeRate,
			MinMaxBounds.Ints change
	) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<AzureDikeChargeCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(AzureDikeChargeCriterion.Conditions::player),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("charges", MinMaxBounds.Ints.ANY).forGetter(AzureDikeChargeCriterion.Conditions::charges),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("recharge_rate", MinMaxBounds.Ints.ANY).forGetter(AzureDikeChargeCriterion.Conditions::rechargeRate),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("change", MinMaxBounds.Ints.ANY).forGetter(AzureDikeChargeCriterion.Conditions::change)
		).apply(instance, AzureDikeChargeCriterion.Conditions::new));
		
		public boolean matches(float charges, int rechargeRate, float change) {
			return this.charges.matches(Math.round(charges)) && this.rechargeRate.matches(rechargeRate) && this.change.matches(Math.round(change));
		}
	}
}
	
