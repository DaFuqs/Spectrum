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
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import vazkii.patchouli.client.RenderHelper;

import static net.minecraft.client.gui.DrawableHelper.GUI_ICONS_TEXTURE;

@Environment(EnvType.CLIENT)
public class GuiOverlay {
	
	private static final TranslatableText noneText = new TranslatableText("item.spectrum.placement_staff.tooltip.none_in_inventory");
	
	private static ItemStack itemStackToRender;
	private static int amount;
	
	public static void register() {
		// That one is on Patchouli. ty <3
		HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
			renderSelectedStaffStack(matrixStack);
			renderAzureDike(matrixStack);
		});
	}
	
	private static void renderAzureDike(MatrixStack matrixStack) {
		Entity entity = MinecraftClient.getInstance().cameraEntity;
		if (entity instanceof LivingEntity livingEntity && !livingEntity.isInvulnerable()) {
			int charges = AzureDikeProvider.getAzureDikeCharges(livingEntity);
			int maxCharges = AzureDikeProvider.getMaxAzureDikeCharges(livingEntity);
			
			if (charges > 0) {
				int v = 9;
				int u = 0;
				
				RenderSystem.setShaderTexture(0, AzureDikeComponent.AZURE_DIKE_BAR_TEXTURE);
				
				Window window = MinecraftClient.getInstance().getWindow();
				int height = window.getScaledHeight() - 49;
				int width = window.getScaledWidth() / 2 + 91;
				
				for (int i = 0; i < maxCharges / 2.0; i++) {
					
					int x;
					int y;
					boolean hasFullAir = livingEntity.getAir() == livingEntity.getMaxAir();
					if (hasFullAir) {
						x = width - i * 8 - 9 + SpectrumCommon.CONFIG.azureDikeHudOffsetX;
						y = height + SpectrumCommon.CONFIG.azureDikeHudOffsetY;
					} else {
						x = width - i * 8 - 9 + SpectrumCommon.CONFIG.azureDikeHudOffsetXLackingAir;
						y = height + SpectrumCommon.CONFIG.azureDikeHudOffsetYLackingAir;
					}
					
					InGameHud.drawTexture(matrixStack, x, y, u, v, 9, 9, 256, 256); // background
				}
				
				for (int i = 0; i < maxCharges; i++) {
					
					int x;
					int y;
					boolean hasFullAir = livingEntity.getAir() == livingEntity.getMaxAir();
					if (hasFullAir) {
						x = width - i * 8 - 9 + SpectrumCommon.CONFIG.azureDikeHudOffsetX;
						y = height + SpectrumCommon.CONFIG.azureDikeHudOffsetY;
					} else {
						x = width - i * 8 - 9 + SpectrumCommon.CONFIG.azureDikeHudOffsetXLackingAir;
						y = height + SpectrumCommon.CONFIG.azureDikeHudOffsetYLackingAir;
					}
					
					if (i * 2 + 1 < charges) {
						InGameHud.drawTexture(matrixStack, x, y, u + 18, v, 9, 9, 256, 256); // full charge icon
					}
					if (i * 2 + 1 == charges) {
						InGameHud.drawTexture(matrixStack, x, y, u + 27, v, 9, 9, 256, 256); // half charge icon
					}
				}
				
				RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
			}
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
			} else {
				MinecraftClient.getInstance().textRenderer.draw(matrixStack, amount + "x", x + 18, y + 19, 0xDDDDDD);
			}
			matrixStack.pop();
		}
	}
	
	public static void setItemStackToRender(ItemStack itemStack, int amount) {
		GuiOverlay.itemStackToRender = itemStack;
		GuiOverlay.amount = amount;
	}
	
	public static void doNotRenderOverlay() {
		GuiOverlay.amount = -1;
	}
	
	
}
