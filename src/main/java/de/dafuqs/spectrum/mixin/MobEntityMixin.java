package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import de.dafuqs.spectrum.status_effects.SleepStatusEffect;
import de.dafuqs.spectrum.status_effects.SpectrumStatusEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin {

    @Shadow private @Nullable LivingEntity target;

    @Shadow protected abstract void mobTick();

    @Inject(method = "tickNewAi", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;getWorld()Lnet/minecraft/world/World;", ordinal = 0), cancellable = true)
    public void slowDownAIticks(CallbackInfo ci) {
        var entity = (MobEntity) (Object) this;

        if (entity.hasStatusEffect(SpectrumStatusEffects.ETERNAL_SLUMBER) || entity.hasStatusEffect(SpectrumStatusEffects.FATAL_SLUMBER)) {
            target = null;
            ci.cancel();
            return;
        }

        var effect = entity.getStatusEffect(SpectrumStatusEffects.SOMNOLENCE);

        if (effect == null)
            return;

        var potency = SleepStatusEffect.getSleepVulnerability(effect, entity);

        if (potency <= 0 || entity.getRandom().nextFloat() > Math.min(potency * 0.05, 0.3))
            return;

        if (entity.getRandom().nextFloat() < potency * 0.75)
            target = null;

        ci.cancel();
    }
}
