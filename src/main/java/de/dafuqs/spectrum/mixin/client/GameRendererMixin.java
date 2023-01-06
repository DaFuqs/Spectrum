package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.deeper_down.*;
import net.minecraft.client.render.*;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @ModifyReturnValue(method = "getNightVisionStrength(Lnet/minecraft/entity/LivingEntity;F)F", at = @At("RETURN"))
    private static float spectrum$nerfNightVisionInDimension(float original, LivingEntity entity, float tickDelta) {
        if (entity.world.getRegistryKey().equals(DDDimension.DEEPER_DOWN_DIMENSION_KEY)) {
            return original / 8F;
        }
        return original;
    }

}
