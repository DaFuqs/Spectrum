package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.blocks.blocklikeentities.util.PostTickEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// This gets disabled if another mod is present, if you add something to this mixin make sure you account for that.
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin implements PostTickEntity {
    @Shadow protected abstract void sendMovementPackets();
    
    @Unique boolean spectrum$sendMovement = false;
    
    /**
     * Since the player can be moved by FloatingBlockEntity after ClientPlayerEntity.tick()
     * the call to sendMovementPackets() needs to be delayed till after all FloatingBlockEntities have ticked
     */
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendMovementPackets()V"))
    void redirectSendMovementPackets(ClientPlayerEntity clientPlayerEntity) {
        spectrum$sendMovement = true;
    }

    @Override
    public void spectrum$postTick() {
        if (spectrum$sendMovement) {
            sendMovementPackets();
            spectrum$sendMovement = false;
        }
    }
}
