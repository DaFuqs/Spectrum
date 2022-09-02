package de.dafuqs.spectrum.items;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;

public interface DamageAwareItem {
	
	void onItemEntityDamaged(DamageSource source, float amount, ItemEntity itemEntity);
	
}
