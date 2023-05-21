package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.client.*;
import net.minecraft.client.network.*;
import net.minecraft.network.packet.s2c.play.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import static de.dafuqs.spectrum.SpectrumCommon.*;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
	
	@Inject(method = "onSynchronizeTags", at = @At("TAIL"))
	public void spectrum$syncTags(SynchronizeTagsS2CPacket packet, CallbackInfo ci) {
		logInfo("Registering MultiBlocks on Client...");
		SpectrumMultiblocks.register();
		
		SpectrumColorProviders.resetToggleableProviders();
	}
	
}
