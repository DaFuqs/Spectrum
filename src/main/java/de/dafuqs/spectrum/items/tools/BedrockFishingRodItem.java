package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.items.Preenchanted;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class BedrockFishingRodItem extends FishingRodItem implements Preenchanted {

	public BedrockFishingRodItem(Settings settings) {
		super(settings);
	}

	@Override
	public boolean isDamageable() {
		return false;
	}
	
	@Override
	public Map<Enchantment, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.LUCK_OF_THE_SEA, 3);
	}
	
	@Override
	public ItemStack getDefaultStack() {
		return getDefaultEnchantedStack(this);
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}
	
}