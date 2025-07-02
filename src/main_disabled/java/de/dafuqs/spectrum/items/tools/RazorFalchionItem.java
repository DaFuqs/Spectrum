package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.item.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;

import java.util.*;

public class RazorFalchionItem extends SwordItem implements Preenchanted {
	
	public RazorFalchionItem(Tier toolMaterial, Properties settings) {
		super(toolMaterial, settings);
	}
	
	@Override
	public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.LOOTING, 3);
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return false;
	}
}
