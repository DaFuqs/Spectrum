package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FoxEntity.class)
public class FoxEntityMixin {

    @ModifyReturnValue(method = "isSleeping()Z", at = @At("RETURN"))
    public boolean spectrum$forceFoxSleepingState(boolean original) {
        if (original)
            return true;

        var entity = (LivingEntity) (Object) this;
        return entity.hasStatusEffect(SpectrumStatusEffects.ETERNAL_SLUMBER) || entity.hasStatusEffect(SpectrumStatusEffects.FATAL_SLUMBER);
    }
}
