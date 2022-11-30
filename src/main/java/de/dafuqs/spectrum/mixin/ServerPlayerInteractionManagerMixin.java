package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.cca.HardcoreDeathComponent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {
	
	@Shadow
	@Final
	protected ServerPlayerEntity player;
	
	// If someone puts players out of spectator manually
	// forget about their hardcore death
	@Inject(at = @At("HEAD"), method = "setGameMode(Lnet/minecraft/world/GameMode;Lnet/minecraft/world/GameMode;)V")
	public void spectrum$mitigateFallDamageWithPuffCirclet(GameMode gameMode, GameMode previousGameMode, CallbackInfo ci) {
		if (gameMode != GameMode.SPECTATOR && previousGameMode == GameMode.SPECTATOR && HardcoreDeathComponent.hasHardcoreDeath(player.getGameProfile())) {
			HardcoreDeathComponent.removeHardcoreDeath(player.getGameProfile());
		}
	}
	
}
