package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;

import java.util.*;

public class HummingstoneHymnCriterion extends SimpleCriterionTrigger<HummingstoneHymnCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("hummingstone_hymn");
	
	public void trigger(ServerPlayer player, ServerLevel world, BlockPos pos) {
		this.trigger(player, (conditions) -> conditions.matches(world, pos));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			LocationPredicate locationPredicate
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<HummingstoneHymnCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(HummingstoneHymnCriterion.Conditions::player),
				LocationPredicate.CODEC.optionalFieldOf("location", LocationPredicate.Builder.location().build()).forGetter(Conditions::locationPredicate)
		).apply(instance, Conditions::new));
		
		public boolean matches(ServerLevel world, BlockPos pos) {
			return this.locationPredicate.matches(world, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5);
		}
		
	}
	
}
