package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;

import java.util.*;

public class DivinityTickCriterion extends SimpleCriterionTrigger<DivinityTickCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("divinity_tick");
	
	public void trigger(ServerPlayer player) {
		this.trigger(player, (conditions) -> conditions.matches(player.isAlive(), player.getHealth()));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			Optional<Boolean> isAlive,
			MinMaxBounds.Doubles healthRange
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				Codec.BOOL.optionalFieldOf("isAlive").forGetter(Conditions::isAlive),
				MinMaxBounds.Doubles.CODEC.optionalFieldOf("healthRange", MinMaxBounds.Doubles.ANY).forGetter(Conditions::healthRange)
		).apply(instance, DivinityTickCriterion.Conditions::new));
		
		public boolean matches(boolean isPlayerAlive, float health) {
			return this.isAlive.isPresent() && (isPlayerAlive == this.isAlive.get()) && this.healthRange.matches(health);
		}
	}
}
