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
        if (DDDimension.DIMENSION_KEY.equals(entity.world.getRegistryKey())) {
            return original / 6F;
        }
        return original;
    }

}
