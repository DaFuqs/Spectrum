package de.dafuqs.spectrum.helpers.enchantments;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.item.*;

public class ImprovedCriticalHelper {
	
	public static float getAddtionalCritDamageMultiplier(int improvedCriticalLevel) {
		return SpectrumCommon.CONFIG.ImprovedCriticalExtraDamageMultiplierPerLevel * improvedCriticalLevel;
	}
	
	public static float getAddtionalCritDamageMultiplier(HolderLookup.Provider lookup, ItemStack stack) {
		return getAddtionalCritDamageMultiplier(SpectrumEnchantmentHelper.getLevel(lookup, SpectrumEnchantments.IMPROVED_CRITICAL, stack));
	}
	
}
