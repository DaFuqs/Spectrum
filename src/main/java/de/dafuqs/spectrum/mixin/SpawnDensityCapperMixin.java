package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(LocalMobCapCalculator.class)
public class SpawnDensityCapperMixin {
	
	@WrapOperation(method = "canSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LocalMobCapCalculator$MobCounts;canSpawn(Lnet/minecraft/world/entity/MobCategory;)Z"))
	public boolean reduceSpawnCap(LocalMobCapCalculator.MobCounts instance, MobCategory spawnGroup, Operation<Boolean> original, @Local LocalMobCapCalculator.MobCounts densityCap, @Local ServerPlayer serverPlayerEntity) {
		var calming = serverPlayerEntity.getEffect(SpectrumMobEffects.CALMING);
		
		if (calming != null) {
			return densityCap == null || ((DensityCapAccessor) densityCap).getCounts().getOrDefault(spawnGroup, 0) < spawnGroup.getMaxInstancesPerChunk() - ((calming.getAmplifier() + 1) * 2.5);
		}
		
		return original.call(instance, spawnGroup);
	}
}
