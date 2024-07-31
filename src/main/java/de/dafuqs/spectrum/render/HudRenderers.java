package de.dafuqs.spectrum.render;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.cca.azure_dike.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.text.*;

@Environment(EnvType.CLIENT)
public class HudRenderers {
	
	private static final Text missingInkText = Text.translatable("item.spectrum.constructors_staff.tooltip.missing_ink");
	private static final Text noneText = Text.translatable("item.spectrum.constructors_staff.tooltip.none_in_inventory");
	
	private static ItemStack itemStackToRender;
	private static int amount;
	private static boolean missingInk;
	
	public static void register() {
		HudRenderCallback.EVENT.register((drawContext, tickDelta) -> renderSelectedStaffStack(drawContext));
	}
	
	private static final int SPECTRUM$_DIKE_HEARTS_PER_ROW = 10;
	private static final int SPECTRUM$_DIKE_PER_ROW = 20;
	
	// this is run in InGameHudMixin instead to render behind the chat and other gui elements
	public static void renderAzureDike(DrawContext drawContext, PlayerEntity cameraPlayer, int x, int y) {
		AzureDikeComponent azureDikeComponent = AzureDikeProvider.getAzureDikeComponent(cameraPlayer);
		int maxCharges = (int) Math.ceil(azureDikeComponent.getMaxProtection());
		if (maxCharges > 0) {
			int charges = (int) Math.ceil(azureDikeComponent.getProtection());

			boolean blink = false;
			if (cameraPlayer.getRecentDamageSource() != null && cameraPlayer.getWorld() != null) {
				blink = (cameraPlayer.getWorld().getTime() >> 2) % 2 == 0;
			}
			
			int totalDikeCanisters = (maxCharges - 1) / SPECTRUM$_DIKE_PER_ROW;
			int filledDikeCanisters = (charges - 1) / SPECTRUM$_DIKE_PER_ROW;
			int displayedDike = (charges - 1) % SPECTRUM$_DIKE_PER_ROW + 1;
			int dikeHeartOutlinesThisRow = totalDikeCanisters > filledDikeCanisters ? SPECTRUM$_DIKE_HEARTS_PER_ROW : (((maxCharges - 1) % SPECTRUM$_DIKE_PER_ROW / 2) + 1);
			
			boolean renderBackRow = filledDikeCanisters > 0;
			boolean hasArmor = cameraPlayer.getArmor() > 0;

			var texture = AzureDikeComponent.AZURE_DIKE_BAR_TEXTURE;
			
			x += SpectrumCommon.CONFIG.AzureDikeHudOffsetX;
			y += hasArmor ? SpectrumCommon.CONFIG.AzureDikeHudOffsetYWithArmor : SpectrumCommon.CONFIG.AzureDikeHudOffsetY;

			// back row
			if (renderBackRow) {
				for (int i = displayedDike / 2; i < 10; i++) {
					drawContext.drawTexture(texture, x + i * 8, y, 36, 9, 9, 9, 256, 256); // "back row" icon
				}
			}
			
			// outline
			for (int i = 0; i < dikeHeartOutlinesThisRow; i++) {
				if (renderBackRow) {
					if (blink) {
						drawContext.drawTexture(texture, x + i * 8, y, 54, 9, 9, 9, 256, 256); // background
					} else {
						drawContext.drawTexture(texture, x + i * 8, y, 45, 9, 9, 9, 256, 256); // background
					}
				} else {
					if (blink) {
						drawContext.drawTexture(texture, x + i * 8, y, 9, 9, 9, 9, 256, 256); // background
					} else {
						drawContext.drawTexture(texture, x + i * 8, y, 0, 9, 9, 9, 256, 256); // background
					}
				}
			}
			
			// hearts
			for (int i = 0; i < displayedDike; i += 2) {
				if (i + 1 < displayedDike) {
					drawContext.drawTexture(texture, x + i * 4, y, 18, 9, 9, 9, 256, 256); // full charge icon
				} else {
					drawContext.drawTexture(texture, x + i * 4, y, 27, 9, 9, 9, 256, 256); // half charge icon
				}
			}
			
			// canisters
			for (int i = 0; i < filledDikeCanisters; i++) {
				drawContext.drawTexture(texture, x + i * 6, y - 9, 0, 0, 9, 9, 256, 256); // full canisters
			}
			for (int i = filledDikeCanisters; i < totalDikeCanisters; i++) {
				drawContext.drawTexture(texture, x + i * 6, y - 9, 9, 0, 9, 9, 256, 256); // empty canisters
			}
		}
	}
	
	private static void renderSelectedStaffStack(DrawContext drawContext) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (amount > -1 && itemStackToRender != null) {
			// Render the item stack next to the cursor
			Window window = MinecraftClient.getInstance().getWindow();
			int x = window.getScaledWidth() / 2 + 3;
			int y = window.getScaledHeight() / 2 + 3;
			
			var matrixStack = drawContext.getMatrices();
			matrixStack.push();
			matrixStack.scale(0.5F, 0.5F, 1F);
			
			var textRenderer = client.textRenderer;
			drawContext.drawItem(itemStackToRender, (x + 8) * 2, (y + 8) * 2); // TODO: make this render 2x the size, so it spans all 2 rows of text
			matrixStack.scale(2F, 2F, 1F);
			drawContext.drawText(textRenderer, itemStackToRender.getName(), x + 18, y + 8, 0xFFFFFF, false);
			if (amount == 0) {
				drawContext.drawText(textRenderer, noneText, x + 18, y + 19, 0xDDDDDD, false);
			} else if (missingInk) {
				drawContext.drawText(textRenderer, missingInkText, x + 18, y + 19, 0xDDDDDD, false);
			} else {
				drawContext.drawText(textRenderer, amount + "x", x + 18, y + 19, 0xDDDDDD, false);
			}
			matrixStack.pop();
		}
	}
	
	public static void setItemStackToRender(ItemStack itemStack, int amount, boolean missingInk) {
		HudRenderers.itemStackToRender = itemStack;
		HudRenderers.amount = amount;
		HudRenderers.missingInk = missingInk;
	}
	
	public static void clearItemStackOverlay() {
		HudRenderers.amount = -1;
	}
	
}
