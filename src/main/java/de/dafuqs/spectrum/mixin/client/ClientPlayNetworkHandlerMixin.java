package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.api.status_effect.*;
import net.minecraft.client.network.*;
import net.minecraft.entity.effect.*;
import net.minecraft.network.packet.s2c.play.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

	@Inject(method = "onEntityStatusEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)V"))
	public void readAndApplyIncurableFlag(EntityStatusEffectS2CPacket packet, CallbackInfo ci, @Local StatusEffectInstance effect) {
		var incurable = ((IncurablePacketInject) packet).getIncurable();
		if (incurable) {
			((Incurable) effect).spectrum$setIncurable(true);
		}
	}
}