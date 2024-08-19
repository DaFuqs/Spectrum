package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin {

    @Shadow private @Nullable LivingEntity target;

    @Inject(method = "tickNewAi", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;getWorld()Lnet/minecraft/world/World;", ordinal = 0), cancellable = true)
    public void slowDownAIticks(CallbackInfo ci) {
        var entity = (MobEntity) (Object) this;

        if ((entity.hasStatusEffect(SpectrumStatusEffects.ETERNAL_SLUMBER) || entity.hasStatusEffect(SpectrumStatusEffects.FATAL_SLUMBER)) && !SleepStatusEffect.isImmuneish(entity)) {
            target = null;
            ci.cancel();
            return;
        }
        
        var potency = SleepStatusEffect.getGeneralSleepResistanceIfEntityHasSoporificEffect(entity);

        if (potency <= 0 || entity.getRandom().nextFloat() > Math.min(potency * 0.05, 0.3))
            return;

        if (entity.getRandom().nextFloat() < potency * 0.75)
            target = null;

        ci.cancel();
    }
}
