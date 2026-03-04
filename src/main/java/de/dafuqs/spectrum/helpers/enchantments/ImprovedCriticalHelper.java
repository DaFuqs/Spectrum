package de.dafuqs.spectrum.helpers.enchantments;

import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.item.*;

public class ImprovedCriticalHelper {
	
	public static float getAdditionalCritDamageMultiplier(int improvedCriticalLevel) {
		return SpectrumConfig.CONFIG.ImprovedCriticalExtraDamageMultiplierPerLevel.get() * improvedCriticalLevel;
	}
	
	public static float getAdditionalCritDamageMultiplier(HolderLookup.Provider lookup, ItemStack stack) {
		return getAdditionalCritDamageMultiplier(SpectrumEnchantmentHelper.getLevel(lookup, SpectrumEnchantmentKeys.IMPROVED_CRITICAL, stack));
	}
	
}
