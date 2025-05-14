package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;

import java.awt.*;

public class PotionWorkshopScreen extends AbstractContainerScreen<PotionWorkshopScreenHandler> {
	
	public static final ResourceLocation BACKGROUND_3_SLOTS = SpectrumCommon.locate("textures/gui/container/potion_workshop_3_slots.png");
	public static final ResourceLocation BACKGROUND_4_SLOTS = SpectrumCommon.locate("textures/gui/container/potion_workshop_4_slots.png");
	private static final int[] BUBBLE_PROGRESS = new int[]{0, 4, 8, 11, 13, 17, 20, 24, 26, 30, 33, 36, 41};
	
	private final ResourceLocation background;
	
	public PotionWorkshopScreen(PotionWorkshopScreenHandler handler, Inventory playerInventory, Component title) {
		super(handler, playerInventory, title);
		this.imageHeight = 202;
		
		if (AdvancementHelper.hasAdvancement(playerInventory.player, SpectrumAdvancements.FOURTH_BREWING_SLOT)) {
			background = BACKGROUND_4_SLOTS;
		} else {
			background = BACKGROUND_3_SLOTS;
		}
	}
	
	@Override
	protected void renderLabels(GuiGraphics drawContext, int mouseX, int mouseY) {
		// draw "title" and "inventory" texts
		int titleX = (imageWidth - font.width(title)) / 2;
		int titleY = 6;
		Component title = this.title;
		int playerInventoryX = 8;
		int playerInventoryY = 109;
		
		drawContext.drawString(this.font, title, titleX, titleY, RenderHelper.GREEN_COLOR, false);
		drawContext.drawString(this.font, this.playerInventoryTitle, playerInventoryX, playerInventoryY, RenderHelper.GREEN_COLOR, false);
	}
	
	@Override
	protected void renderBg(GuiGraphics drawContext, float delta, int mouseX, int mouseY) {
		int startX = (this.width - this.imageWidth) / 2;
		int startY = (this.height - this.imageHeight) / 2;
		
		// main background
		drawContext.blit(background, startX, startY, 0, 0, imageWidth, imageHeight);
		
		int brewTime = (this.menu).getBrewTime();
		if (brewTime > 0) {
			// the rising bubbles
			int progress = BUBBLE_PROGRESS[brewTime / 2 % 13];
			if (progress > 0) {
				drawContext.blit(background, startX + 29, startY + 39 + 43 - progress, 176, 40 - progress, 11, progress);
			}
			
			int maxBrewTime = (this.menu).getMaxBrewTime();
			int potionColor = (this.menu).getPotionColor();
			Color color = new Color(potionColor);
			// the brew
			progress = (int) (100.0F * ((float) brewTime / maxBrewTime));
			RenderSystem.setShaderColor(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1.0F);
			drawContext.blit(background, startX + 45, startY + 22, 0, 212, progress, 44);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			
		}
	}
	
	@Override
	public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
		renderBackground(drawContext, mouseX, mouseY, delta);
		super.render(drawContext, mouseX, mouseY, delta);
		renderTooltip(drawContext, mouseX, mouseY);
	}
	
}