package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.deeper_down.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.network.*;
import net.minecraft.sound.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
	
	@Shadow
	@Nullable
	public ClientPlayerEntity player;
	
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getRegistryKey()Lnet/minecraft/util/registry/RegistryKey;"), method = "getMusicType()Lnet/minecraft/sound/MusicSound;", cancellable = true)
	public void spectrum$getMusicType(CallbackInfoReturnable<MusicSound> cir) {
		if (player.world.getRegistryKey() == DDDimension.DIMENSION_KEY) {
			if (Support.hasPlayerFinishedMod(player)) {
				cir.setReturnValue(SpectrumMusicType.SPECTRUM_THEME);
			} else {
				cir.setReturnValue(SpectrumMusicType.DEEPER_DOWN_THEME);
			}
		}
	}
	
}