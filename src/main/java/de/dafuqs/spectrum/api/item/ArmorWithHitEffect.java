package de.dafuqs.spectrum.api.item;

import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.item.*;

public interface ArmorWithHitEffect {
	
	void onHit(ItemStack itemStack, DamageSource source, LivingEntity targetEntity, float amount);
	
}
