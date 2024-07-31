package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.sensor.NearestPlayersSensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NearestPlayersSensor.class)
public class NearestPlayerSensorMixin {

    @WrapOperation(method = "method_19098", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isInRange(Lnet/minecraft/entity/Entity;D)Z"))
    private static boolean modifyPlayerdetectionRange(LivingEntity instance, Entity entity, double v, Operation<Boolean> original) {
        if (entity instanceof LivingEntity living) {
            return instance.isInRange(living, 16 * SpectrumStatusEffects.getCalmingMultiplier(living, living.getStatusEffect(SpectrumStatusEffects.CALMING)));
        }
        return original.call(instance, entity, v);
    }
}
