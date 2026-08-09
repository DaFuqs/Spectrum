package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.mixin.client.accessors.*;
import de.dafuqs.spectrum.sound.*;
import net.minecraft.client.resources.sounds.*;
import net.minecraft.client.sounds.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.At.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(SoundEngine.class)
public abstract class SoundSystemMixin {
	
	@Inject(method = "tickNonPaused", at = @At(value = "INVOKE", target = "net/minecraft/client/Options.getSoundSourceVolume (Lnet/minecraft/sounds/SoundSource;)F"))
	public void reverb$tick(CallbackInfo ci, @Local ChannelAccess.ChannelHandle sourceManager, @Local SoundInstance soundInstance) {
		sourceManager.execute(source -> DimensionReverb.SourceEffects.tick(soundInstance, ((SourceAccessor) source).getSource()));
	}
	
	@Inject(method = "play", at = @At(value = "INVOKE", target = "net/minecraft/client/sounds/ChannelAccess$ChannelHandle.execute (Ljava/util/function/Consumer;)V", ordinal = 0, shift = Shift.AFTER))
	public void reverb$play(SoundInstance soundInstance, CallbackInfo ci, @Local ChannelAccess.ChannelHandle sourceManager) {
		sourceManager.execute(source -> DimensionReverb.SourceEffects.tick(soundInstance, ((SourceAccessor) source).getSource()));
	}
	
	@Inject(method = "reload", at = @At("TAIL"))
	public void reverb$reloadSounds(CallbackInfo ci) {
		DimensionReverb.SourceEffects.updateSlots();
	}
	
}