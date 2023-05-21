package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.items.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;
import net.minecraft.util.collection.*;

import java.util.*;

// Waiting for https://github.com/FabricMC/fabric/pull/1804
public class BedrockShearsItem extends ShearsItem implements Preenchanted {
	
	public BedrockShearsItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean isDamageable() {
		return false;
	}
	
	@Override
	public Map<Enchantment, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.SILK_TOUCH, 1);
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
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}
	
}