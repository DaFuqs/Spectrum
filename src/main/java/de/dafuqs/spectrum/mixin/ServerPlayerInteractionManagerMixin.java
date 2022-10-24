package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.cca.HardcoreDeathComponent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
	
	@Shadow private GameMode gameMode;
	
	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/server/network/ServerPlayerEntity;)V")
	protected void spectrum$checkForHardcoreDeath(ServerPlayerEntity player, CallbackInfo ci) {
		if(HardcoreDeathComponent.hasHardcoreDeath(player.getUuid())) {
			this.gameMode = GameMode.SPECTATOR; // TODO: send this to the client
		}
	}
	
}
