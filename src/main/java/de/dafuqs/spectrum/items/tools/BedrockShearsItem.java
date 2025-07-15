package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.item.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;

import java.util.*;

// Waiting for https://github.com/FabricMC/fabric/pull/1804
// Who's still waiting this with me in 2025??
public class BedrockShearsItem extends ShearsItem implements Preenchanted {
	
	public BedrockShearsItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.EFFICIENCY, 6);
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}
	
}