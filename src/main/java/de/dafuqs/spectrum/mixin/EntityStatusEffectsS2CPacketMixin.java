package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.injectors.*;
import net.minecraft.network.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.effect.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ClientboundUpdateMobEffectPacket.class)
public abstract class EntityStatusEffectsS2CPacketMixin implements EntityStatusEffectS2CPacketInjector {
	
	@Unique
	private boolean incurable;
	
	@Inject(method = "<init>(ILnet/minecraft/world/effect/MobEffectInstance;Z)V", at = @At("RETURN"))
	public void initIncurable(int entityId, MobEffectInstance effect, boolean keepFading, CallbackInfo ci) {
		this.incurable = StatusEffectHelper.isIncurable(effect);
	}
	
	@Inject(method = "<init>(Lnet/minecraft/network/RegistryFriendlyByteBuf;)V", at = @At("RETURN"))
	public void initIncurable(RegistryFriendlyByteBuf buf, CallbackInfo ci) {
		this.incurable = buf.readBoolean();
	}
	
	@Inject(method = "write", at = @At("RETURN"))
	public void writeIncurable(RegistryFriendlyByteBuf buf, CallbackInfo ci) {
		buf.writeBoolean(incurable);
	}
	
	@Override
	public boolean spectrum$isIncurable() {
		return incurable;
	}
}
