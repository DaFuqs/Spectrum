package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.sensor.NearestLivingEntitiesSensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(NearestLivingEntitiesSensor.class)
public abstract class NearestLivingEntitiesSensorMixin<T extends LivingEntity> {

    @Shadow protected abstract int getHorizontalExpansion();

    @ModifyExpressionValue(method = "sense", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getEntitiesByClass(Ljava/lang/Class;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;"))
    private List<LivingEntity> capRangeForCalming(List<LivingEntity> original, @Local(argsOnly = true) T caller) {
        for (int i = 0; i < original.size(); i++) {
            var entity = original.get(i);
            var calming = entity.getStatusEffect(SpectrumStatusEffects.CALMING);

            if (calming == null)
                continue;

            if (!caller.isInRange(entity, getHorizontalExpansion() * SpectrumStatusEffects.getCalmingMultiplier(calming)))
                original.remove(entity);
        }
        return original;
    }
}
