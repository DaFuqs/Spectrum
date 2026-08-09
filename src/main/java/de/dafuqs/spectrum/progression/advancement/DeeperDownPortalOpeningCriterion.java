package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.*;

import java.util.*;

public class DeeperDownPortalOpeningCriterion extends SimpleCriterionTrigger<DeeperDownPortalOpeningCriterion.Conditions> {
	
	public static final String NAME = "deeper_down_portal_opening";
	
	public void trigger(ServerPlayer player) {
		this.trigger(player, conditions -> true);
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(Optional<ContextAwarePredicate> player) implements SimpleInstance {
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(DeeperDownPortalOpeningCriterion.Conditions::player)
		).apply(instance, DeeperDownPortalOpeningCriterion.Conditions::new));
	}
}
