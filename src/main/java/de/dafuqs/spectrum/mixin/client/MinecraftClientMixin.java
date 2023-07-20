package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.sound.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
	
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getMusic()Ljava/util/Optional;"), method = "getMusicType()Lnet/minecraft/sound/MusicSound;", cancellable = true)
	public void spectrum$getMusicType(CallbackInfoReturnable<MusicSound> cir) {
		MinecraftClient thisClient = (MinecraftClient) (Object) this;
		if (thisClient.player.world.getRegistryKey() == SpectrumCommon.DEEPER_DOWN) {
			if (Support.hasPlayerFinishedMod(MinecraftClient.getInstance().player)) {
				cir.setReturnValue(SpectrumMusicType.SPECTRUM_THEME);
			} else {
				cir.setReturnValue(SpectrumMusicType.DEEPER_DOWN_THEME);
			}
		}
	}
	
}