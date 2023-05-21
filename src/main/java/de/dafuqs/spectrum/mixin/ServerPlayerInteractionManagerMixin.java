package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.cca.*;
import net.minecraft.server.network.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

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
