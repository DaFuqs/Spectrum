package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.api.status_effect.*;
import net.minecraft.entity.effect.*;
import net.minecraft.network.*;
import net.minecraft.network.packet.s2c.play.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(EntityStatusEffectS2CPacket.class)
public class EntityStatusEffectS2CPacketMixin implements IncurablePacketInject {

	@Unique
	private boolean incurable;

	@Inject(method = "<init>(ILnet/minecraft/entity/effect/StatusEffectInstance;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/network/packet/s2c/play/EntityStatusEffectS2CPacket;flags:B"))
	public void initIncurable(int entityId, StatusEffectInstance effect, CallbackInfo ci, @Local byte b) {
		this.incurable = Incurable.isIncurable(effect);
	}

	@Inject(method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketByteBuf;readByte()B", ordinal = 1))
	public void initIncurable(PacketByteBuf buf, CallbackInfo ci) {
		this.incurable = buf.readBoolean();
	}

	@Inject(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketByteBuf;writeByte(I)Lio/netty/buffer/ByteBuf;", ordinal = 1))
	public void writeIncurable(PacketByteBuf buf, CallbackInfo ci) {
		buf.writeBoolean(incurable);
	}

	@Override
	public boolean getIncurable() {
		return incurable;
	}
}
