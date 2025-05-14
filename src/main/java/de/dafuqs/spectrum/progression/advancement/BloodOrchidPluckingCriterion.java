package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;

import java.util.*;

public class BloodOrchidPluckingCriterion extends SimpleCriterionTrigger<BloodOrchidPluckingCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("blood_orchid_plucking");
	
	public void trigger(ServerPlayer player) {
		this.trigger(player, conditions -> true);
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Conditions::player)
		).apply(instance, BloodOrchidPluckingCriterion.Conditions::new));
	}
}
