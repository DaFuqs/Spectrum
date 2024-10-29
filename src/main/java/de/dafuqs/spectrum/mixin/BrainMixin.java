package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.server.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Brain.class)
public abstract class BrainMixin<E extends LivingEntity> {

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
        
        var scaling = SleepStatusEffect.getSleepScaling(entity);
        if (scaling <= 0 || entity.getRandom().nextFloat() > Math.min(scaling * 0.05, 0.3))
            return;
        
        if (entity.getRandom().nextFloat() < scaling * 0.5) {
            forget(MemoryModuleType.ANGRY_AT);
            doExclusively(Activity.REST);
        }

        ci.cancel();
    }
}
