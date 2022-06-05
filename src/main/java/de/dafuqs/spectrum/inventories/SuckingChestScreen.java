package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SuckingChestScreen extends HandledScreen<SuckingChestScreenHandler> {
	
	public static final Identifier BACKGROUND = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/black_hole_chest.png");
	
	public SuckingChestScreen(SuckingChestScreenHandler handler, PlayerInventory playerInventory, Text title) {
		super(handler, playerInventory, title);
		this.backgroundHeight = 193;
	}
	
	protected void init() {
		super.init();
	}
	
	@Override
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
		// draw "title" and "inventory" texts
		int titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
		int titleY = 6;
		Text title = this.title;
		int inventoryX = 8;
		int intInventoryY = 102;
		
		this.textRenderer.draw(matrices, title, titleX, titleY, 3289650);
		this.textRenderer.draw(matrices, this.playerInventoryTitle, inventoryX, intInventoryY, 3289650);
	}
	
	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, BACKGROUND);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		
		/*if(mouseX > x+153 && mouseX < x+153+16 && mouseY > y+5 && mouseY < y+5+16) {
			this.renderTooltip(matrices, new TranslatableText("block.spectrum.compacting_chest.toggle_crafting_mode"), mouseX, mouseY);
		} else {
		
		}*/
		drawMouseoverTooltip(matrices, mouseX, mouseY);
	}
	
}