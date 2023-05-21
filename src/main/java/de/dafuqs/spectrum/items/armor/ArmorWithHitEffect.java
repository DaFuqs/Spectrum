package de.dafuqs.spectrum.items.armor;

import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.item.*;

public interface ArmorWithHitEffect {
	
	void onHit(ItemStack itemStack, DamageSource source, LivingEntity targetEntity, float amount);
	
}
