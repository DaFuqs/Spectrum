package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.option.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.screen.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;
import org.joml.*;

import java.util.*;

// TODO - No colors. Intentional placeholder? Consider implementing.
public class QuickNavigationGridScreen<QuickNavigationGridScreenHandler extends ScreenHandler> extends HandledScreen<QuickNavigationGridScreenHandler> {
	
	public static final int TEXT_COLOR = 0xEEEEEE;
	public static final Identifier BACKGROUND = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/quick_navigation.png");
	
	protected static final Text CONTROLS_TEXT_1 = Text.translatable("gui.spectrum.quick_navigation.controls1");
	protected static final Text CONTROLS_TEXT_2 = Text.translatable("gui.spectrum.quick_navigation.controls2");
	
	private static final List<Pair<Integer, Integer>> SQUARE_OFFSETS = List.of(
			new Pair<>(-20, -80),
			new Pair<>(40, -20),
			new Pair<>(-20, 40),
			new Pair<>(-80, -20)
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
		
		void navigate(GUISelection direction, QuickNavigationGridScreen screen) {
			switch (direction) {
				case BACK -> screen.back();
				case SELECT -> centerEntry.whenSelected(screen);
				case UP -> topEntry.whenSelected(screen);
				case RIGHT -> rightEntry.whenSelected(screen);
				case DOWN -> bottomEntry.whenSelected(screen);
				default -> leftEntry.whenSelected(screen);
			}
		}
		
		void drawForeground(Screen screen, MatrixStack matrices, int startX, int startY) {
			centerEntry.drawSmallForeground(screen, matrices, startX - 15, startY - 15);

			topEntry.drawBigForeground(screen, matrices, startX + SQUARE_OFFSETS.get(0).getLeft(), startY + SQUARE_OFFSETS.get(0).getRight());
			rightEntry.drawBigForeground(screen, matrices, startX + SQUARE_OFFSETS.get(1).getLeft(), startY + SQUARE_OFFSETS.get(1).getRight());
			bottomEntry.drawBigForeground(screen, matrices, startX + SQUARE_OFFSETS.get(2).getLeft(), startY + SQUARE_OFFSETS.get(2).getRight());
			leftEntry.drawBigForeground(screen, matrices, startX + SQUARE_OFFSETS.get(3).getLeft(), startY + SQUARE_OFFSETS.get(3).getRight());
		}

		void drawBackground(Screen screen, MatrixStack matrices, int startX, int startY) {
			RenderSystem.setShader(GameRenderer::getPositionTexProgram);
			RenderSystem.setShaderTexture(0, BACKGROUND);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			
			centerEntry.drawSmallBackground(screen, matrices, startX - 15, startY - 15);

			topEntry.drawBigBackground(screen, matrices, startX + SQUARE_OFFSETS.get(0).getLeft(), startY + SQUARE_OFFSETS.get(0).getRight());
			rightEntry.drawBigBackground(screen, matrices, startX + SQUARE_OFFSETS.get(1).getLeft(), startY + SQUARE_OFFSETS.get(1).getRight());
			bottomEntry.drawBigBackground(screen, matrices, startX + SQUARE_OFFSETS.get(2).getLeft(), startY + SQUARE_OFFSETS.get(2).getRight());
			leftEntry.drawBigBackground(screen, matrices, startX + SQUARE_OFFSETS.get(3).getLeft(), startY + SQUARE_OFFSETS.get(3).getRight());
		}
		
	}
	
	public abstract static class GridEntry {
		
		public static final GridEntry CLOSE = GridEntry.empty(QuickNavigationGridScreen::close);
		public static final GridEntry BACK = GridEntry.empty(QuickNavigationGridScreen::back);
		public static final GridEntry EMPTY = GridEntry.empty(null);
		
		public interface GridEntryCallback {
			void whenSelected(QuickNavigationGridScreen screen);
		}
		
		protected final Text text;
		protected final int halfTextWidth;
		protected final @Nullable GridEntryCallback onClickCallback;

		protected GridEntry(String text, @Nullable GridEntry.GridEntryCallback onClickCallback) {
			this.text = Text.translatable(text);
			this.halfTextWidth = MinecraftClient.getInstance().textRenderer.getWidth(this.text) / 2;
			this.onClickCallback = onClickCallback;
		}

		public static GridEntry empty(@Nullable GridEntryCallback callback) {
			return new EmptyGridEntry(callback);
		}

		public static GridEntry textured(int textureStartX, int textureStartY, String text, @Nullable GridEntryCallback callback) {
			return new TexturedGridEntry(textureStartX, textureStartY, text, callback);
		}

		public static GridEntry text(Text innerText, String text, @Nullable GridEntryCallback callback) {
			return new TextGridEntry(innerText, text, callback);
		}

		public static GridEntry colored(Vector3f color, String text, @Nullable GridEntryCallback callback) {
			return new ColoredGridEntry(color, text, callback);
		}

		public static GridEntry item(Item item, String text, @Nullable GridEntryCallback callback) {
			return new ItemGridEntry(item, text, callback);
		}

		public void whenSelected(QuickNavigationGridScreen screen) {
			if (this.onClickCallback != null) {
				this.onClickCallback.whenSelected(screen);
			}
		}

		void drawBigBackground(Screen screen, MatrixStack matrices, int startX, int startY) {
			screen.drawTexture(matrices, startX, startY, 10, 0, 38, 38);
		}

		void drawSmallBackground(Screen screen, MatrixStack matrices, int startX, int startY) {
			screen.drawTexture(matrices, startX, startY, 48, 0, 28, 28);
		}

		void drawBigForeground(Screen screen, MatrixStack matrices, int startX, int startY) {
			MinecraftClient.getInstance().textRenderer.draw(matrices, this.text, startX + 19 - halfTextWidth, startY + 40, TEXT_COLOR);
		}
		
		void drawSmallForeground(Screen screen, MatrixStack matrices, int startX, int startY) {
			MinecraftClient.getInstance().textRenderer.draw(matrices, this.text, startX + 14 - halfTextWidth, startY + 34, TEXT_COLOR);
		}

	}

	public static class EmptyGridEntry extends GridEntry {
		protected EmptyGridEntry(@Nullable GridEntry.GridEntryCallback onClickCallback) {
			super("", onClickCallback);
		}

		@Override
		void drawBigBackground(Screen screen, MatrixStack matrices, int startX, int startY) {
		}

		@Override
		void drawSmallBackground(Screen screen, MatrixStack matrices, int startX, int startY) {
			screen.drawTexture(matrices, startX + 9, startY + 9, 0, 0, 10, 10);
		}

		@Override
		void drawBigForeground(Screen screen, MatrixStack matrices, int startX, int startY) {
		}

		@Override
		void drawSmallForeground(Screen screen, MatrixStack matrices, int startX, int startY) {
		}
	}

	public static class ColoredGridEntry extends GridEntry {
		protected final Vector3f color;

		private ColoredGridEntry(Vector3f color, String text, GridEntry.GridEntryCallback callback) {
			super(text, callback);
			this.color = color;
		}
		
		@Override
		void drawBigForeground(Screen screen, MatrixStack matrices, int startX, int startY) {
			super.drawBigForeground(screen, matrices, startX, startY);
			RenderHelper.fillQuad(matrices, startX + 3, startY + 3, 32, 32, color);
		}
		
		@Override
		void drawSmallForeground(Screen screen, MatrixStack matrices, int startX, int startY) {
			super.drawSmallForeground(screen, matrices, startX, startY);
			RenderHelper.fillQuad(matrices, startX + 2, startY + 2, 24, 24, color);
		}

	}

	public static class TexturedGridEntry extends GridEntry {

		protected final int textureStartX;
		protected final int textureStartY;
		private TexturedGridEntry(int textureStartX, int textureStartY, @Nullable String text, GridEntry.GridEntryCallback callback) {
			super(text, callback);
			this.textureStartX = textureStartX;
			this.textureStartY = textureStartY;
		}

		@Override
		void drawBigBackground(Screen screen, MatrixStack matrices, int startX, int startY) {
			super.drawBigBackground(screen, matrices, startX, startY);
			screen.drawTexture(matrices, startX + 11, startY + 11, textureStartX, textureStartY, 20, 20);
		}

		@Override
		void drawSmallBackground(Screen screen, MatrixStack matrices, int startX, int startY) {
			super.drawSmallBackground(screen, matrices, startX, startY);
			screen.drawTexture(matrices, startX, startY, textureStartX, textureStartY, 20, 20);
		}

	}

	public static class TextGridEntry extends GridEntry {

		protected final Text innerText;
		protected final int innerHalfTextWidth;

		private TextGridEntry(Text innerText, @Nullable String text, GridEntry.GridEntryCallback callback) {
			super(text, callback);
			this.innerText = innerText;
			this.innerHalfTextWidth = MinecraftClient.getInstance().textRenderer.getWidth(this.innerText) / 2;
		}

		@Override
		void drawBigForeground(Screen screen, MatrixStack matrices, int startX, int startY) {
			MinecraftClient.getInstance().textRenderer.draw(matrices, this.innerText, startX + 19 - innerHalfTextWidth, startY + 15, TEXT_COLOR);
		}

		@Override
		void drawSmallForeground(Screen screen, MatrixStack matrices, int startX, int startY) {
			MinecraftClient.getInstance().textRenderer.draw(matrices, this.innerText, startX + 14 - innerHalfTextWidth, startY + 10, TEXT_COLOR);
		}
	}

	private static class ItemGridEntry extends GridEntry {

		protected final ItemStack stack;
		private ItemGridEntry(Item item, String text, GridEntry.GridEntryCallback callback) {
			super(text, callback);
			this.stack = item.getDefaultStack();
		}

		@Override
		void drawBigForeground(Screen screen, MatrixStack matrices, int startX, int startY) {
			super.drawBigForeground(screen, matrices, startX, startY);
			MinecraftClient.getInstance().getItemRenderer().renderInGui(matrices, stack, startX + 10, startY + 10);
		}

		@Override
		void drawSmallForeground(Screen screen, MatrixStack matrices, int startX, int startY) {
			super.drawBigForeground(screen, matrices, startX, startY);
			MinecraftClient.getInstance().getItemRenderer().renderInGui(matrices, stack, startX + 5, startY + 5);
		}

	}
	
	public final Stack<Grid> gridStack = new Stack<>();
	
	public QuickNavigationGridScreen(QuickNavigationGridScreenHandler handler, PlayerInventory playerInventory, Text title) {
		super(handler, playerInventory, title);
		this.backgroundHeight = 256;
	}
	
	private void back() {
		client.world.playSound(null, client.player.getBlockPos(), SpectrumSoundEvents.PAINTBRUSH_SWITCH, SoundCategory.NEUTRAL, 0.5F, 1.0F);
		if(gridStack.size() == 1) {
			client.player.closeHandledScreen();
		} else {
			gridStack.pop();
		}
	}
	
	protected void selectGrid(Grid grid) {
		client.world.playSound(null, client.player.getBlockPos(), SpectrumSoundEvents.PAINTBRUSH_SWITCH, SoundCategory.NEUTRAL, 0.5F, 1.0F);
		gridStack.push(grid);
	}
	
	public Grid current() {
		return gridStack.peek();
	}
	
	@Override
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
		current().drawBackground(this, matrices, backgroundWidth / 2, backgroundHeight / 2);
		current().drawForeground(this, matrices, backgroundWidth / 2, backgroundHeight / 2);
		
		this.textRenderer.draw(matrices, CONTROLS_TEXT_1, (backgroundWidth - textRenderer.getWidth(CONTROLS_TEXT_1)) / 2, 228, TEXT_COLOR);
		this.textRenderer.draw(matrices, CONTROLS_TEXT_2, (backgroundWidth - textRenderer.getWidth(CONTROLS_TEXT_2)) / 2, 238, TEXT_COLOR);
	}
	
	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {

	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 1) {
			current().navigate(GUISelection.BACK, this);
		} else {
			int startX = backgroundWidth / 2;
			int startY = backgroundHeight / 2;
			
			mouseX = mouseX - x;
			mouseY = mouseY - y;
			
			int centerElementSize = gridStack.size() == 1 ? 5 : 14;
			if (mouseX >= startX - centerElementSize && mouseX <= startX + centerElementSize && mouseY >= startY - centerElementSize && mouseY <= startY + centerElementSize) {
				current().navigate(GUISelection.SELECT, this);
				return true;
			}
			
			int offsetID = 0;
			for (Pair<Integer, Integer> offset : SQUARE_OFFSETS) {
				if (mouseX >= startX + offset.getLeft() && mouseX <= startX + offset.getLeft() + 32 && mouseY >= startY + offset.getRight() && mouseY <= startY + offset.getRight() + 32) {
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
		GameOptions options = MinecraftClient.getInstance().options;
		if (options.leftKey.matchesKey(keyCode, scanCode)) {
			current().navigate(GUISelection.LEFT, this);
			return true;
		} else if (options.forwardKey.matchesKey(keyCode, scanCode)) {
			current().navigate(GUISelection.UP, this);
			return true;
		} else if (options.rightKey.matchesKey(keyCode, scanCode)) {
			current().navigate(GUISelection.RIGHT, this);
			return true;
		} else if (options.backKey.matchesKey(keyCode, scanCode)) {
			current().navigate(GUISelection.DOWN, this);
			return true;
		} else if (options.dropKey.matchesKey(keyCode, scanCode) || options.inventoryKey.matchesKey(keyCode, scanCode)) {
			current().navigate(GUISelection.SELECT, this);
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
}