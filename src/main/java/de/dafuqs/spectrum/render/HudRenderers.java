package de.dafuqs.spectrum.render;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.cca.azure_dike.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.hud.*;
import net.minecraft.client.util.*;
import net.minecraft.client.util.math.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import vazkii.patchouli.client.*;

import static net.minecraft.client.gui.DrawableHelper.*;

@Environment(EnvType.CLIENT)
public class HudRenderers {
	
	private static final Text missingInkText = Text.translatable("item.spectrum.constructors_staff.tooltip.missing_ink");
	private static final Text noneText = Text.translatable("item.spectrum.constructors_staff.tooltip.none_in_inventory");
	
	private static ItemStack itemStackToRender;
	private static int amount;
	private static boolean missingInk;
	
	public static void register() {
		// That one is on Patchouli. ty <3
		HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> renderSelectedStaffStack(matrixStack));
	}

	private static final int SPECTRUM$_DIKE_HEARTS_PER_ROW = 10;
	private static final int SPECTRUM$_DIKE_PER_ROW = 20;
	
	// this is run in InGameHudMixin instead to render behind the chat and other gui elements
	public static void renderAzureDike(MatrixStack matrixStack, PlayerEntity cameraPlayer, int x, int y) {
		AzureDikeComponent azureDikeComponent = AzureDikeProvider.getAzureDikeComponent(cameraPlayer);
		int maxCharges = azureDikeComponent.getMaxProtection();
		if (maxCharges > 0) {
			int charges = azureDikeComponent.getProtection();
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
			
			RenderSystem.setShaderTexture(0, AzureDikeComponent.AZURE_DIKE_BAR_TEXTURE);
			
			x += SpectrumCommon.CONFIG.AzureDikeHudOffsetX;
			y += (hasArmor ? SpectrumCommon.CONFIG.AzureDikeHudOffsetYWithArmor : SpectrumCommon.CONFIG.AzureDikeHudOffsetY);

			// back row
			if (renderBackRow) {
				for (int i = displayedDike / 2; i < 10; i++) {
					InGameHud.drawTexture(matrixStack, x + i * 8, y, 36, 9, 9, 9, 256, 256); // "back row" icon
				}
			}
			
			// outline
			for (int i = 0; i < dikeHeartOutlinesThisRow; i++) {
				if (renderBackRow) {
					if (blink) {
						InGameHud.drawTexture(matrixStack, x + i * 8, y, 54, 9, 9, 9, 256, 256); // background
					} else {
						InGameHud.drawTexture(matrixStack, x + i * 8, y, 45, 9, 9, 9, 256, 256); // background
					}
				} else {
					if (blink) {
						InGameHud.drawTexture(matrixStack, x + i * 8, y, 9, 9, 9, 9, 256, 256); // background
					} else {
						InGameHud.drawTexture(matrixStack, x + i * 8, y, 0, 9, 9, 9, 256, 256); // background
					}
				}
			}
			
			// hearts
			for (int i = 0; i < displayedDike; i += 2) {
				if (i + 1 < displayedDike) {
					InGameHud.drawTexture(matrixStack, x + i * 4, y, 18, 9, 9, 9, 256, 256); // full charge icon
				} else {
					InGameHud.drawTexture(matrixStack, x + i * 4, y, 27, 9, 9, 9, 256, 256); // half charge icon
				}
			}
			
			// canisters
			for (int i = 0; i < filledDikeCanisters; i++) {
				InGameHud.drawTexture(matrixStack, x + i * 6, y - 9, 0, 0, 9, 9, 256, 256); // full canisters
			}
			for (int i = filledDikeCanisters; i < totalDikeCanisters; i++) {
				InGameHud.drawTexture(matrixStack, x + i * 6, y - 9, 9, 0, 9, 9, 256, 256); // empty canisters
			}
			
			RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
		}
	}
	
	private static void renderSelectedStaffStack(MatrixStack matrixStack) {
		if (amount > -1 && itemStackToRender != null) {
			// Render the item stack next to the cursor
			Window window = MinecraftClient.getInstance().getWindow();
			int x = window.getScaledWidth() / 2 + 3;
			int y = window.getScaledHeight() / 2 + 3;
			
			matrixStack.push();
			matrixStack.scale(0.5F, 0.5F, 1F);
			RenderHelper.renderItemStackInGui(matrixStack, itemStackToRender, (x + 8) * 2, (y + 8) * 2);
			matrixStack.scale(2F, 2F, 1F);
			MinecraftClient.getInstance().textRenderer.draw(matrixStack, itemStackToRender.getName(), x + 18, y + 8, 0xFFFFFF);
			if (amount == 0) {
				MinecraftClient.getInstance().textRenderer.draw(matrixStack, noneText, x + 18, y + 19, 0xDDDDDD);
			} else if (missingInk) {
				MinecraftClient.getInstance().textRenderer.draw(matrixStack, missingInkText, x + 18, y + 19, 0xDDDDDD);
			} else {
				MinecraftClient.getInstance().textRenderer.draw(matrixStack, amount + "x", x + 18, y + 19, 0xDDDDDD);
			}
			matrixStack.pop();
		}
	}
	
	public static void setItemStackToRender(ItemStack itemStack, int amount, boolean missingInk) {
		HudRenderers.itemStackToRender = itemStack;
		HudRenderers.amount = amount;
		HudRenderers.missingInk = missingInk;
	}
	
	public static void doNotRenderOverlay() {
		HudRenderers.amount = -1;
	}
	
	
}
