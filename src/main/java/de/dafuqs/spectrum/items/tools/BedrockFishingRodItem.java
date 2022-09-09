package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.items.Preenchanted;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumFluidTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.collection.DefaultedList;

import java.util.Map;

public class BedrockFishingRodItem extends SpectrumFishingRodItem implements Preenchanted {
	
	public BedrockFishingRodItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean isDamageable() {
		return false;
	}
	
	@Override
	public Map<Enchantment, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.LUCK_OF_THE_SEA, 4);
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
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}
	
	@Override
	public boolean canFishIn(FluidState fluidState) {
		return fluidState.isIn(FluidTags.WATER)
				|| fluidState.isIn(FluidTags.LAVA)
				|| fluidState.isIn(SpectrumFluidTags.MUD)
				|| fluidState.isIn(SpectrumFluidTags.LIQUID_CRYSTAL)
				|| fluidState.isIn(SpectrumFluidTags.MIDNIGHT_SOLUTION);
	}
	
}