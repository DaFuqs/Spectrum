package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.server.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(FireBlock.class)
public class FireBlockMixin {
	
	@ModifyExpressionValue(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;isRaining()Z", ordinal = 0))
	public boolean spectrum$extinguishInPermanentRain(boolean original, @Local ServerWorld world) {
		if (world.getRegistryKey().equals(SpectrumDimensions.DIMENSION_KEY)) {
			return true;
		}
		return original;
	}
	
	@ModifyExpressionValue(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;isRaining()Z", ordinal = 1))
	public boolean spectrum$assuageInPermanentRain(boolean original, @Local ServerWorld world) {
		if (world.getRegistryKey().equals(SpectrumDimensions.DIMENSION_KEY)) {
			return true;
		}
		return original;
	}
}
