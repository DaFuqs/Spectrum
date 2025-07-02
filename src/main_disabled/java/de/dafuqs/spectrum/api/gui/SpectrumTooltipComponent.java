package de.dafuqs.spectrum.api.gui;

import de.dafuqs.spectrum.*;
import net.fabricmc.api.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.client.gui.screens.inventory.tooltip.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

public interface SpectrumTooltipComponent extends ClientTooltipComponent {
	
	ResourceLocation TEXTURE = SpectrumCommon.locate("textures/gui/container/spectrum_tooltips.png");
	
	static void drawOutline(GuiGraphics context, int x, int y, int columns, int rows) {
		draw(context, x, y, Sprite.BORDER_CORNER_TOP);
		draw(context, x + columns * 18 + 1, y, Sprite.BORDER_CORNER_TOP);
		
		int j;
		for (j = 0; j < columns; ++j) {
			draw(context, x + 1 + j * 18, y, Sprite.BORDER_HORIZONTAL_TOP);
			draw(context, x + 1 + j * 18, y + rows * 20, Sprite.BORDER_HORIZONTAL_BOTTOM);
		}
		
		for (j = 0; j < rows; ++j) {
			draw(context, x, y + j * 20 + 1, Sprite.BORDER_VERTICAL);
			draw(context, x + columns * 18 + 1, y + j * 20 + 1, Sprite.BORDER_VERTICAL);
		}
		
		draw(context, x, y + rows * 20, Sprite.BORDER_CORNER_BOTTOM);
		draw(context, x + columns * 18 + 1, y + rows * 20, Sprite.BORDER_CORNER_BOTTOM);
	}
	
	static void drawSlot(GuiGraphics context, int x, int y, int index, ItemStack itemStack, Font textRenderer) {
		draw(context, x, y, Sprite.SLOT);
		
		context.renderItem(itemStack, x + 1, y + 1, index);
		context.renderItemDecorations(textRenderer, itemStack, x + 1, y + 1);
		if (index == 0) {
			AbstractContainerScreen.renderSlotHighlight(context, x + 1, y + 1, 0);
		}
	}
	
	static void drawDottedSlot(GuiGraphics context, int x, int y) {
		draw(context, x, y, Sprite.DOTTED_SLOT);
	}
	
	private static void draw(GuiGraphics context, int x, int y, @NotNull Sprite sprite) {
		context.blit(TEXTURE, x, y, sprite.u, sprite.v, sprite.width, sprite.height, 128, 128);
	}
	
	@Environment(EnvType.CLIENT)
	enum Sprite {
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
