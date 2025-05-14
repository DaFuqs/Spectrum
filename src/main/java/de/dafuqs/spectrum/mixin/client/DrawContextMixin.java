package de.dafuqs.spectrum.mixin.client;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.api.render.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(GuiGraphics.class)
public abstract class DrawContextMixin {
	
	@Shadow
	public abstract void fill(RenderType layer, int x1, int y1, int x2, int y2, int color);
	
	@Shadow
	public abstract void fillGradient(int startX, int startY, int endX, int endY, int colorStart, int colorEnd);
	
	@Shadow
	public abstract void renderOutline(int x, int y, int width, int height, int color);
	
	@Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "net/minecraft/world/item/ItemStack.getCount ()I", ordinal = 0))
	protected void spectrum$drawSlotBackground(Font textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
		SlotBackgroundEffectProvider backgroundEffectProvider = null;
		
		if (stack.getItem() instanceof SlotBackgroundEffectProvider prov)
			backgroundEffectProvider = prov;
		
		else if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof SlotBackgroundEffectProvider prov)
			backgroundEffectProvider = prov;
		
		if (backgroundEffectProvider == null)
			return;
		
		var player = Minecraft.getInstance().player;
		var tickDelta = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
		
		var type = backgroundEffectProvider.backgroundType(player, stack);
		var opacity = backgroundEffectProvider.getEffectOpacity(player, stack, tickDelta);
		var color = (backgroundEffectProvider.getBackgroundColor(player, stack, tickDelta) & 0x00FFFFFF) | (Math.round(opacity * 255) << 24);
		var transColor = color & 0x00FFFFFF;
		
		var time = Minecraft.getInstance().player.level().getGameTime() % 864000;
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
			case BORDER:
				renderOutline(x, y, 16, 16, color);
		}
	}
	
	@Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "FIELD", target = "net/minecraft/client/gui/GuiGraphics.minecraft : Lnet/minecraft/client/Minecraft;", ordinal = 0, shift = At.Shift.BEFORE))
	protected void spectrum$appendBars(Font textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
		if (!(stack.getItem() instanceof ExtendedItemBarProvider extendedItemBarProvider)) {
			return;
		}
		
		for (int i = 0; i < extendedItemBarProvider.barCount(stack); i++) {
			var signature = extendedItemBarProvider.getSignature(Minecraft.getInstance().player, stack, i);
			
			if (signature == ExtendedItemBarProvider.PASS)
				continue;
			
			int k = x + signature.xPos();
			int l = y + signature.yPos();
			this.fill(RenderType.guiOverlay(), k, l, k + signature.length(), l + signature.backgroundHeight(), signature.backgroundColor());
			this.fill(RenderType.guiOverlay(), k, l, k + signature.fill(), l + signature.fillHeight(), signature.fillColor());
		}
	}
	
	@WrapWithCondition(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/GuiGraphics.fill (Lnet/minecraft/client/renderer/RenderType;IIIII)V", ordinal = 0))
	protected boolean spectrum$disableVanillaBackground(GuiGraphics instance, RenderType layer, int x1, int y1, int x2, int y2, int color, @Local(argsOnly = true) ItemStack stack) {
		if (stack.getItem() instanceof ExtendedItemBarProvider extendedItemBarProvider) {
			return extendedItemBarProvider.allowVanillaDurabilityBarRendering(Minecraft.getInstance().player, stack);
		}
		return true;
	}
	
	@WrapWithCondition(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/GuiGraphics.fill (Lnet/minecraft/client/renderer/RenderType;IIIII)V", ordinal = 1))
	protected boolean spectrum$disableVanillaBar(GuiGraphics instance, RenderType layer, int x1, int y1, int x2, int y2, int color, @Local(argsOnly = true) ItemStack stack) {
		if (stack.getItem() instanceof ExtendedItemBarProvider extendedItemBarProvider) {
			return extendedItemBarProvider.allowVanillaDurabilityBarRendering(Minecraft.getInstance().player, stack);
		}
		return true;
	}
}
