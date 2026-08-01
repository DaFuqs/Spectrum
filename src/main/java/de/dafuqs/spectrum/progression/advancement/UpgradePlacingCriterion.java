package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;

import java.util.*;

public class UpgradePlacingCriterion extends SimpleCriterionTrigger<UpgradePlacingCriterion.Conditions> {
	
	public static final String NAME = "upgrade_place";
	
	public void trigger(ServerPlayer player, ServerLevel world, BlockPos pos, int upgradeCount, Map<Upgradeable.UpgradeType, Integer> upgradeModifiers) {
		this.trigger(player, (conditions) -> conditions.matches(world, pos, upgradeCount, upgradeModifiers));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			Optional<BlockPredicate> blockPredicate,
			MinMaxBounds.Ints countRange,
			MinMaxBounds.Ints speedRange,
			MinMaxBounds.Ints experienceRange,
			MinMaxBounds.Ints efficiencyRange,
			MinMaxBounds.Ints yieldRange
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				BlockPredicate.CODEC.optionalFieldOf("block").forGetter(Conditions::blockPredicate),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("count", MinMaxBounds.Ints.ANY).forGetter(Conditions::countRange),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("speed_mod", MinMaxBounds.Ints.ANY).forGetter(Conditions::speedRange),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("experience_mod", MinMaxBounds.Ints.ANY).forGetter(Conditions::experienceRange),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("efficiency_mod", MinMaxBounds.Ints.ANY).forGetter(Conditions::efficiencyRange),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("yield_mod", MinMaxBounds.Ints.ANY).forGetter(Conditions::yieldRange)
		).apply(instance, Conditions::new));
		
		public boolean matches(ServerLevel world, BlockPos pos, int upgradeCount, Map<Upgradeable.UpgradeType, Integer> upgradeModifiers) {
			return (this.blockPredicate.isEmpty() || this.blockPredicate.get().matches(world, pos))
					&& this.countRange.matches(upgradeCount)
					&& this.speedRange.matches(upgradeModifiers.get(Upgradeable.UpgradeType.SPEED))
					&& this.experienceRange.matches(upgradeModifiers.get(Upgradeable.UpgradeType.EXPERIENCE))
					&& this.efficiencyRange.matches(upgradeModifiers.get(Upgradeable.UpgradeType.EFFICIENCY))
					&& this.yieldRange.matches(upgradeModifiers.get(Upgradeable.UpgradeType.YIELD));
		}
	}
	
}