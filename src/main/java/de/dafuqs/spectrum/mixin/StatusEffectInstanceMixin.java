package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumDamageTypes;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import de.dafuqs.spectrum.status_effects.SleepStatusEffect;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalEntityTypeTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StatusEffectInstance.class)
public abstract class StatusEffectInstanceMixin {

    @Shadow protected abstract boolean isActive();

    @Shadow public abstract StatusEffect getEffectType();

    @Inject(method = "update", at = @At(value = "RETURN"))
    public void spectrum$fatalSlumberKill(LivingEntity entity, Runnable overwriteCallback, CallbackInfoReturnable<Boolean> cir) {
        if (!isActive() && getEffectType() == SpectrumStatusEffects.FATAL_SLUMBER) {
            var damage = Float.MAX_VALUE;
            if (SleepStatusEffect.isImmuneish(entity)) {
                if (entity instanceof PlayerEntity) {
                    damage = entity.getHealth() * 0.95F;
                }
                else {
                    damage = entity.getMaxHealth() * 0.3F;
                }
            }
            entity.damage(SpectrumDamageTypes.sleep(entity.getWorld(), null), damage);
        }
    }
}
