package de.dafuqs.spectrum.items.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class SpectrumTooltipComponent implements TooltipComponent {
	
	public static final Identifier TEXTURE = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/spectrum_tooltips.png");
	
	@Override
	public int getHeight() {
		return 0;
	}
	
	@Override
	public int getWidth(TextRenderer textRenderer) {
		return 0;
	}
	
	public void drawOutline(int x, int y, int columns, int rows, MatrixStack matrices, int z) {
		this.draw(matrices, x, y, z, Sprite.BORDER_CORNER_TOP);
		this.draw(matrices, x + columns * 18 + 1, y, z, Sprite.BORDER_CORNER_TOP);
		
		int j;
		for (j = 0; j < columns; ++j) {
			this.draw(matrices, x + 1 + j * 18, y, z, Sprite.BORDER_HORIZONTAL_TOP);
			this.draw(matrices, x + 1 + j * 18, y + rows * 20, z, Sprite.BORDER_HORIZONTAL_BOTTOM);
		}
		
		for (j = 0; j < rows; ++j) {
			this.draw(matrices, x, y + j * 20 + 1, z, Sprite.BORDER_VERTICAL);
			this.draw(matrices, x + columns * 18 + 1, y + j * 20 + 1, z, Sprite.BORDER_VERTICAL);
		}
		
		this.draw(matrices, x, y + rows * 20, z, Sprite.BORDER_CORNER_BOTTOM);
		this.draw(matrices, x + columns * 18 + 1, y + rows * 20, z, Sprite.BORDER_CORNER_BOTTOM);
	}
	
	public void drawSlot(int x, int y, int index, ItemStack itemStack, TextRenderer textRenderer, MatrixStack matrices, @NotNull ItemRenderer itemRenderer, int z) {
		this.draw(matrices, x, y, z, Sprite.SLOT);
		
		itemRenderer.renderInGuiWithOverrides(itemStack, x + 1, y + 1, index);
		itemRenderer.renderGuiItemOverlay(textRenderer, itemStack, x + 1, y + 1);
		if (index == 0) {
			HandledScreen.drawSlotHighlight(matrices, x + 1, y + 1, z);
		}
	}
	
	public void drawDottedSlot(int x, int y, MatrixStack matrices, int z) {
		this.draw(matrices, x, y, z, Sprite.DOTTED_SLOT);
	}
	
	private void draw(MatrixStack matrices, int x, int y, int z, @NotNull Sprite sprite) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		DrawableHelper.drawTexture(matrices, x, y, z, (float) sprite.u, (float) sprite.v, sprite.width, sprite.height, 128, 128);
	}
	
	@Environment(EnvType.CLIENT)
	public enum Sprite {
		SLOT(0, 0, 18, 20),
		DOTTED_SLOT(18, 0, 18 + 18, 20),
		BLOCKED_SLOT(0, 40, 18, 20),
		BORDER_VERTICAL(0, 18, 1, 20),
		BORDER_HORIZONTAL_TOP(0, 20, 18, 1),
		BORDER_HORIZONTAL_BOTTOM(0, 60, 18, 1),
		BORDER_CORNER_TOP(0, 20, 1, 1),
		BORDER_CORNER_BOTTOM(0, 60, 1, 1);
		
		public final int u;
		public final int v;
		public final int width;
		public final int height;
		
		Sprite(int u, int v, int width, int height) {
			this.u = u;
			this.v = v;
			this.width = width;
			this.height = height;
		}
	}
}
