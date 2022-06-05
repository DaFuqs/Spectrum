package de.dafuqs.spectrum.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.azure_dike.AzureDikeComponent;
import de.dafuqs.spectrum.azure_dike.AzureDikeProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawableHelper {
	
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
	
	@Inject(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 1))
	private void renderStatusBarsMixin(MatrixStack matrices, CallbackInfo info) {
		PlayerEntity playerEntity = this.getCameraPlayer();
		if (playerEntity != null && !playerEntity.isInvulnerable()) {
			int charges = AzureDikeProvider.getAzureDikeCharges(playerEntity);
			int maxCharges = AzureDikeProvider.getMaxAzureDikeCharges(playerEntity);
			
			if (charges > 0) {
				LivingEntity livingEntity = this.getRiddenEntity();
				
				int v = 9;
				int u = 0;
				if (this.getHeartCount(livingEntity) == 0) {
					
					RenderSystem.setShaderTexture(0, AzureDikeComponent.AZURE_DIKE_BAR_TEXTURE);
					
					int height = this.scaledHeight - 49;
					int width = this.scaledWidth / 2 + 91;
					
					for (int i = 0; i < maxCharges / 2.0; i++) {
						
						int x;
						int y;
						boolean hasFullAir = playerEntity.getAir() == playerEntity.getMaxAir();
						if (hasFullAir) {
							x = width - i * 8 - 9 + SpectrumCommon.CONFIG.azureDikeHudOffsetX;
							y = height + SpectrumCommon.CONFIG.azureDikeHudOffsetY;
						} else {
							x = width - i * 8 - 9 + SpectrumCommon.CONFIG.azureDikeHudOffsetXLackingAir;
							y = height + SpectrumCommon.CONFIG.azureDikeHudOffsetYLackingAir;
						}
						
						this.drawTexture(matrices, x, y, u, v, 9, 9); // background
					}
					
					for (int i = 0; i < maxCharges; i++) {
						
						int x;
						int y;
						boolean hasFullAir = playerEntity.getAir() == playerEntity.getMaxAir();
						if (hasFullAir) {
							x = width - i * 8 - 9 + SpectrumCommon.CONFIG.azureDikeHudOffsetX;
							y = height + SpectrumCommon.CONFIG.azureDikeHudOffsetY;
						} else {
							x = width - i * 8 - 9 + SpectrumCommon.CONFIG.azureDikeHudOffsetXLackingAir;
							y = height + SpectrumCommon.CONFIG.azureDikeHudOffsetYLackingAir;
						}
						
						if (i * 2 + 1 < charges) {
							this.drawTexture(matrices, x, y, u + 18, v, 9, 9); // full charge icon
						}
						if (i * 2 + 1 == charges) {
							this.drawTexture(matrices, x, y, u + 27, v, 9, 9); // half charge icon
						}
					}
					
					RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
				}
			}
		}
	}
	
	@Shadow
	private PlayerEntity getCameraPlayer() {
		return null;
	}
	
	@Shadow
	private LivingEntity getRiddenEntity() {
		return null;
	}
	
	@Shadow
	private int getHeartCount(LivingEntity entity) {
		return 0;
	}
	
}