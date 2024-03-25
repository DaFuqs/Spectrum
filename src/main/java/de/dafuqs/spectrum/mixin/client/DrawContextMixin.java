package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import de.dafuqs.spectrum.api.render.ExtendedItemBarProvider;
import de.dafuqs.spectrum.api.render.SlotBackgroundEffectProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {

    @Shadow public abstract void fill(RenderLayer layer, int x1, int y1, int x2, int y2, int color);

    @Shadow public abstract void fillGradient(int startX, int startY, int endX, int endY, int colorStart, int colorEnd);

    @Shadow public abstract void drawBorder(int x, int y, int width, int height, int color);

    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getCount()I", ordinal = 0))
    protected void spectrum$drawSlotBackground(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
        if (!(stack.getItem() instanceof SlotBackgroundEffectProvider backgroundEffectProvider))
            return;

        var player = MinecraftClient.getInstance().player;
        var tickDelta = MinecraftClient.getInstance().getTickDelta();

        var type = backgroundEffectProvider.backgroundType(player, stack);
        var opacity = backgroundEffectProvider.getEffectOpacity(player, stack, tickDelta);
        var color = (backgroundEffectProvider.getBackgroundColor(player, stack, tickDelta) & 0x00FFFFFF) | (Math.round(opacity * 255) << 24);
        var transColor = color  & 0x00FFFFFF;

        var time = MinecraftClient.getInstance().player.getWorld().getTime() % 864000;
        var bounce = Math.sin((time + tickDelta) / 20F) * 0.4F + 0.5F;
        var alpha = (int) Math.round(bounce * 255F);

        switch (type) {
            case NONE: {
                return;
            }
            case BORDER_FADE: {
                fillGradient(x, y, x + 1, y + 15, transColor, color);
                fillGradient(x + 15, y, x + 16, y + 15, transColor, color);
                fillGradient(x, y + 15, x + 16, y + 16, color, color);
                return;
            }
            case FULL_PACKAGE:
            case PULSE: {
                fillGradient(x, y, x + 16, y + 16, transColor, transColor | (alpha << 24));
                if (type == SlotBackgroundEffectProvider.SlotEffect.PULSE)
                    return;
            }
            case BORDER: drawBorder(x, y, 16, 16, color);
        }
    }

    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/DrawContext;client:Lnet/minecraft/client/MinecraftClient;", ordinal = 0, shift = At.Shift.BEFORE))
    protected void spectrum$appendBars(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
        if (!(stack.getItem() instanceof ExtendedItemBarProvider extendedItemBarProvider)) {
            return;
        }

        for (int i = 0; i < extendedItemBarProvider.barCount(stack); i++) {
            var signature =  extendedItemBarProvider.getSignature(MinecraftClient.getInstance().player, stack, i);

            if (signature == ExtendedItemBarProvider.PASS)
                continue;

            int k = x + signature.xPos();
            int l = y + signature.yPos();
            this.fill(RenderLayer.getGuiOverlay(), k, l, k + signature.length(), l + signature.backgroundHeight(), signature.backgroundColor());
            this.fill(RenderLayer.getGuiOverlay(), k, l, k + signature.fill(), l + signature.fillHeight(), signature.fillColor());
        }
    }

    @WrapWithCondition(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(Lnet/minecraft/client/render/RenderLayer;IIIII)V", ordinal = 0))
    protected boolean spectrum$disableVanillaBackground(DrawContext instance, RenderLayer layer, int x1, int y1, int x2, int y2, int color, @Local(argsOnly = true) ItemStack stack) {
        if (stack.getItem() instanceof ExtendedItemBarProvider extendedItemBarProvider) {
            return extendedItemBarProvider.allowVanillaDurabilityBarRendering(MinecraftClient.getInstance().player, stack);
        }
        return true;
    }

    @WrapWithCondition(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(Lnet/minecraft/client/render/RenderLayer;IIIII)V", ordinal = 1))
    protected boolean spectrum$disableVanillaBar(DrawContext instance, RenderLayer layer, int x1, int y1, int x2, int y2, int color, @Local(argsOnly = true) ItemStack stack) {
        if (stack.getItem() instanceof ExtendedItemBarProvider extendedItemBarProvider) {
            return extendedItemBarProvider.allowVanillaDurabilityBarRendering(MinecraftClient.getInstance().player, stack);
        }
        return true;
    }
}
