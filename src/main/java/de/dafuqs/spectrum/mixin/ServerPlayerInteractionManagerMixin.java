package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.attachment_types.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerInteractionManagerMixin {
	
	@Shadow
	@Final
	protected ServerPlayer player;
	
	// If someone puts players out of spectator manually
	// forget about their hardcore death
	@Inject(at = @At("HEAD"), method = "setGameModeForPlayer")
	public void spectrum$mitigateFallDamageWithPuffCirclet(GameType gameMode, GameType previousGameMode, CallbackInfo ci) {
		if (gameMode != GameType.SPECTATOR && previousGameMode == GameType.SPECTATOR && HardcoreDeathAttachmentType.hasHardcoreDeath(player)) {
			player.setData(HardcoreDeathAttachmentType.ATTACHMENT_TYPE, false);
		}
	}
	
}
