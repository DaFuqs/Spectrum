package de.dafuqs.spectrum.mixin.compat.colorful_hearts.absent;

import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.render.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.hud.*;
import net.minecraft.entity.player.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
	
	@Shadow
	@Final
	@Mutable
	private final MinecraftClient client;
	@Shadow
	private int scaledWidth;
	@Shadow
	private int scaledHeight;
	
	public InGameHudMixin(MinecraftClient client) {
		this.client = client;
	}
	
	@Inject(method = "renderHealthBar", at = @At(value = "TAIL"))
	private void spectrum$renderStatusBars(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
		HudRenderers.renderAzureDike(context, scaledWidth, scaledHeight, player);
	}
	
	@ModifyVariable(method = "renderHealthBar", at = @At("STORE"), ordinal = 7)
	private int spectrum$showDivinityHardcoreHearts(int i, DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking) {
		if (player.hasStatusEffect(SpectrumStatusEffects.DIVINITY)) {
			return 9 * 5;
		}
		return i;
	}
	
}