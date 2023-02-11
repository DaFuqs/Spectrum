package de.dafuqs.spectrum.items.tools;

import de.dafuqs.arrowhead.api.ArrowheadCrossbow;
import de.dafuqs.spectrum.items.Preenchanted;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.util.Map;

public class BedrockCrossbowItem extends CrossbowItem implements Preenchanted, ArrowheadCrossbow {
	
	public BedrockCrossbowItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean isDamageable() {
		return false;
	}
	
	@Override
	public Map<Enchantment, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.QUICK_CHARGE, 4);
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
	
	@Override
	public float getProjectileVelocityModifier() {
		return 1.5F;
	}
	
	@Override
	public float getPullTimeModifier() {
		return 3.0F;
	}
	
	@Override
	public float getDivergenceMod() {
		return 0.8F;
	}
	
}