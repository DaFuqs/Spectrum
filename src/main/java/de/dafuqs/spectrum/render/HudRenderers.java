package de.dafuqs.spectrum.render;

import com.mojang.blaze3d.platform.*;
import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.cca.azure_dike.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;

@Environment(EnvType.CLIENT)
public class HudRenderers {
	
	private static final Component missingInkText = Component.translatable("item.spectrum.constructors_staff.tooltip.missing_ink");
	private static final Component noneText = Component.translatable("item.spectrum.constructors_staff.tooltip.none_in_inventory");
	
	private static ItemStack itemStackToRender;
	private static int amount;
	private static boolean missingInk;
	
	public static void register() {
		HudRenderCallback.EVENT.register((drawContext, tickDelta) -> renderSelectedStaffStack(drawContext));
	}
	
	private static final int SPECTRUM$_DIKE_HEARTS_PER_ROW = 10;
	private static final int SPECTRUM$_DIKE_PER_ROW = 20;
	
	// this is run in InGameHudMixin instead to render behind the chat and other gui elements
	public static void renderAzureDike(GuiGraphics drawContext, Player cameraPlayer, int x, int y) {
		AzureDikeComponent azureDikeComponent = AzureDikeProvider.getAzureDikeComponent(cameraPlayer);
		int maxCharges = (int) Math.ceil(azureDikeComponent.getMaxProtection());
		if (maxCharges > 0) {
			int charges = (int) Math.ceil(azureDikeComponent.getCurrentProtection());

			boolean blink = false;
			if (cameraPlayer.getLastDamageSource() != null && cameraPlayer.level() != null) {
				blink = (cameraPlayer.level().getGameTime() >> 2) % 2 == 0;
			}
			
			int totalDikeCanisters = (maxCharges - 1) / SPECTRUM$_DIKE_PER_ROW;
			int filledDikeCanisters = (charges - 1) / SPECTRUM$_DIKE_PER_ROW;
			int displayedDike = (charges - 1) % SPECTRUM$_DIKE_PER_ROW + 1;
			int dikeHeartOutlinesThisRow = totalDikeCanisters > filledDikeCanisters ? SPECTRUM$_DIKE_HEARTS_PER_ROW : (((maxCharges - 1) % SPECTRUM$_DIKE_PER_ROW / 2) + 1);
			
			boolean renderBackRow = filledDikeCanisters > 0;
			boolean hasArmor = cameraPlayer.getArmorValue() > 0;

			var texture = AzureDikeComponent.AZURE_DIKE_BAR_TEXTURE;
			
			x += SpectrumCommon.CONFIG.AzureDikeHudOffsetX;
			y += hasArmor ? SpectrumCommon.CONFIG.AzureDikeHudOffsetYWithArmor : SpectrumCommon.CONFIG.AzureDikeHudOffsetY;
			
			RenderSystem.enableBlend();
			
			// back row
			if (renderBackRow) {
				for (int i = displayedDike / 2; i < 10; i++) {
					drawContext.blit(texture, x + i * 8, y, 36, 9, 9, 9, 256, 256); // "back row" icon
				}
			}
			
			// outline
			for (int i = 0; i < dikeHeartOutlinesThisRow; i++) {
				if (renderBackRow) {
					if (blink) {
						drawContext.blit(texture, x + i * 8, y, 54, 9, 9, 9, 256, 256); // background
					} else {
						drawContext.blit(texture, x + i * 8, y, 45, 9, 9, 9, 256, 256); // background
					}
				} else {
					if (blink) {
						drawContext.blit(texture, x + i * 8, y, 9, 9, 9, 9, 256, 256); // background
					} else {
						drawContext.blit(texture, x + i * 8, y, 0, 9, 9, 9, 256, 256); // background
					}
				}
			}
			
			// hearts
			for (int i = 0; i < displayedDike; i += 2) {
				if (i + 1 < displayedDike) {
					drawContext.blit(texture, x + i * 4, y, 18, 9, 9, 9, 256, 256); // full charge icon
				} else {
					drawContext.blit(texture, x + i * 4, y, 27, 9, 9, 9, 256, 256); // half charge icon
				}
			}
			
			// canisters
			for (int i = 0; i < filledDikeCanisters; i++) {
				drawContext.blit(texture, x + i * 6, y - 9, 0, 0, 9, 9, 256, 256); // full canisters
			}
			for (int i = filledDikeCanisters; i < totalDikeCanisters; i++) {
				drawContext.blit(texture, x + i * 6, y - 9, 9, 0, 9, 9, 256, 256); // empty canisters
			}
			
			RenderSystem.disableBlend();
		}
	}
	
	private static void renderSelectedStaffStack(GuiGraphics drawContext) {
		Minecraft client = Minecraft.getInstance();
		if (amount > -1 && itemStackToRender != null) {
			// Render the item stack next to the cursor
			Window window = Minecraft.getInstance().getWindow();
			int x = window.getGuiScaledWidth() / 2 + 3;
			int y = window.getGuiScaledHeight() / 2 + 3;
			
			var poseStack = drawContext.pose();
			poseStack.pushPose();
			poseStack.scale(0.5F, 0.5F, 1F);
			
			var textRenderer = client.font;
			drawContext.renderItem(itemStackToRender, (x + 8) * 2, (y + 8) * 2); // TODO: make this render 2x the size, so it spans all 2 rows of text
			poseStack.scale(2F, 2F, 1F);
			drawContext.drawString(textRenderer, itemStackToRender.getHoverName(), x + 18, y + 8, 0xFFFFFF, false);
			if (amount == 0) {
				drawContext.drawString(textRenderer, noneText, x + 18, y + 19, 0xDDDDDD, false);
			} else if (missingInk) {
				drawContext.drawString(textRenderer, missingInkText, x + 18, y + 19, 0xDDDDDD, false);
			} else {
				drawContext.drawString(textRenderer, amount + "x", x + 18, y + 19, 0xDDDDDD, false);
			}
			poseStack.popPose();
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
