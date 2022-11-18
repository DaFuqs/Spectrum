package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.registries.SpectrumMultiblocks;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.SynchronizeTagsS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static de.dafuqs.spectrum.SpectrumCommon.logInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onSynchronizeTags", at = @At("TAIL"))
    public void spectrum$syncTags(SynchronizeTagsS2CPacket packet, CallbackInfo ci) {
        logInfo("Registering MultiBlocks for client...");
        SpectrumMultiblocks.register();
    }

}
