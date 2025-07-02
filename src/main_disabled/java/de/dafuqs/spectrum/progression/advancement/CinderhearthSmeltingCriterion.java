package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;

import java.util.*;

public class CinderhearthSmeltingCriterion extends SimpleCriterionTrigger<CinderhearthSmeltingCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("cinderhearth_smelting");
	
	public void trigger(ServerPlayer player, ItemStack input, List<ItemStack> outputs, int experience, Upgradeable.UpgradeHolder upgrades) {
		this.trigger(player, (conditions) -> conditions.matches(input, outputs, experience, upgrades));
	}
	
	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(
			Optional<ContextAwarePredicate> player,
			ItemPredicate input,
			ItemPredicate output,
			MinMaxBounds.Ints gainedExperience,
			MinMaxBounds.Ints speedMultiplier,
			MinMaxBounds.Ints yieldMultiplier,
			MinMaxBounds.Ints efficiencyMultiplier,
			MinMaxBounds.Ints experienceMultiplier
	) implements SimpleCriterionTrigger.SimpleInstance {
		
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(CinderhearthSmeltingCriterion.Conditions::player),
				ItemPredicate.CODEC.optionalFieldOf("input", ItemPredicate.Builder.item().build()).forGetter(Conditions::input),
				ItemPredicate.CODEC.optionalFieldOf("output", ItemPredicate.Builder.item().build()).forGetter(Conditions::output),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("gained_experience", MinMaxBounds.Ints.ANY).forGetter(Conditions::gainedExperience),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("speed_multiplier", MinMaxBounds.Ints.ANY).forGetter(Conditions::speedMultiplier),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("yield_multiplier", MinMaxBounds.Ints.ANY).forGetter(Conditions::yieldMultiplier),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("efficiency_multiplier", MinMaxBounds.Ints.ANY).forGetter(Conditions::efficiencyMultiplier),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("experience_multiplier", MinMaxBounds.Ints.ANY).forGetter(Conditions::experienceMultiplier)
		).apply(instance, CinderhearthSmeltingCriterion.Conditions::new));
		
		public boolean matches(ItemStack input, List<ItemStack> outputs, int experience, Upgradeable.UpgradeHolder upgrades) {
			if (!this.input.test(input)) {
				return false;
			}
			if (!this.gainedExperience.matches(experience)) {
				return false;
			}
			if (!this.speedMultiplier.matches(upgrades.getRawValue(Upgradeable.UpgradeType.SPEED))) {
				return false;
			}
			if (!this.yieldMultiplier.matches(upgrades.getRawValue(Upgradeable.UpgradeType.YIELD))) {
				return false;
			}
			if (!this.efficiencyMultiplier.matches(upgrades.getRawValue(Upgradeable.UpgradeType.EFFICIENCY))) {
				return false;
			}
			if (!this.experienceMultiplier.matches(upgrades.getRawValue(Upgradeable.UpgradeType.EXPERIENCE))) {
				return false;
			}
			if (this.output.equals(ItemPredicate.Builder.item().build())) {
				return true; // empty output predicate
			}
			for (ItemStack output : outputs) {
				if (this.output.test(output)) {
					return true;
				}
			}
			return false;
		}
		
	}
	
}
