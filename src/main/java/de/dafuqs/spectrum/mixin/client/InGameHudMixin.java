package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import de.dafuqs.spectrum.render.HudRenderers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
	
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
	
	@Inject(method = "renderHealthBar(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/entity/player/PlayerEntity;IIIIFIIIZ)V", at = @At(value = "TAIL"))
	private void spectrum$renderStatusBars(MatrixStack matrices, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
		HudRenderers.renderAzureDike(matrices, scaledWidth, scaledHeight, player);
	}
	
	@ModifyVariable(method = "renderHealthBar", at = @At("STORE"), ordinal = 7)
	private int spectrum$showDivinityHardcoreHearts(int i, MatrixStack matrices, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking) {
		if(player.hasStatusEffect(SpectrumStatusEffects.DIVINITY)) {
			return 9 * 5;
		}
		return i;
	}
	
}