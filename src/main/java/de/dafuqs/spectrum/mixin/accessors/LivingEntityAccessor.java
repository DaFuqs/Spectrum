package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
	@Accessor
	DamageSource getLastDamageSource();
	
	@Accessor
	void setLastDamageSource(DamageSource damageSource);
	
}
