package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class InertiaUsedCriterion extends SimpleCriterionTrigger<InertiaUsedCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("inertia_used");
	
	public void trigger(ServerPlayer player, BlockState state, long amount) {
		this.trigger(player, (conditions) -> conditions.matches(state, amount));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			Optional<Block> block,
			StatePropertiesPredicate statePredicate,
			LongRange amount
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				BuiltInRegistries.BLOCK.byNameCodec().optionalFieldOf("block").forGetter(Conditions::block),
				StatePropertiesPredicate.CODEC.optionalFieldOf("state", new StatePropertiesPredicate(List.of())).forGetter(Conditions::statePredicate),
				LongRange.CODEC.optionalFieldOf("amount", LongRange.ANY).forGetter(Conditions::amount)
		).apply(instance, Conditions::new));
		
		public boolean matches(BlockState state, long amount) {
			if (this.block.isPresent() && !state.is(this.block.get())) {
				return false;
			} else {
				return this.statePredicate.matches(state) && this.amount.test(amount);
			}
		}
	}
}
