package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.registries.*;
import net.neoforged.neoforge.common.*;
import net.neoforged.neoforge.common.extensions.*;
import net.neoforged.neoforge.fluids.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(IEntityExtension.class)
public interface IEntityExtensionMixin {
	
	@Shadow
	FluidType getEyeInFluidType();
	
	// fixes weirdness with entities like fish not being able to swim in liquid crystal
	// TODO: test and remove in MC 26.1 since Neo patched that in https://github.com/neoforged/NeoForge/commit/ad038e822a142901aeb33b0eedde0a892588b662
	@ModifyReturnValue(method = "isEyeInFluidType(Lnet/neoforged/neoforge/fluids/FluidType;)Z", at = @At("RETURN"))
	default boolean getEyeInFluidType(boolean original, FluidType type) {
		if(!original && type == NeoForgeMod.WATER_TYPE.value()) {
			return getEyeInFluidType() == SpectrumFluids.LIQUID_CRYSTAL_TYPE.get();
		}
		return original;
	}
	
}
