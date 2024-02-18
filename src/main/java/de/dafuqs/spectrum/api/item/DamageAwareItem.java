package de.dafuqs.spectrum.api.item;

import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;

public interface DamageAwareItem {
	
	void onItemEntityDamaged(DamageSource source, float amount, ItemEntity itemEntity);
	
}
