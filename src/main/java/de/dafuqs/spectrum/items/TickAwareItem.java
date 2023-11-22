package de.dafuqs.spectrum.items;

import net.minecraft.entity.*;

public interface TickAwareItem {
	
	void onItemEntityTicked(ItemEntity itemEntity);
	
}
