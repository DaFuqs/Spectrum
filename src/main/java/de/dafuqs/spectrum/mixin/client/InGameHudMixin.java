package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.render.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.hud.*;
import net.minecraft.entity.player.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow public abstract void render(DrawContext context, float tickDelta);

    @Inject(method = "renderStatusBars(Lnet/minecraft/client/gui/DrawContext;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getArmor()I"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void spectrum$renderHealthBar(DrawContext context, CallbackInfo ci, PlayerEntity cameraPlayer, int lastHealth, boolean blinking, long timeStart, int health, HungerManager hungerManager, int foodLevel, int x, int foodX, int y, float maxHealth, int absorption, int heartRows, int rowHeight, int armorY) {
        HudRenderers.renderAzureDike(context, cameraPlayer, x, armorY);
    }

    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;isFancyGraphicsOrBetter()Z"))
    private boolean spectrum$disableVignietteInDimension(boolean original) {
        if (isInDim()) {
            return false;
        }
        return original;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderCrosshair(Lnet/minecraft/client/gui/DrawContext;)V"))
    private boolean spectrum$disableCrosshairSomnolence(InGameHud instance, DrawContext context) {
        var player = getCameraPlayer();

        if (player == null)
            return true;
		
		var potency = SleepStatusEffect.getSleepScaling(player);

        return potency <= 0.25F;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(FLnet/minecraft/client/gui/DrawContext;)V"))
    private boolean spectrum$disableHotbarSomnolence(InGameHud instance, float tickDelta, DrawContext context) {
        var player = getCameraPlayer();

        if (player == null)
            return true;
		
		var potency = SleepStatusEffect.getSleepScaling(player);

        return potency <= 0.4F;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusBars(Lnet/minecraft/client/gui/DrawContext;)V"))
    private boolean spectrum$disableStatusSomnolence(InGameHud instance, DrawContext context) {
        var player = getCameraPlayer();

        if (player == null)
            return true;
		
		var potency = SleepStatusEffect.getSleepScaling(player);

        return potency <= 0.4F;
    }

    @Unique
    private static boolean isInDim() {
        MinecraftClient client = MinecraftClient.getInstance();
        return SpectrumDimensions.DIMENSION_KEY.equals(client.player.getWorld().getRegistryKey());
    }

}
