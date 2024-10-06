package de.dafuqs.spectrum.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Accessor
    DamageSource getLastDamageSource();

    @Accessor("lastDamageSource")
    public void setLastDamageSource(DamageSource damageSource);
}
