package de.dafuqs.spectrum.interfaces;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;

public interface ArmorWithHitEffect {
	
	void onHit(ItemStack itemStack, DamageSource source, LivingEntity targetEntity, float amount);
	
}
