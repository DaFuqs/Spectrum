package de.dafuqs.spectrum.api.item;

import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.item.*;

public interface DamageAwareItem {
	
	void onItemEntityDamaged(DamageSource source, float amount, ItemEntity itemEntity);
	
}
