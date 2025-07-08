package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;

public class PedestalRecipeCalculatedCriterion extends SimpleCriterionTrigger<PedestalCraftingCriterion.Conditions> {
	
	public static final String NAME = "pedestal_recipe_calculated";
	
	public void trigger(ServerPlayer player, ItemStack itemStack, int experience, int durationTicks) {
		this.trigger(player, (conditions) -> conditions.matches(itemStack, experience, durationTicks));
	}
	
	@Override
	public Codec<PedestalCraftingCriterion.Conditions> codec() {
		return PedestalCraftingCriterion.Conditions.CODEC;
	}
}
