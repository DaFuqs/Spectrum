package de.dafuqs.spectrum.api.item;

import net.minecraft.world.entity.item.*;

public interface TickAwareItem {
	
	void onItemEntityTicked(ItemEntity itemEntity);
	
}
