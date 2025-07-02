package de.dafuqs.spectrum.mixin.compat.colorful_hearts.absent;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.gui.*;
import net.minecraft.world.entity.player.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(Gui.class)
public abstract class InGameHudMixin {
	
	@ModifyVariable(method = "renderHearts", at = @At("STORE"), ordinal = 7)
	private int spectrum$showDivinityHardcoreHearts(int i, GuiGraphics context, Player player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking) {
		if (player.hasEffect(SpectrumStatusEffects.DIVINITY)) {
			return 9 * 5;
		}
		return i;
	}
	
}
