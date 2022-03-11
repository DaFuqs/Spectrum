package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.registries.SpectrumDefaultEnchantments;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;

public class BedrockBowItem extends BowItem {

	public BedrockBowItem(Settings settings) {
		super(settings);
	}

	@Override
	public boolean isDamageable() {
		return false;
	}
	
	@Override
	public ItemStack getDefaultStack() {
		return SpectrumDefaultEnchantments.getDefaultEnchantedStack(this);
	}

}