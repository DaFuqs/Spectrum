package de.dafuqs.spectrum.api.item;

import net.minecraft.entity.*;

public interface TickAwareItem {
	
	void onItemEntityTicked(ItemEntity itemEntity);
	
}
