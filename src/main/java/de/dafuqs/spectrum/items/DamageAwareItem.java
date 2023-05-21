package de.dafuqs.spectrum.items;

import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;

public interface DamageAwareItem {
	
	void onItemEntityDamaged(DamageSource source, float amount, ItemEntity itemEntity);
	
}
