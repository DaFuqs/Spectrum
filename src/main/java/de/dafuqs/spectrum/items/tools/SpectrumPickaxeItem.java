package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.item.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;

import java.util.*;

public class SpectrumPickaxeItem extends PickaxeItem implements Preenchanted {
	
	public SpectrumPickaxeItem(Tier material, Properties settings) {
		super(material, settings);
	}
	
	@Override
	public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
		return Map.of();
	}
}
