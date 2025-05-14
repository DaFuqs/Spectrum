package de.dafuqs.spectrum.api.damage_type;

import net.minecraft.world.item.*;

import java.util.*;

public interface StackTracking {
	
	Optional<ItemStack> spectrum$getTrackedStack();
	
	void spectrum$setTrackedStack(ItemStack stack);
}
