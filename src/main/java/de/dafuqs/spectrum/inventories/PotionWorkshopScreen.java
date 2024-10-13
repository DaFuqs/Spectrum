package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.entity.player.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.awt.*;

public class PotionWorkshopScreen extends HandledScreen<PotionWorkshopScreenHandler> {
	
	public static final Identifier BACKGROUND_3_SLOTS = SpectrumCommon.locate("textures/gui/container/potion_workshop_3_slots.png");
	public static final Identifier BACKGROUND_4_SLOTS = SpectrumCommon.locate("textures/gui/container/potion_workshop_4_slots.png");
	private static final int[] BUBBLE_PROGRESS = new int[]{0, 4, 8, 11, 13, 17, 20, 24, 26, 30, 33, 36, 41};
	
	private final Identifier background;
	
	public PotionWorkshopScreen(PotionWorkshopScreenHandler handler, PlayerInventory playerInventory, Text title) {
		super(handler, playerInventory, title);
		this.backgroundHeight = 202;
		
		if (AdvancementHelper.hasAdvancement(playerInventory.player, SpectrumAdvancements.FOURTH_BREWING_SLOT)) {
			background = BACKGROUND_4_SLOTS;
		} else {
			background = BACKGROUND_3_SLOTS;
		}
	}
	
	@Override
	protected void drawForeground(DrawContext drawContext, int mouseX, int mouseY) {
		// draw "title" and "inventory" texts
		int titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
		int titleY = 6;
		Text title = this.title;
		int playerInventoryX = 8;
		int playerInventoryY = 109;
		
		drawContext.drawText(this.textRenderer, title, titleX, titleY, RenderHelper.GREEN_COLOR, false);
		drawContext.drawText(this.textRenderer, this.playerInventoryTitle, playerInventoryX, playerInventoryY, RenderHelper.GREEN_COLOR, false);
	}
	
	@Override
	protected void drawBackground(DrawContext drawContext, float delta, int mouseX, int mouseY) {
		int startX = (this.width - this.backgroundWidth) / 2;
		int startY = (this.height - this.backgroundHeight) / 2;
		
		// main background
		drawContext.drawTexture(background, startX, startY, 0, 0, backgroundWidth, backgroundHeight);
		
		int brewTime = (this.handler).getBrewTime();
		if (brewTime > 0) {
			// the rising bubbles
			int progress = BUBBLE_PROGRESS[brewTime / 2 % 13];
			if (progress > 0) {
				drawContext.drawTexture(background, startX + 29, startY + 39 + 43 - progress, 176, 40 - progress, 11, progress);
			}
			
			int maxBrewTime = (this.handler).getMaxBrewTime();
			int potionColor = (this.handler).getPotionColor();
			Color color = new Color(potionColor);
			// the brew
			progress = (int) (100.0F * ((float) brewTime / maxBrewTime));
			RenderSystem.setShaderColor(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1.0F);
			drawContext.drawTexture(background, startX + 45, startY + 22, 0, 212, progress, 44);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			
		}
	}
	
	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
		renderBackground(drawContext);
		super.render(drawContext, mouseX, mouseY, delta);
		drawMouseoverTooltip(drawContext, mouseX, mouseY);
	}
	
}