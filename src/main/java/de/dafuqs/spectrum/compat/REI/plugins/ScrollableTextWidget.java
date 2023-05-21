package de.dafuqs.spectrum.compat.REI.plugins;

import me.shedaniel.clothconfig2.*;
import me.shedaniel.clothconfig2.api.*;
import me.shedaniel.clothconfig2.api.scroll.ScrollingContainer;
import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.util.math.*;
import net.minecraft.text.*;

import java.util.*;

/**
 * Mostly a copy of REIs ScrollableTextWidget
 * But since that is private here a public implementation
 * <a href="https://github.com/shedaniel/RoughlyEnoughItems/blob/7.x-1.18/default-plugin/src/main/java/me/shedaniel/rei/plugin/client/categories/DefaultInformationCategory.java">REI</a>
 */
public class ScrollableTextWidget extends WidgetWithBounds {
	private final Rectangle bounds;
	private final List<OrderedText> texts;
	
	private final ScrollingContainer scrolling = new ScrollingContainer() {
		@Override
		public me.shedaniel.math.Rectangle getBounds() {
			Rectangle bounds = ScrollableTextWidget.this.getBounds();
			return new Rectangle(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2);
		}
		
		@Override
		public int getMaxScrollHeight() {
			int i = 2;
			for (OrderedText entry : texts) {
				i += entry == null ? 4 : font.fontHeight;
			}
			return i;
		}
	};
	
	public ScrollableTextWidget(Rectangle bounds, List<OrderedText> texts) {
		this.bounds = Objects.requireNonNull(bounds);
		this.texts = texts;
	}
	
	@Override
	public boolean mouseScrolled(double double_1, double double_2, double double_3) {
		if (containsMouse(double_1, double_2)) {
			scrolling.offset(ClothConfigInitializer.getScrollStep() * -double_3, true);
			return true;
		}
		return false;
	}
	
	@Override
	public List<? extends Element> children() {
		return Collections.emptyList();
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (scrolling.updateDraggingState(mouseX, mouseY, button))
			return true;
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (scrolling.mouseDragged(mouseX, mouseY, button, deltaX, deltaY))
			return true;
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}
	
	@Override
	public Rectangle getBounds() {
		return bounds;
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		scrolling.updatePosition(delta);
		Rectangle innerBounds = scrolling.getScissorBounds();
		ScissorsHandler.INSTANCE.scissor(innerBounds);
		int currentY = -scrolling.scrollAmountInt() + innerBounds.y;
		for (OrderedText text : texts) {
			if (text != null && currentY + font.fontHeight >= innerBounds.y && currentY <= innerBounds.getMaxY()) {
				font.draw(matrices, text, innerBounds.x + 2, currentY + 2, REIRuntime.getInstance().isDarkThemeEnabled() ? 0xFFBBBBBB : 0xFF090909);
			}
			currentY += text == null ? 4 : font.fontHeight;
		}
		ScissorsHandler.INSTANCE.removeLastScissor();
		ScissorsHandler.INSTANCE.scissor(scrolling.getBounds());
		scrolling.renderScrollBar(0xff000000, 1, REIRuntime.getInstance().isDarkThemeEnabled() ? 0.8f : 1f);
		ScissorsHandler.INSTANCE.removeLastScissor();
	}
	
}