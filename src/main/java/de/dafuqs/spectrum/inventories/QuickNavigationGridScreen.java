package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.Options;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import javax.annotation.*;
import org.joml.*;

import java.util.*;

public class QuickNavigationGridScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
	
	public static final int TEXT_COLOR = 0xEEEEEE;
	public static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(SpectrumCommon.MOD_ID, "textures/gui/quick_navigation.png");
	
	protected static final Component CONTROLS_TEXT_1 = Component.translatable("gui.spectrum.quick_navigation.controls1");
	protected static final Component CONTROLS_TEXT_2 = Component.translatable("gui.spectrum.quick_navigation.controls2");
	
	private static final List<Tuple<Integer, Integer>> SQUARE_OFFSETS = List.of(
			new Tuple<>(-20, -80),
			new Tuple<>(40, -20),
			new Tuple<>(-20, 40),
			new Tuple<>(-80, -20)
	);
	
	public enum GUISelection {
		UP,
		RIGHT,
		DOWN,
		LEFT,
		SELECT,
		BACK
	}
	
	public static class Grid {
		
		private final GridEntry centerEntry;
		private final GridEntry topEntry;
		private final GridEntry rightEntry;
		private final GridEntry bottomEntry;
		private final GridEntry leftEntry;
		
		public Grid(GridEntry centerEntry, GridEntry topEntry, GridEntry rightEntry, GridEntry bottomEntry, GridEntry leftEntry) {
			this.centerEntry = centerEntry;
			this.topEntry = topEntry;
			this.rightEntry = rightEntry;
			this.bottomEntry = bottomEntry;
			this.leftEntry = leftEntry;
		}
		
		void navigate(GUISelection direction, QuickNavigationGridScreen<?> screen) {
			switch (direction) {
				case BACK -> screen.back();
				case SELECT -> centerEntry.whenSelected(screen);
				case UP -> topEntry.whenSelected(screen);
				case RIGHT -> rightEntry.whenSelected(screen);
				case DOWN -> bottomEntry.whenSelected(screen);
				default -> leftEntry.whenSelected(screen);
			}
		}
		
		void drawForeground(Screen screen, GuiGraphics drawContext, int startX, int startY) {
			centerEntry.drawSmallForeground(screen, drawContext, startX - 15, startY - 15);
			
			topEntry.drawBigForeground(screen, drawContext, startX + SQUARE_OFFSETS.get(0).getA(), startY + SQUARE_OFFSETS.get(0).getB());
			rightEntry.drawBigForeground(screen, drawContext, startX + SQUARE_OFFSETS.get(1).getA(), startY + SQUARE_OFFSETS.get(1).getB());
			bottomEntry.drawBigForeground(screen, drawContext, startX + SQUARE_OFFSETS.get(2).getA(), startY + SQUARE_OFFSETS.get(2).getB());
			leftEntry.drawBigForeground(screen, drawContext, startX + SQUARE_OFFSETS.get(3).getA(), startY + SQUARE_OFFSETS.get(3).getB());
		}
		
		void drawBackground(Screen screen, GuiGraphics drawContext, int startX, int startY) {
			centerEntry.drawSmallBackground(screen, drawContext, startX - 15, startY - 15);
			
			topEntry.drawBigBackground(screen, drawContext, startX + SQUARE_OFFSETS.get(0).getA(), startY + SQUARE_OFFSETS.get(0).getB());
			rightEntry.drawBigBackground(screen, drawContext, startX + SQUARE_OFFSETS.get(1).getA(), startY + SQUARE_OFFSETS.get(1).getB());
			bottomEntry.drawBigBackground(screen, drawContext, startX + SQUARE_OFFSETS.get(2).getA(), startY + SQUARE_OFFSETS.get(2).getB());
			leftEntry.drawBigBackground(screen, drawContext, startX + SQUARE_OFFSETS.get(3).getA(), startY + SQUARE_OFFSETS.get(3).getB());
		}
		
	}
	
	public abstract static class GridEntry {
		
		public static final GridEntry CLOSE = GridEntry.empty(QuickNavigationGridScreen::onClose);
		public static final GridEntry BACK = GridEntry.empty(QuickNavigationGridScreen::back);
		public static final GridEntry EMPTY = GridEntry.empty(null);
		
		public interface GridEntryCallback {
			void whenSelected(QuickNavigationGridScreen<?> screen);
		}
		
		protected final Component text;
		protected final @Nullable GridEntryCallback onClickCallback;
		
		protected GridEntry(Component text, @Nullable GridEntry.GridEntryCallback onClickCallback) {
			this.text = text;
			this.onClickCallback = onClickCallback;
		}
		
		public static GridEntry empty(@Nullable GridEntryCallback callback) {
			return new EmptyGridEntry(callback);
		}
		
		public static GridEntry textured(int textureStartX, int textureStartY, Component text, @Nullable GridEntryCallback callback) {
			return new TexturedGridEntry(textureStartX, textureStartY, text, callback);
		}
		
		public static GridEntry text(Component innerText, Component text, @Nullable GridEntryCallback callback) {
			return new TextGridEntry(innerText, text, callback);
		}
		
		public static GridEntry colored(Vector3f color, Component text, @Nullable GridEntryCallback callback) {
			return new ColoredGridEntry(color, text, callback);
		}
		
		public static GridEntry item(Item item, Component text, @Nullable GridEntryCallback callback) {
			return new ItemGridEntry(item, text, callback);
		}
		
		public void whenSelected(QuickNavigationGridScreen<?> screen) {
			if (this.onClickCallback != null) {
				this.onClickCallback.whenSelected(screen);
			}
		}
		
		void drawBigBackground(Screen screen, GuiGraphics drawContext, int startX, int startY) {
			drawContext.blit(BACKGROUND, startX, startY, 10, 0, 38, 38);
		}
		
		void drawSmallBackground(Screen screen, GuiGraphics drawContext, int startX, int startY) {
			drawContext.blit(BACKGROUND, startX, startY, 48, 0, 28, 28);
		}
		
		void drawBigForeground(Screen screen, GuiGraphics drawContext, int startX, int startY) {
			Minecraft client = Minecraft.getInstance();
			drawContext.drawCenteredString(client.font, this.text, startX + 19, startY + 40, TEXT_COLOR);
		}
		
		void drawSmallForeground(Screen screen, GuiGraphics drawContext, int startX, int startY) {
			Minecraft client = Minecraft.getInstance();
			drawContext.drawCenteredString(client.font, this.text, startX + 14, startY + 34, TEXT_COLOR);
		}
		
	}
	
	public static class EmptyGridEntry extends GridEntry {
		protected EmptyGridEntry(@Nullable GridEntry.GridEntryCallback onClickCallback) {
			super(Component.empty(), onClickCallback);
		}
		
		@Override
		void drawBigBackground(Screen screen, GuiGraphics drawContext, int startX, int startY) {
		}
		
		@Override
		void drawSmallBackground(Screen screen, GuiGraphics drawContext, int startX, int startY) {
			drawContext.blit(BACKGROUND, startX + 9, startY + 9, 0, 0, 10, 10);
		}
		
		@Override
		void drawBigForeground(Screen screen, GuiGraphics drawContext, int startX, int startY) {
		}
		
		@Override
		void drawSmallForeground(Screen screen, GuiGraphics drawContext, int startX, int startY) {
		}
	}
	
	public static class ColoredGridEntry extends GridEntry {
		protected final Vector3f color;
		
		private ColoredGridEntry(Vector3f color, Component text, @Nullable GridEntry.GridEntryCallback callback) {
			super(text, callback);
			this.color = color;
		}
		
		@Override
		void drawBigForeground(Screen screen, GuiGraphics drawContext, int startX, int startY) {
			super.drawBigForeground(screen, drawContext, startX, startY);
			RenderHelper.fillQuad(drawContext.pose(), startX + 3, startY + 3, 32, 32, color);
		}
		
		@Override
		void drawSmallForeground(Screen screen, GuiGraphics drawContext, int startX, int startY) {
			super.drawSmallForeground(screen, drawContext, startX, startY);
			RenderHelper.fillQuad(drawContext.pose(), startX + 2, startY + 2, 24, 24, color);
		}
		
	}
	
	public static class TexturedGridEntry extends GridEntry {
		
		protected final int textureStartX;
		protected final int textureStartY;
		
		private TexturedGridEntry(int textureStartX, int textureStartY, Component text, @Nullable GridEntry.GridEntryCallback callback) {
			super(text, callback);
			this.textureStartX = textureStartX;
			this.textureStartY = textureStartY;
		}
		
		@Override
		void drawBigBackground(Screen screen, GuiGraphics drawContext, int startX, int startY) {
			super.drawBigBackground(screen, drawContext, startX, startY);
			drawContext.blit(BACKGROUND, startX + 11, startY + 11, textureStartX, textureStartY, 20, 20);
		}
		
		@Override
		void drawSmallBackground(Screen screen, GuiGraphics drawContext, int startX, int startY) {
			super.drawSmallBackground(screen, drawContext, startX, startY);
			drawContext.blit(BACKGROUND, startX, startY, textureStartX, textureStartY, 20, 20);
		}
		
	}
	
	public static class TextGridEntry extends GridEntry {
		
		protected final Component innerText;
		protected final int innerHalfTextWidth;
		
		private TextGridEntry(Component innerText, Component text, @Nullable GridEntry.GridEntryCallback callback) {
			super(text, callback);
			Minecraft client = Minecraft.getInstance();
			this.innerText = innerText;
			this.innerHalfTextWidth = client.font.width(this.innerText) / 2;
		}
		
		@Override
		void drawBigForeground(Screen screen, GuiGraphics drawContext, int startX, int startY) {
			Minecraft client = Minecraft.getInstance();
			drawContext.drawCenteredString(client.font, this.innerText, startX + 19, startY + 15, TEXT_COLOR);
		}
		
		@Override
		void drawSmallForeground(Screen screen, GuiGraphics drawContext, int startX, int startY) {
			Minecraft client = Minecraft.getInstance();
			drawContext.drawCenteredString(client.font, this.innerText, startX + 14, startY + 10, TEXT_COLOR);
		}
	}
	
	private static class ItemGridEntry extends GridEntry {
		
		protected final ItemStack stack;
		
		private ItemGridEntry(Item item, Component text, @Nullable GridEntry.GridEntryCallback callback) {
			super(text, callback);
			this.stack = item.getDefaultInstance();
		}
		
		@Override
		void drawBigForeground(Screen screen, GuiGraphics drawContext, int startX, int startY) {
			super.drawBigForeground(screen, drawContext, startX, startY);
			drawContext.renderItem(stack, startX + 10, startY + 10);
		}
		
		@Override
		void drawSmallForeground(Screen screen, GuiGraphics drawContext, int startX, int startY) {
			super.drawSmallForeground(screen, drawContext, startX, startY);
			drawContext.renderItem(stack, startX + 5, startY + 5);
		}
		
	}
	
	public final Stack<Grid> gridStack = new Stack<>();
	
	public QuickNavigationGridScreen(T handler, Inventory playerInventory, Component title) {
		super(handler, playerInventory, title);
		this.imageHeight = 256;
	}
	
	private void back() {
		minecraft.level.playSound(null, minecraft.player.blockPosition(), SpectrumSoundEvents.PAINTBRUSH_SWITCH, SoundSource.NEUTRAL, 0.5F, 1.0F);
		if (gridStack.size() == 1) {
			minecraft.player.closeContainer();
		} else {
			gridStack.pop();
		}
	}
	
	protected void selectGrid(Grid grid) {
		minecraft.level.playSound(null, minecraft.player.blockPosition(), SpectrumSoundEvents.PAINTBRUSH_SWITCH, SoundSource.NEUTRAL, 0.5F, 1.0F);
		gridStack.push(grid);
	}
	
	public Grid current() {
		return gridStack.peek();
	}
	
	@Override
	protected void renderLabels(GuiGraphics drawContext, int mouseX, int mouseY) {
		current().drawBackground(this, drawContext, imageWidth / 2, imageHeight / 2);
		current().drawForeground(this, drawContext, imageWidth / 2, imageHeight / 2);
		
		drawContext.drawCenteredString(this.font, CONTROLS_TEXT_1, imageWidth / 2, 228, TEXT_COLOR);
		drawContext.drawCenteredString(this.font, CONTROLS_TEXT_2, imageWidth / 2, 238, TEXT_COLOR);
	}
	
	@Override
	protected void renderBg(GuiGraphics drawContext, float delta, int mouseX, int mouseY) {
	
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 1) {
			current().navigate(GUISelection.BACK, this);
		} else {
			int startX = imageWidth / 2;
			int startY = imageHeight / 2;
			
			mouseX = mouseX - leftPos;
			mouseY = mouseY - topPos;
			
			int centerElementSize = gridStack.size() == 1 ? 5 : 14;
			if (mouseX >= startX - centerElementSize && mouseX <= startX + centerElementSize && mouseY >= startY - centerElementSize && mouseY <= startY + centerElementSize) {
				current().navigate(GUISelection.SELECT, this);
				return true;
			}
			
			int offsetID = 0;
			for (Tuple<Integer, Integer> offset : SQUARE_OFFSETS) {
				if (mouseX >= startX + offset.getA() && mouseX <= startX + offset.getA() + 32 && mouseY >= startY + offset.getB() && mouseY <= startY + offset.getB() + 32) {
					switch (offsetID) {
						case 0 -> {
							current().navigate(GUISelection.UP, this);
							return true;
						}
						case 1 -> {
							current().navigate(GUISelection.RIGHT, this);
							return true;
						}
						case 2 -> {
							current().navigate(GUISelection.DOWN, this);
							return true;
						}
						case 3 -> {
							current().navigate(GUISelection.LEFT, this);
							return true;
						}
					}
				}
				offsetID++;
			}
		}
		
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		Minecraft client = Minecraft.getInstance();
		Options options = client.options;
		if (options.keyLeft.matches(keyCode, scanCode)) {
			current().navigate(GUISelection.LEFT, this);
			return true;
		} else if (options.keyUp.matches(keyCode, scanCode)) {
			current().navigate(GUISelection.UP, this);
			return true;
		} else if (options.keyRight.matches(keyCode, scanCode)) {
			current().navigate(GUISelection.RIGHT, this);
			return true;
		} else if (options.keyDown.matches(keyCode, scanCode)) {
			current().navigate(GUISelection.DOWN, this);
			return true;
		} else if (options.keyDrop.matches(keyCode, scanCode) || options.keyInventory.matches(keyCode, scanCode)) {
			current().navigate(GUISelection.SELECT, this);
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
}
