package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.potion_workshop.PotionWorkshopBlockEntity;
import de.dafuqs.spectrum.helpers.Support;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;

public class PotionWorkshopScreen extends HandledScreen<PotionWorkshopScreenHandler> {
	
	public static final Identifier BACKGROUND_3_SLOTS = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/potion_workshop_3_slots.png");
	public static final Identifier BACKGROUND_4_SLOTS = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/potion_workshop_4_slots.png");
	private static final int[] BUBBLE_PROGRESS = new int[]{0, 4, 8, 11, 13, 17, 20, 24, 26, 30, 33, 36, 41};
	
	private final Identifier background;
	
	public PotionWorkshopScreen(PotionWorkshopScreenHandler handler, PlayerInventory playerInventory, Text title) {
		super(handler, playerInventory, title);
		this.backgroundHeight = 202;
		
		if (Support.hasAdvancement(playerInventory.player, PotionWorkshopBlockEntity.FOURTH_BREWING_SLOT_ADVANCEMENT_IDENTIFIER)) {
			background = BACKGROUND_4_SLOTS;
		} else {
			background = BACKGROUND_3_SLOTS;
		}
	}
	
	@Override
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
		// draw "title" and "inventory" texts
		int titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2; // 8;
		int titleY = 6;
		Text title = this.title;
		int playerInventoryX = 8;
		int playerInventoryY = 109;
		
		this.textRenderer.draw(matrices, title, titleX, titleY, 3289650);
		this.textRenderer.draw(matrices, this.playerInventoryTitle, playerInventoryX, playerInventoryY, 3289650);
	}
	
	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, background);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		
		int startX = (this.width - this.backgroundWidth) / 2;
		int startY = (this.height - this.backgroundHeight) / 2;
		
		// main background
		drawTexture(matrices, startX, startY, 0, 0, backgroundWidth, backgroundHeight);
		
		int brewTime = (this.handler).getBrewTime();
		if (brewTime > 0) {
			// the rising bubbles
			int n = BUBBLE_PROGRESS[brewTime / 2 % 13];
			if (n > 0) {
				this.drawTexture(matrices, startX + 29, startY + 39 + 43 - n, 176, 40 - n, 11, n);
			}
			
			int maxBrewTime = (this.handler).getMaxBrewTime();
			int potionColor = (this.handler).getPotionColor();
			Color color = new Color(potionColor);
			RenderSystem.setShaderColor(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1.0F);
			n = (int) (100.0F * ((float) brewTime / maxBrewTime));
			// the brew
			if (n > 0) {
				this.drawTexture(matrices, startX + 45, startY + 22, 0, 212, n, 44);
			}
		}
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		drawMouseoverTooltip(matrices, mouseX, mouseY);
	}
	
}