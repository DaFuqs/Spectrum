package de.dafuqs.spectrum.items;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;

public interface TickAwareItem {
	
	void onItemEntityTicked(ItemEntity itemEntity);
	
}
