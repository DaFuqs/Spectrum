package de.dafuqs.spectrum.mixin.compat.colorful_hearts.absent;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.hud.*;
import net.minecraft.entity.player.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
	
	@ModifyVariable(method = "renderHealthBar", at = @At("STORE"), ordinal = 7)
	private int spectrum$showDivinityHardcoreHearts(int i, DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking) {
		if (player.hasStatusEffect(SpectrumStatusEffects.DIVINITY)) {
			return 9 * 5;
		}
		return i;
	}
	
}
