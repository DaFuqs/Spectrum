package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.cca.HardcoreDeathComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(DeathScreen.class)
public class DeathScreenMixin {
	
	@ModifyVariable(method = "<init>(Lnet/minecraft/text/Text;Z)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private static boolean spectrum$isHardcore(boolean isHardcore) {
		if(!isHardcore && (HardcoreDeathComponent.isInHardcore(MinecraftClient.getInstance().player) || HardcoreDeathComponent.hasHardcoreDeath(MinecraftClient.getInstance().player.getUuid()))) {
			return true;
		}
		return isHardcore;
	}
	
}
