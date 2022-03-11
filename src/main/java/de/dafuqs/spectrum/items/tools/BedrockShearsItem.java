package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.registries.SpectrumDefaultEnchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;

// Waiting for https://github.com/FabricMC/fabric/pull/1804
public class BedrockShearsItem extends ShearsItem {

	public BedrockShearsItem(Settings settings) {
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