package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import de.dafuqs.spectrum.mixin.accessors.DensityCapAccessor;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.SpawnDensityCapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpawnDensityCapper.class)
public class SpawnDensityCapperMixin {

    @WrapOperation(method = "canSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/SpawnDensityCapper$DensityCap;canSpawn(Lnet/minecraft/entity/SpawnGroup;)Z"))
    public boolean reduceSpawnCap(SpawnDensityCapper.DensityCap instance, SpawnGroup spawnGroup, Operation<Boolean> original, @Local SpawnDensityCapper.DensityCap densityCap, @Local ServerPlayerEntity serverPlayerEntity) {
        var calming = serverPlayerEntity.getStatusEffect(SpectrumStatusEffects.CALMING);

        if (calming != null) {
            return densityCap == null || ((DensityCapAccessor) densityCap).getSpawnGroupsToDensity().getOrDefault(spawnGroup, 0) < spawnGroup.getCapacity() - ((calming.getAmplifier() + 1) * 2.5);
        }

        return original.call(instance, spawnGroup);
    }
}
