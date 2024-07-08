package de.dafuqs.spectrum.api.item;

import net.minecraft.entity.*;
import net.minecraft.item.*;

/**
 * Make sure to wrap your damage source with stack tracking or this won't work!
 */
public interface ArmorPiercingItem extends SplitDamageItem {
	
	float getDefenseMultiplier(LivingEntity target, ItemStack stack);
	
	float getToughnessMultiplier(LivingEntity target, ItemStack stack);
	
	float getProtReduction(LivingEntity target, ItemStack stack);
}
