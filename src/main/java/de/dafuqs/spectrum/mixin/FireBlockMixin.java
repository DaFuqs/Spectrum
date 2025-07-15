package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.block.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(FireBlock.class)
public class FireBlockMixin {
	
	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "net/minecraft/server/level/ServerLevel.isRaining ()Z", ordinal = 0))
	public boolean spectrum$extinguishInPermanentRain(boolean original, @Local(argsOnly = true) ServerLevel world) {
		if (world.dimension().equals(SpectrumDimensions.DIMENSION_KEY)) {
			return true;
		}
		return original;
	}
	
	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "net/minecraft/server/level/ServerLevel.isRaining ()Z", ordinal = 1))
	public boolean spectrum$assuageInPermanentRain(boolean original, @Local(argsOnly = true) ServerLevel world) {
		if (world.dimension().equals(SpectrumDimensions.DIMENSION_KEY)) {
			return true;
		}
		return original;
	}
}
