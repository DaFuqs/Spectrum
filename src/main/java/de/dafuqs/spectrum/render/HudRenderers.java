package de.dafuqs.spectrum.render;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.azure_dike.AzureDikeComponent;
import de.dafuqs.spectrum.azure_dike.AzureDikeProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import vazkii.patchouli.client.RenderHelper;

import static net.minecraft.client.gui.DrawableHelper.GUI_ICONS_TEXTURE;

@Environment(EnvType.CLIENT)
public class HudRenderers {
	
	private static final Text missingInkText = Text.translatable("item.spectrum.placement_staff.tooltip.missing_ink");
	private static final Text noneText = Text.translatable("item.spectrum.placement_staff.tooltip.none_in_inventory");
	
	private static ItemStack itemStackToRender;
	private static int amount;
	private static boolean missingInk;
	
	public static void register() {
		// That one is on Patchouli. ty <3
		HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
			renderSelectedStaffStack(matrixStack);
		});
	}
	
	// this is run in InGameHudMixin instead to render behind the chat and other gui elements
	public static void renderAzureDike(MatrixStack matrixStack, int scaledWidth, int scaledHeight, PlayerEntity cameraPlayer) {
		int charges = AzureDikeProvider.getAzureDikeCharges(cameraPlayer);
		if (charges > 0) {
			int absorptionAmount = MathHelper.ceil(cameraPlayer.getAbsorptionAmount());
			int maxCharges = AzureDikeProvider.getMaxAzureDikeCharges(cameraPlayer);
			boolean blink = false;
			if(cameraPlayer.getRecentDamageSource() != null && cameraPlayer.getWorld() != null) {
				blink = (cameraPlayer.getWorld().getTime() >> 2) % 2 == 0;
			}
			
			int fullCanisters = charges / 20;
			int emptyCanisters = (maxCharges / 20) - fullCanisters;
			int displayedHearts = charges % 20;
			if (displayedHearts == 0) { // if the row is full render it as full instead of wrapping over
				displayedHearts = 20;
				fullCanisters--;
				emptyCanisters++;
			}
			int renderedOutlines = emptyCanisters > 0 ? 10 : ((maxCharges % 20 / 2) + (maxCharges % 2 == 0 ? 0 : 1));
			boolean renderBackRow = fullCanisters > 0;
			
			int maxHealth = (int) cameraPlayer.getMaxHealth();
			int heartRows = maxHealth / 20;
			if(cameraPlayer.getMaxHealth() % 20 == 0) {
				heartRows--;
			}
			boolean hasArmor = cameraPlayer.getArmor() > 0;
			RenderSystem.setShaderTexture(0, AzureDikeComponent.AZURE_DIKE_BAR_TEXTURE);
			
			int width = scaledWidth / 2 - 82;
			int height = scaledHeight - 49;
			
			int y = hasArmor ? height + heartRows * SpectrumCommon.CONFIG.azureDikeHudOffsetYForEachRowOfExtraHearts + SpectrumCommon.CONFIG.azureDikeHudOffsetYWithArmor : height + SpectrumCommon.CONFIG.azureDikeHudOffsetY;
			if(absorptionAmount > 0) {
				int absorptionRows = (int) Math.ceil(absorptionAmount / 20.0F);
				int absorptionRowHeight = 10 - (absorptionRows - 2);
				y -= absorptionRows * Math.max(absorptionRowHeight, 3);
			}
			int x = width - 9 + SpectrumCommon.CONFIG.azureDikeHudOffsetX;
			
			// back row
			if(renderBackRow) {
				for (int i = displayedHearts / 2; i < 10; i++) {
					InGameHud.drawTexture(matrixStack, x + i * 8, y, 36, 9, 9, 9, 256, 256); // "back row" icon
				}
			}
			
			// outline
			for (int i = 0; i < renderedOutlines; i++) {
				if(renderBackRow) {
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
			for (int i = 0; i < displayedHearts; i++) {
				int q = i * 2 + 1;
				if (q < displayedHearts) {
					InGameHud.drawTexture(matrixStack, x + i * 8, y, 18, 9, 9, 9, 256, 256); // full charge icon
				} else if (q == displayedHearts) {
					InGameHud.drawTexture(matrixStack, x + i * 8, y, 27, 9, 9, 9, 256, 256); // half charge icon
				}
			}
			
			// canisters
			for(int i = 0; i < fullCanisters; i++) {
				InGameHud.drawTexture(matrixStack, x + i * 6, y - 9, 0, 0, 9, 9, 256, 256); // full canisters
			}
			for(int i = fullCanisters; i < fullCanisters + emptyCanisters; i++) {
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
