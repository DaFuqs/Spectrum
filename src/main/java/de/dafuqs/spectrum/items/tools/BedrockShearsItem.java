package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.items.Preenchanted;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.util.collection.DefaultedList;

import java.util.Map;

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