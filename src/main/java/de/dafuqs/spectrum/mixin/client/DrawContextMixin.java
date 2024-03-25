package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import de.dafuqs.spectrum.api.render.ExtendedItemBars;
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

    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/DrawContext;client:Lnet/minecraft/client/MinecraftClient;", ordinal = 0, shift = At.Shift.BEFORE))
    protected void spectrum$appendBars(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
        if (!(stack.getItem() instanceof ExtendedItemBars extendedItemBars)) {
            return;
        }

        for (int i = 0; i < extendedItemBars.barCount(stack); i++) {
            var signature =  extendedItemBars.getSignature(MinecraftClient.getInstance().player, stack, i);

            if (signature == ExtendedItemBars.PASS)
                continue;

            int k = x + signature.xPos();
            int l = y + signature.yPos();
            this.fill(RenderLayer.getGuiOverlay(), k, l, k + signature.length(), l + signature.backgroundHeight(), signature.backgroundColor());
            this.fill(RenderLayer.getGuiOverlay(), k, l, k + signature.fill(), l + signature.fillHeight(), signature.fillColor());
        }
    }

    @WrapWithCondition(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(Lnet/minecraft/client/render/RenderLayer;IIIII)V", ordinal = 0))
    protected boolean spectrum$disableVanillaBackground(DrawContext instance, RenderLayer layer, int x1, int y1, int x2, int y2, int color, @Local(argsOnly = true) ItemStack stack) {
        if (stack.getItem() instanceof ExtendedItemBars extendedItemBars) {
            return extendedItemBars.allowVanillaDurabilityBarRendering(MinecraftClient.getInstance().player, stack);
        }
        return true;
    }

    @WrapWithCondition(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(Lnet/minecraft/client/render/RenderLayer;IIIII)V", ordinal = 1))
    protected boolean spectrum$disableVanillaBar(DrawContext instance, RenderLayer layer, int x1, int y1, int x2, int y2, int color, @Local(argsOnly = true) ItemStack stack) {
        if (stack.getItem() instanceof ExtendedItemBars extendedItemBars) {
            return extendedItemBars.allowVanillaDurabilityBarRendering(MinecraftClient.getInstance().player, stack);
        }
        return true;
    }
}
