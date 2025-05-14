package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.material.*;

import java.util.*;

public class BedrockFishingRodItem extends SpectrumFishingRodItem implements Preenchanted {
	
	public BedrockFishingRodItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.LUCK_OF_THE_SEA, 4);
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}
	
	@Override
	public boolean canFishIn(FluidState fluidState) {
		return fluidState.is(SpectrumFluidTags.BEDROCK_ROD_FISHABLE_IN);
	}
	
	@Override
	public void spawnBobber(Player user, Level world, int luckOfTheSeaLevel, int waitTimeReductionTicks, int exuberanceLevel, int bigCatchLevel, int serendipityReelLevel, boolean inventoryInsertion, boolean shouldSmeltDrops) {
		world.addFreshEntity(new BedrockFishingBobberEntity(user, world, luckOfTheSeaLevel, waitTimeReductionTicks, exuberanceLevel, bigCatchLevel, serendipityReelLevel, inventoryInsertion, shouldSmeltDrops));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.bedrock_fishing_rod.tooltip").withStyle(ChatFormatting.GRAY));
	}
	
}
