package de.dafuqs.spectrum.mixin.compat.colorfulhearts.absent;

import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.render.*;
import net.fabricmc.api.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.hud.*;
import net.minecraft.client.util.math.*;
import net.minecraft.entity.player.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {

	@Inject(method = "renderStatusBars(Lnet/minecraft/client/util/math/MatrixStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getArmor()I"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void spectrum$renderHealthBar(MatrixStack matrices, CallbackInfo ci, PlayerEntity cameraPlayer, int lastHealth, boolean blinking, long timeStart, int health, HungerManager hungerManager, int foodLevel, int x, int foodX, int y, float maxHealth, int absorption, int heartRows, int rowHeight, int armorY) {
		HudRenderers.renderAzureDike(matrices, cameraPlayer, x, armorY);
	}
	
	@ModifyVariable(method = "renderHealthBar", at = @At("STORE"), ordinal = 7)
	private int spectrum$showDivinityHardcoreHearts(int i, MatrixStack matrices, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking) {
		if (player.hasStatusEffect(SpectrumStatusEffects.DIVINITY)) {
			return 9 * 5;
		}
		return i;
	}
	
}