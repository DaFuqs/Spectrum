package de.dafuqs.spectrum.items.tools;

import de.dafuqs.arrowhead.api.*;
import de.dafuqs.spectrum.api.item.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;

import java.util.*;

public class BedrockCrossbowItem extends CrossbowItem implements Preenchanted, ArrowheadCrossbow {
	
	public BedrockCrossbowItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
		return Map.of(Enchantments.QUICK_CHARGE, 4);
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}

	@Override
	public float getProjectileVelocityModifier(ItemStack stack) {
		return 1.5F;
	}

	@Override
	public float getPullTimeModifier(ItemStack stack) {
		return 3.0F;
	}

	@Override
	public float getDivergenceMod(ItemStack stack) {
		return 0.8F;
	}
	
}