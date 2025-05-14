package de.dafuqs.spectrum.items.tools;

import net.minecraft.core.component.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;

public class GlintlessPickaxe extends SpectrumPickaxeItem {
	
	public GlintlessPickaxe(Tier material, Properties settings) {
        super(material, settings);
    }

    @Override
	public boolean isFoil(ItemStack stack) {
        var defaults = getDefaultEnchantments();
		var comp = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
		for (var entry : comp.entrySet()) {
			var key = entry.getKey().unwrapKey();
			if (key.isEmpty()) continue;
			if (entry.getIntValue() > defaults.getOrDefault(key.get(), 0))
				return true;
		}
		return false;
    }
}
