package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;

import java.util.*;

public class SlimeSizingCriterion extends SimpleCriterionTrigger<SlimeSizingCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("slime_sizing");
	
	public void trigger(ServerPlayer player, int size) {
		this.trigger(player, (conditions) -> conditions.matches(size));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			MinMaxBounds.Ints sizeRange
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("size", MinMaxBounds.Ints.ANY).forGetter(Conditions::sizeRange)
		).apply(instance, Conditions::new));
		
		public boolean matches(int size) {
			return this.sizeRange.matches(size);
		}
	}
	
}
