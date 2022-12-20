package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.RenderHelper;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.Stack;

public class QuickNavigationGridScreen<QuickNavigationGridScreenHandler extends ScreenHandler> extends HandledScreen<QuickNavigationGridScreenHandler> {
	
	public static final int TEXT_COLOR = 0xEEEEEE;
	public static final Identifier BACKGROUND = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/paintbrush.png");
	
	protected static final Text CONTROLS_TEXT_1 = Text.translatable("item.spectrum.paintbrush.gui.controls1");
	protected static final Text CONTROLS_TEXT_2 = Text.translatable("item.spectrum.paintbrush.gui.controls2");
	
	private static final List<Pair<Integer, Integer>> SQUARE_OFFSETS = List.of(
			new Pair<>(-16, -64),
			new Pair<>(32, -16),
			new Pair<>(-16, 32),
			new Pair<>(-64, -16)
	);
	
	public enum GUIDirection {
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
		
		void navigate(GUIDirection direction, QuickNavigationGridScreen screen) {
			switch (direction) {
				case BACK -> {
					screen.back();
				}
				case SELECT -> {
					centerEntry.callback.whenSelected(screen);
				}
				case UP -> {
					topEntry.callback.whenSelected(screen);
				}
				case RIGHT -> {
					rightEntry.callback.whenSelected(screen);
				}
				case DOWN -> {
					bottomEntry.callback.whenSelected(screen);
				}
				default -> {
					leftEntry.callback.whenSelected(screen);
				}
			}
		}
		
		void render(Screen screen, MatrixStack matrices, int startX, int startY) {
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderTexture(0, BACKGROUND);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			if (centerEntry == GridEntry.CLOSE) {
				screen.drawTexture(matrices, startX - 5, startY - 5, 0, 0, 10, 10);
			} else {
				centerEntry.renderSmall(screen, matrices, startX - 14, startY - 14);
			}
			
			topEntry.renderBig(screen, matrices, startX + SQUARE_OFFSETS.get(0).getLeft() - 3, startY + SQUARE_OFFSETS.get(0).getRight() - 3);
			rightEntry.renderBig(screen, matrices, startX + SQUARE_OFFSETS.get(1).getLeft() - 3, startY + SQUARE_OFFSETS.get(1).getRight() - 3);
			bottomEntry.renderBig(screen, matrices, startX + SQUARE_OFFSETS.get(2).getLeft() - 3, startY + SQUARE_OFFSETS.get(2).getRight() - 3);
			leftEntry.renderBig(screen, matrices, startX + SQUARE_OFFSETS.get(3).getLeft() - 3, startY + SQUARE_OFFSETS.get(3).getRight() - 3);
		}
		
	}
	
	public static class GridEntry {
		
		public static GridEntry CLOSE = GridEntry.of(null, null, QuickNavigationGridScreen::close);
		public static GridEntry BACK = GridEntry.of(null, null, QuickNavigationGridScreen::back);
		public static GridEntry EMPTY = GridEntry.of();
		
		public interface GridEntryCallback {
			void whenSelected(QuickNavigationGridScreen screen);
		}
		
		private final Vec3f color;
		private final @Nullable Point point;
		private final GridEntryCallback callback;
		
		private GridEntry(Vec3f color, @Nullable Point point, GridEntryCallback callback) {
			this.color = color;
			this.point = point;
			this.callback = callback;
		}
		
		public static GridEntry of() {
			return new GridEntry(null, null, screen -> {});
		}
		
		public static GridEntry of(Vec3f color) {
			return new GridEntry(color, null, screen -> {});
		}
		
		public static GridEntry of(Vec3f color, Point point) {
			return new GridEntry(color, point, screen -> {});
		}
		
		public static GridEntry of(Vec3f color, Point point, GridEntryCallback callback) {
			return new GridEntry(color, point, callback);
		}
		
		void renderBig(Screen screen, MatrixStack matrices, int startX, int startY) {
			boolean drawFrame = false;
			if(color != null) {
				RenderHelper.fillQuad(matrices, startX + 3, startY + 3, 32, 32, color);
				drawFrame = true;
			}
			if(point != null) {
				screen.drawTexture(matrices, startX + 11, startY + 11, point.x, point.y, 16, 16);
				drawFrame = true;
			}
			if(drawFrame) {
				screen.drawTexture(matrices, startX, startY, 10, 0, 38, 38);
			}
		}
		
		void renderSmall(Screen screen, MatrixStack matrices, int startX, int startY) {
			boolean drawFrame = false;
			if(color != null) {
				RenderHelper.fillQuad(matrices, startX + 2, startY + 2, 24, 24, color);
				drawFrame = true;
			}
			if(point != null) {
				drawFrame = true;
			}
			if(drawFrame) {
				screen.drawTexture(matrices, startX, startY, 48, 0, 28, 28);
			}
		}
		
	}
	
	public final Stack<Grid> gridStack = new Stack<>();
	
	public QuickNavigationGridScreen(QuickNavigationGridScreenHandler handler, PlayerInventory playerInventory, Text title) {
		super(handler, playerInventory, title);
		this.backgroundHeight = 256;
	}
	
	private void back() {
		client.world.playSound(client.player.getBlockPos(), SpectrumSoundEvents.PAINTBRUSH_SWITCH, SoundCategory.NEUTRAL, 0.5F, 1.0F, false);
		if(gridStack.size() == 1) {
			client.player.closeHandledScreen();
		} else {
			gridStack.pop();
		}
	}
	
	void selectGrid(Grid grid) {
		client.world.playSound(client.player.getBlockPos(), SpectrumSoundEvents.PAINTBRUSH_SWITCH, SoundCategory.NEUTRAL, 0.5F, 1.0F, false);
		gridStack.push(grid);
	}
	
	public Grid current() {
		return gridStack.peek();
	}
	
	@Override
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
		current().render(this, matrices, backgroundWidth / 2, backgroundHeight / 2);
		
		this.textRenderer.draw(matrices, CONTROLS_TEXT_1, (backgroundWidth - textRenderer.getWidth(CONTROLS_TEXT_1)) / 2, 202, TEXT_COLOR);
		this.textRenderer.draw(matrices, CONTROLS_TEXT_2, (backgroundWidth - textRenderer.getWidth(CONTROLS_TEXT_2)) / 2, 212, TEXT_COLOR);
	}
	
	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
	
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 1) {
			current().navigate(GUIDirection.BACK, this);
		} else {
			int startX = backgroundWidth / 2;
			int startY = backgroundHeight / 2;
			
			mouseX = mouseX - x;
			mouseY = mouseY - y;
			
			int centerElementSize = gridStack.size() == 1 ? 5 : 14;
			if (mouseX >= startX - centerElementSize && mouseX <= startX + centerElementSize && mouseY >= startY - centerElementSize && mouseY <= startY + centerElementSize) {
				current().navigate(GUIDirection.SELECT, this);
				return true;
			}
			
			int offsetID = 0;
			for (Pair<Integer, Integer> offset : SQUARE_OFFSETS) {
				if (mouseX >= startX + offset.getLeft() && mouseX <= startX + offset.getLeft() + 32 && mouseY >= startY + offset.getRight() && mouseY <= startY + offset.getRight() + 32) {
					switch (offsetID) {
						case 0 -> {
							current().navigate(GUIDirection.UP, this);
							return true;
						}
						case 1 -> {
							current().navigate(GUIDirection.RIGHT, this);
							return true;
						}
						case 2 -> {
							current().navigate(GUIDirection.DOWN, this);
							return true;
						}
						case 3 -> {
							current().navigate(GUIDirection.LEFT, this);
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
			current().navigate(GUIDirection.LEFT, this);
			return true;
		} else if (options.forwardKey.matchesKey(keyCode, scanCode)) {
			current().navigate(GUIDirection.UP, this);
			return true;
		} else if (options.rightKey.matchesKey(keyCode, scanCode)) {
			current().navigate(GUIDirection.RIGHT, this);
			return true;
		} else if (options.backKey.matchesKey(keyCode, scanCode)) {
			current().navigate(GUIDirection.DOWN, this);
			return true;
		} else if (options.dropKey.matchesKey(keyCode, scanCode) || options.inventoryKey.matchesKey(keyCode, scanCode)) {
			current().navigate(GUIDirection.SELECT, this);
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
}