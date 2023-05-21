package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.items.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;
import net.minecraft.util.collection.*;

import java.util.*;

public class SpectrumPickaxeItem extends PickaxeItem implements Preenchanted {
	
	public SpectrumPickaxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
		super(material, attackDamage, attackSpeed, settings);
	}
	
	@Override
	public Map<Enchantment, Integer> getDefaultEnchantments() {
		return Map.of();
	}
	
	@Override
	public ItemStack getDefaultStack() {
		return getDefaultEnchantedStack(this);
	}
	
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		if (this.isIn(group)) {
			stacks.add(getDefaultEnchantedStack(this));
		}
	}
	
}