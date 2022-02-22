package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.blocks.chests.CompactingChestBlockEntity;
import de.dafuqs.spectrum.blocks.potion_workshop.PotionWorkshopBlockEntity;
import de.dafuqs.spectrum.networking.SpectrumC2SPackets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class PotionWorkshopScreen extends HandledScreen<PotionWorkshopScreenHandler> {

	public static final Identifier BACKGROUND_3_SLOTS = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/potion_workshop_3_slots.png");
	public static final Identifier BACKGROUND_4_SLOTS = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/potion_workshop_4_slots.png");
	
	private final Identifier background;
	
	private static final int[] BUBBLE_PROGRESS = new int[]{29, 24, 20, 16, 11, 6, 0};
	
	public PotionWorkshopScreen(PotionWorkshopScreenHandler handler, PlayerInventory playerInventory, Text title) {
		super(handler, playerInventory, title);
		this.backgroundHeight = 202;
		
		if(Support.hasAdvancement(playerInventory.player, PotionWorkshopBlockEntity.FOURTH_BREWING_SLOT_ADVANCEMENT_IDENTIFIER)) {
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
		
		// main background
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
		
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;
		int m = (this.handler).getBrewTime();
		if (m > 0) {
			int n = (int)(28.0F * (1.0F - (float)m / 400.0F));
			// the brew
			if (n > 0) {
				this.drawTexture(matrices, i + 97, j + 16, 176, 0, 9, n);
			}
			
			// the rising bubbles
			n = BUBBLE_PROGRESS[m / 2 % 7];
			if (n > 0) {
				this.drawTexture(matrices, i + 63, j + 14 + 29 - n, 185, 29 - n, 12, n);
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