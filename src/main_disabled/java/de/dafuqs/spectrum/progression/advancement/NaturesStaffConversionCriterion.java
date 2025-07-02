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

public class NaturesStaffConversionCriterion extends SimpleCriterionTrigger<NaturesStaffConversionCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("natures_staff_conversion");
	
	public void trigger(ServerPlayer player, BlockState sourceBlockState, BlockState targetBlockState) {
		this.trigger(player, (conditions) -> conditions.matches(sourceBlockState, targetBlockState));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			Optional<Block> sourceBlock,
			StatePropertiesPredicate sourceBlockState,
			Optional<Block> targetBlock,
			StatePropertiesPredicate targetBlockState
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				BuiltInRegistries.BLOCK.byNameCodec().optionalFieldOf("source_block").forGetter(Conditions::sourceBlock),
				StatePropertiesPredicate.CODEC.optionalFieldOf("source_state", new StatePropertiesPredicate(List.of())).forGetter(Conditions::sourceBlockState),
				BuiltInRegistries.BLOCK.byNameCodec().optionalFieldOf("target_block").forGetter(Conditions::targetBlock),
				StatePropertiesPredicate.CODEC.optionalFieldOf("target_state", new StatePropertiesPredicate(List.of())).forGetter(Conditions::targetBlockState)
		).apply(instance, Conditions::new));
		
		public boolean matches(BlockState sourceBlockState, BlockState targetBlockState) {
			if (this.sourceBlock.isPresent() && !sourceBlockState.is(this.sourceBlock.get())) {
				return false;
			}
			if (!this.sourceBlockState.matches(sourceBlockState)) {
				return false;
			}
			if (this.targetBlock.isPresent() && !targetBlockState.is(this.targetBlock.get())) {
				return false;
			} else {
				return this.targetBlockState.matches(targetBlockState);
			}
		}
		
	}
	
}
