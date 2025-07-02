package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;

import java.util.*;

public class FluidDippingCriterion extends SimpleCriterionTrigger<FluidDippingCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("fluid_dipping");
	
	public void trigger(ServerPlayer player, ServerLevel world, BlockPos pos, ItemStack previousStack, ItemStack targetStack) {
		this.trigger(player, (conditions) -> conditions.matches(world, pos, previousStack, targetStack));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			FluidPredicate fluidPredicate,
			ItemPredicate previousStackPredicate,
			ItemPredicate targetStackPredicate
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				FluidPredicate.CODEC.optionalFieldOf("fluid", FluidPredicate.Builder.fluid().build()).forGetter(Conditions::fluidPredicate),
				ItemPredicate.CODEC.optionalFieldOf("source_stack", ItemPredicate.Builder.item().build()).forGetter(Conditions::previousStackPredicate),
				ItemPredicate.CODEC.optionalFieldOf("target_stack", ItemPredicate.Builder.item().build()).forGetter(Conditions::targetStackPredicate)
		).apply(instance, Conditions::new));
		
		public boolean matches(ServerLevel world, BlockPos pos, ItemStack previousStack, ItemStack targetStack) {
			return this.fluidPredicate.matches(world, pos) && this.previousStackPredicate.test(previousStack) && this.targetStackPredicate.test(targetStack);
		}
	}
	
}
