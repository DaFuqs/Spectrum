package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import de.dafuqs.spectrum.status_effects.SleepStatusEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Brain.class)
public abstract class BrainMixin<E extends LivingEntity> { ;

    @Shadow public abstract void doExclusively(Activity activity);

    @Shadow public abstract <U> void forget(MemoryModuleType<U> type);

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void slowDownBrainTicks(ServerWorld world, E entity, CallbackInfo ci) {
        if (entity.hasStatusEffect(SpectrumStatusEffects.ETERNAL_SLUMBER) || entity.hasStatusEffect(SpectrumStatusEffects.FATAL_SLUMBER)) {
            ci.cancel();
            return;
        }

        var effect = entity.getStatusEffect(SpectrumStatusEffects.SOMNOLENCE);

        if (effect == null)
            return;

        var potency = SleepStatusEffect.getSleepVulnerability(effect, entity);

        if (potency <= 0 || entity.getRandom().nextFloat() > Math.min(potency * 0.05, 0.3))
            return;

        if (entity.getRandom().nextFloat() < potency * 0.5) {
            forget(MemoryModuleType.ANGRY_AT);
            doExclusively(Activity.REST);
        }

        ci.cancel();
    }
}
