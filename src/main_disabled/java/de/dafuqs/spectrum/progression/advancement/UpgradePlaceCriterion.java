package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;

import java.util.*;

public class UpgradePlaceCriterion extends SimpleCriterionTrigger<UpgradePlaceCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("upgrade_place");
	
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
			Optional<MinMaxBounds.Ints> countRange,
			Optional<MinMaxBounds.Ints> speedRange,
			Optional<MinMaxBounds.Ints> experienceRange,
			Optional<MinMaxBounds.Ints> efficiencyRange,
			Optional<MinMaxBounds.Ints> yieldRange
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
				BlockPredicate.CODEC.optionalFieldOf("block").forGetter(Conditions::blockPredicate),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("count").forGetter(Conditions::countRange),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("speed_mod").forGetter(Conditions::speedRange),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("experience_mod").forGetter(Conditions::experienceRange),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("efficiency_mod").forGetter(Conditions::efficiencyRange),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("yield_mod").forGetter(Conditions::yieldRange)
		).apply(instance, Conditions::new));
		
		public boolean matches(ServerLevel world, BlockPos pos, int upgradeCount, Map<Upgradeable.UpgradeType, Integer> upgradeModifiers) {
			return (this.blockPredicate.isEmpty() || this.blockPredicate.get().matches(world, pos))
					&& (this.countRange.isEmpty() || this.countRange.get().matches(upgradeCount))
					&& (this.speedRange.isEmpty() || this.speedRange.get().matches(upgradeModifiers.get(Upgradeable.UpgradeType.SPEED)))
					&& (this.experienceRange.isEmpty() || this.experienceRange.get().matches(upgradeModifiers.get(Upgradeable.UpgradeType.EXPERIENCE)))
					&& (this.efficiencyRange.isEmpty() || this.efficiencyRange.get().matches(upgradeModifiers.get(Upgradeable.UpgradeType.EFFICIENCY)))
					&& (this.yieldRange.isEmpty() || this.yieldRange.get().matches(upgradeModifiers.get(Upgradeable.UpgradeType.YIELD)));
		}
	}
	
}