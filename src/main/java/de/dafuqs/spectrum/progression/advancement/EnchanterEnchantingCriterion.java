package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;

public class EnchanterEnchantingCriterion extends SimpleCriterionTrigger<EnchanterCraftingCriterion.Conditions> {
	
	public static final String NAME = "enchanter_enchanting";
	
	public void trigger(ServerPlayer player, ItemStack itemStack, int spentExperience) {
		this.trigger(player, (conditions) -> conditions.matches(itemStack, spentExperience));
	}
	
	@Override
	public Codec<EnchanterCraftingCriterion.Conditions> codec() {
		return EnchanterCraftingCriterion.Conditions.CODEC;
	}
}
