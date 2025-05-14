package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(Fox.class)
public class FoxEntityMixin {

    @ModifyReturnValue(method = "isSleeping()Z", at = @At("RETURN"))
    public boolean spectrum$forceFoxSleepingState(boolean original) {
        if (original)
            return true;

        var entity = (LivingEntity) (Object) this;
		return entity.hasEffect(SpectrumStatusEffects.ETERNAL_SLUMBER) || entity.hasEffect(SpectrumStatusEffects.FATAL_SLUMBER);
    }
}
