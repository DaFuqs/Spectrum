package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.sugar.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.effect.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ClientPacketListener.class)
public class ClientPlayNetworkHandlerMixin {
	
	@Inject(method = "handleUpdateMobEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;forceAddEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)V"))
	public void readAndApplySevereFlag(ClientboundUpdateMobEffectPacket packet, CallbackInfo ci, @Local MobEffectInstance effect) {
		if (packet.spectrum$isSevere())
			effect.spectrum$setSevere(true);
	}
	
}