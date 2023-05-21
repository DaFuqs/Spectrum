package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.items.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;

import java.util.*;

public class RazorFalchionItem extends SwordItem implements Preenchanted {
	
	public RazorFalchionItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
		super(toolMaterial, attackDamage, attackSpeed, settings);
	}
	
	@Override
	public Map<Enchantment, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.LOOTING, 3);
	}

	@Override
	public ItemStack getDefaultStack() {
		return getDefaultEnchantedStack(this);
	}
	
}
