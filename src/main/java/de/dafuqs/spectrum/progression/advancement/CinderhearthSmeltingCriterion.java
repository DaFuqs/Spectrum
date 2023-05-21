package de.dafuqs.spectrum.progression.advancement;

import com.google.gson.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.item.*;
import net.minecraft.predicate.*;
import net.minecraft.predicate.entity.*;
import net.minecraft.predicate.item.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;

import java.util.*;

public class CinderhearthSmeltingCriterion extends AbstractCriterion<CinderhearthSmeltingCriterion.Conditions> {
	
	static final Identifier ID = SpectrumCommon.locate("cinderhearth_smelting");
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
	public CinderhearthSmeltingCriterion.Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		ItemPredicate input = ItemPredicate.fromJson(jsonObject.get("input"));
		ItemPredicate output = ItemPredicate.fromJson(jsonObject.get("output"));
		NumberRange.IntRange experienceRange = NumberRange.IntRange.fromJson(jsonObject.get("gained_experience"));
		NumberRange.IntRange speedMultiplierRange = NumberRange.IntRange.fromJson(jsonObject.get("speed_multiplier"));
		NumberRange.IntRange yieldMultiplierRange = NumberRange.IntRange.fromJson(jsonObject.get("yield_multiplier"));
		NumberRange.IntRange efficiencyMultiplierRange = NumberRange.IntRange.fromJson(jsonObject.get("efficiency_multiplier"));
		NumberRange.IntRange experienceMultiplierRange = NumberRange.IntRange.fromJson(jsonObject.get("experience_multiplier"));
		
		return new CinderhearthSmeltingCriterion.Conditions(extended, input, output, experienceRange, speedMultiplierRange, yieldMultiplierRange, efficiencyMultiplierRange, experienceMultiplierRange);
	}

	public void trigger(ServerPlayerEntity player, ItemStack input, List<ItemStack> outputs, int experience, Upgradeable.UpgradeHolder upgrades) {
		this.trigger(player, (conditions) -> conditions.matches(input, outputs, experience, upgrades));
	}

	public static class Conditions extends AbstractCriterionConditions {

		private final ItemPredicate input;
		private final ItemPredicate output;
		private final NumberRange.IntRange experienceRange;
		private final NumberRange.IntRange speedMultiplierRange;
		private final NumberRange.IntRange yieldMultiplierRange;
		private final NumberRange.IntRange efficiencyMultiplierRange;
		private final NumberRange.IntRange experienceMultiplierRange;

		public Conditions(EntityPredicate.Extended player, ItemPredicate input, ItemPredicate output, NumberRange.IntRange experienceRange, NumberRange.IntRange speedMultiplierRange, NumberRange.IntRange yieldMultiplierRange, NumberRange.IntRange efficiencyMultiplierRange, NumberRange.IntRange experienceMultiplierRange) {
			super(ID, player);
			this.input = input;
			this.output = output;
			this.experienceRange = experienceRange;
			this.speedMultiplierRange = speedMultiplierRange;
			this.yieldMultiplierRange = yieldMultiplierRange;
			this.efficiencyMultiplierRange = efficiencyMultiplierRange;
			this.experienceMultiplierRange = experienceMultiplierRange;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("input", this.input.toJson());
			jsonObject.add("output", this.output.toJson());
			jsonObject.add("gained_experience", this.experienceRange.toJson());
			jsonObject.add("speed_multiplier", this.speedMultiplierRange.toJson());
			jsonObject.add("yield_multiplier", this.yieldMultiplierRange.toJson());
			jsonObject.add("efficiency_multiplier", this.efficiencyMultiplierRange.toJson());
			jsonObject.add("experience_multiplier", this.experienceMultiplierRange.toJson());
			return jsonObject;
		}

		public boolean matches(ItemStack input, List<ItemStack> outputs, int experience, Upgradeable.UpgradeHolder upgrades) {
			if (!this.input.test(input)) {
				return false;
			}
			if (!this.experienceRange.test(experience)) {
				return false;
			}
			if (!this.speedMultiplierRange.test(upgrades.getRawValue(Upgradeable.UpgradeType.SPEED))) {
				return false;
			}
			if (!this.yieldMultiplierRange.test(upgrades.getRawValue(Upgradeable.UpgradeType.YIELD))) {
				return false;
			}
			if (!this.efficiencyMultiplierRange.test(upgrades.getRawValue(Upgradeable.UpgradeType.EFFICIENCY))) {
				return false;
			}
			if (!this.experienceMultiplierRange.test(upgrades.getRawValue(Upgradeable.UpgradeType.EXPERIENCE))) {
				return false;
			}
			if (this.output == ItemPredicate.ANY) {
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
