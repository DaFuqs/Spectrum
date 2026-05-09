package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;
import javax.annotation.*;

public class SpiritInstillerCraftingCriterion extends SimpleCriterionTrigger<FusionShrineCraftingCriterion.Conditions> {
	
	public static final String NAME = "crafted_with_spirit_instiller";
	
	public void trigger(ServerPlayer player, ItemStack itemStack, int experience) {
		this.trigger(player, (conditions) -> conditions.matches(itemStack, experience));
	}
	
	@Override
	public Codec<FusionShrineCraftingCriterion.Conditions> codec() {
		return FusionShrineCraftingCriterion.Conditions.CODEC;
	}
}