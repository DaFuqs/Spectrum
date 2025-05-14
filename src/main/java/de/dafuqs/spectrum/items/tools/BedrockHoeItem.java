package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.item.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;

import java.util.*;

public class BedrockHoeItem extends HoeItem implements Preenchanted {
	
	public BedrockHoeItem(Tier material, Properties settings) {
		super(material, settings);
	}
	
	@Override
	public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.FORTUNE, 4);
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}

}
