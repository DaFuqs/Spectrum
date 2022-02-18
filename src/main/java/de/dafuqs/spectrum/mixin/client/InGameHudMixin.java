package de.dafuqs.spectrum.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.azure_dike.AzureDikeProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
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
    
    private final static Identifier AZURE_DIKE_BAR_TEXTURE = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/azure_dike_overlay.png");
    
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
            
            if (charges > 0) {
                LivingEntity livingEntity = this.getRiddenEntity();
                int variable_two;
                int variable_three;
                int height = this.scaledHeight - 49;
                int width = this.scaledWidth / 2 + 91;
                
                if (this.getHeartCount(livingEntity) == 0) {
                    for (int i = 0; i < 10; i++) {
                        variable_three = height;
                        int upperPos = 9;
                        int lowerPos = 0;
                        
                        variable_two = width - i * 8 - 9;
                        variable_two = variable_two + SpectrumCommon.CONFIG.azureDikeHudX;
                        variable_three = variable_three + SpectrumCommon.CONFIG.azureDikeHudY;
                        RenderSystem.setShaderTexture(0, AZURE_DIKE_BAR_TEXTURE);
                        if (i * 2 + 1 < charges) {
                            this.drawTexture(matrices, variable_two, variable_three, lowerPos, upperPos, 9, 9); // full charge icon
                        }
                        if (i * 2 + 1 == charges) {
                            this.drawTexture(matrices, variable_two, variable_three, lowerPos + 9, upperPos, 9, 9); // half charge icon
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
    
    /*@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;getCurrentGameMode()Lnet/minecraft/world/GameMode;", ordinal = 0))
    private void renderOnHud(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if(!client.options.hudHidden) {
            AzureDikeOverlay.render();
        }
    }*/

}