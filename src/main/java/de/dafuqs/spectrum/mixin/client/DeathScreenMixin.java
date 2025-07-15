package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.cca.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin {
	
	@ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private static boolean spectrum$isHardcore(boolean isHardcore) {
		if (!isHardcore && (HardcoreDeathComponent.isInHardcore(Minecraft.getInstance().player) || HardcoreDeathComponent.hasHardcoreDeath(Minecraft.getInstance().player.getGameProfile()))) {
			return true;
		}
		return isHardcore;
	}
	
}
