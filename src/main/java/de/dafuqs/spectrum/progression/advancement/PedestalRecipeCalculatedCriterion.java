package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;

public class PedestalRecipeCalculatedCriterion extends SimpleCriterionTrigger<PedestalCraftingCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("pedestal_recipe_calculated");
	
	public void trigger(ServerPlayer player, ItemStack itemStack, int experience, int durationTicks) {
		this.trigger(player, (conditions) -> conditions.matches(itemStack, experience, durationTicks));
	}
	
	@Override
	public Codec<PedestalCraftingCriterion.Conditions> codec() {
		return PedestalCraftingCriterion.Conditions.CODEC;
	}
}
