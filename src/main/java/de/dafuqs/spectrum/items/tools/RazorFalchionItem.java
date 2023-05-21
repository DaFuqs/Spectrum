package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.items.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;
import net.minecraft.util.collection.*;

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
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		if (this.isIn(group)) {
			stacks.add(getDefaultEnchantedStack(this));
		}
	}
	
	@Override
	public ItemStack getDefaultStack() {
		return getDefaultEnchantedStack(this);
	}
	
}
