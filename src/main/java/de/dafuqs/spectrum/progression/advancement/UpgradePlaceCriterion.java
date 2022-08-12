package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.JsonObject;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.upgrade.Upgradeable;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class UpgradePlaceCriterion extends AbstractCriterion<UpgradePlaceCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("upgrade_place");
	
	public static UpgradePlaceCriterion.Conditions create(BlockPredicate blockPredicate, NumberRange.IntRange countRange, NumberRange.FloatRange speedRange, NumberRange.FloatRange experienceRange, NumberRange.FloatRange efficiencyRange, NumberRange.FloatRange yieldRange) {
		return new UpgradePlaceCriterion.Conditions(EntityPredicate.Extended.EMPTY, blockPredicate, countRange, speedRange, experienceRange, efficiencyRange, yieldRange);
	}
	
	public Identifier getId() {
		return ID;
	}
	
	public UpgradePlaceCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		BlockPredicate blockPredicate = BlockPredicate.fromJson(jsonObject.get("block"));
		NumberRange.IntRange countRange = NumberRange.IntRange.fromJson(jsonObject.get("count"));
		NumberRange.FloatRange speedRange = NumberRange.FloatRange.fromJson(jsonObject.get("speed_mod"));
		NumberRange.FloatRange experienceRange = NumberRange.FloatRange.fromJson(jsonObject.get("experience_mod"));
		NumberRange.FloatRange efficiencyRange = NumberRange.FloatRange.fromJson(jsonObject.get("efficiency_mod"));
		NumberRange.FloatRange yieldRange = NumberRange.FloatRange.fromJson(jsonObject.get("yield_mod"));
		return new UpgradePlaceCriterion.Conditions(extended, blockPredicate, countRange, speedRange, experienceRange, efficiencyRange, yieldRange);
	}
	
	public void trigger(ServerPlayerEntity player, ServerWorld world, BlockPos pos, int upgradeCount, Map<Upgradeable.UpgradeType, Float> upgradeModifiers) {
		this.trigger(player, (conditions) -> conditions.matches(world, pos, upgradeCount, upgradeModifiers));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final BlockPredicate blockPredicate;
		private final NumberRange.IntRange countRange;
		private final NumberRange.FloatRange speedRange;
		private final NumberRange.FloatRange experienceRange;
		private final NumberRange.FloatRange efficiencyRange;
		private final NumberRange.FloatRange yieldRange;
		
		public Conditions(EntityPredicate.Extended player, BlockPredicate blockPredicate, NumberRange.IntRange countRange, NumberRange.FloatRange speedRange, NumberRange.FloatRange experienceRange, NumberRange.FloatRange efficiencyRange, NumberRange.FloatRange yieldRange) {
			super(ID, player);
			this.blockPredicate = blockPredicate;
			this.countRange = countRange;
			this.speedRange = speedRange;
			this.experienceRange = experienceRange;
			this.efficiencyRange = efficiencyRange;
			this.yieldRange = yieldRange;
		}
		
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("block", this.blockPredicate.toJson());
			jsonObject.add("count", this.countRange.toJson());
			jsonObject.add("speed_mod", this.speedRange.toJson());
			jsonObject.add("experience_mod", this.experienceRange.toJson());
			jsonObject.add("efficiency_mod", this.efficiencyRange.toJson());
			jsonObject.add("yield_mod", this.yieldRange.toJson());
			return jsonObject;
		}
		
		public boolean matches(ServerWorld world, BlockPos pos, int upgradeCount, Map<Upgradeable.UpgradeType, Float> upgradeModifiers) {
			return this.blockPredicate.test(world, pos)
				&& this.countRange.test(upgradeCount)
				&& this.speedRange.test(upgradeModifiers.get(Upgradeable.UpgradeType.SPEED))
				&& this.experienceRange.test(upgradeModifiers.get(Upgradeable.UpgradeType.EXPERIENCE))
				&& this.efficiencyRange.test(upgradeModifiers.get(Upgradeable.UpgradeType.EFFICIENCY))
				&& this.yieldRange.test(upgradeModifiers.get(Upgradeable.UpgradeType.YIELD));
		}
	}
	
}