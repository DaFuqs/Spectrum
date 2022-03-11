package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.registries.SpectrumDefaultEnchantments;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;

public class BedrockCrossbowItem extends CrossbowItem {

	public BedrockCrossbowItem(Settings settings) {
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