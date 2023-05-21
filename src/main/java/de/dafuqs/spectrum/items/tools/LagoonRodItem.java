package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.item.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.player.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class LagoonRodItem extends SpectrumFishingRodItem implements Preenchanted {
	
	public LagoonRodItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public Map<Enchantment, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.LURE, 3);
	}
	
	@Override
	public ItemStack getDefaultStack() {
		return getDefaultEnchantedStack(this);
	}
	
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		if (this.isIn(group)) {
			stacks.add(getDefaultEnchantedStack(this));
		}
	}
	
	@Override
	public boolean canFishIn(FluidState fluidState) {
		return fluidState.isIn(SpectrumFluidTags.LAGOON_ROD_FISHABLE_IN);
	}
	
	@Override
	public void spawnBobber(PlayerEntity user, World world, int luckOfTheSeaLevel, int lureLevel, int exuberanceLevel, int bigCatchLevel, boolean inventoryInsertion, boolean foundry) {
		world.spawnEntity(new LagoonFishingBobberEntity(user, world, luckOfTheSeaLevel, lureLevel, exuberanceLevel, bigCatchLevel, inventoryInsertion, foundry));
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.lagoon_rod.tooltip").formatted(Formatting.GRAY));
	}
	
}