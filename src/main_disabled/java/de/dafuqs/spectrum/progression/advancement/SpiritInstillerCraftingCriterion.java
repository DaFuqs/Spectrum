package de.dafuqs.spectrum.progression.advancement;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;

public class SpiritInstillerCraftingCriterion extends SimpleCriterionTrigger<FusionShrineCraftingCriterion.Conditions> {
	
	public static final ResourceLocation ID = SpectrumCommon.locate("crafted_with_spirit_instiller");
	
	public void trigger(ServerPlayer player, ItemStack itemStack, int experience) {
		this.trigger(player, (conditions) -> conditions.matches(itemStack, experience));
	}

	@Override
	public Codec<FusionShrineCraftingCriterion.Conditions> codec() {
		return FusionShrineCraftingCriterion.Conditions.CODEC;
	}
}