package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.energy.color.InkColors;
import de.dafuqs.spectrum.helpers.RenderHelper;
import de.dafuqs.spectrum.networking.SpectrumC2SPacketSender;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PaintbrushScreen extends HandledScreen<PaintbrushScreenHandler> {
	
	public static final int TEXT_COLOR = 0xEEEEEE;
	public static final Identifier BACKGROUND = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/paintbrush.png");
	
	protected final Text note1 = new LiteralText("Movement Keys to move");
	protected final Text note2 = new LiteralText("Drop/Inventory to select");
	
	public static final List<InkColor> MAIN_GRID = new ArrayList<>() {{
		add(InkColors.MAGENTA);
		add(InkColors.CYAN);
		add(InkColors.YELLOW);
		add(InkColors.BLACK);
	}};
	public static final List<InkColor> MAGENTA_GRID = new ArrayList<>() {{
		add(InkColors.RED);
		add(InkColors.PINK);
		add(null);
		add(InkColors.BROWN);
	}};
	public static final List<InkColor> CYAN_GRID = new ArrayList<>() {{
		add(InkColors.LIGHT_BLUE);
		add(InkColors.BLUE);
		add(InkColors.PURPLE);
		add(null);
	}};
	public static final List<InkColor> YELLOW_GRID = new ArrayList<>() {{
		add(null);
		add(InkColors.ORANGE);
		add(InkColors.LIME);
		add(InkColors.GREEN);
	}};
	public static final List<InkColor> BLACK_GRID = new ArrayList<>() {{
		add(InkColors.WHITE);
		add(null);
		add(InkColors.LIGHT_GRAY);
		add(InkColors.GRAY);
	}};
	public static final List<List<InkColor>> GRIDS = new ArrayList<>() {{
		add(MAIN_GRID);
		add(MAGENTA_GRID);
		add(CYAN_GRID);
		add(YELLOW_GRID);
		add(BLACK_GRID);
	}};
	
	private static final List<Pair<Integer, Integer>> SQUARE_OFFSETS = List.of(
			new Pair<>(-64, -16),
			new Pair<>(-16, -64),
			new Pair<>(32, -16),
			new Pair<>(-16, 32)
	);
	
	private int currentGrid = 0;

	public PaintbrushScreen(PaintbrushScreenHandler handler, PlayerInventory playerInventory, Text title) {
		super(handler, playerInventory, title);
		this.backgroundHeight = 256;
	}
	
	@Override
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
		drawGrid(matrices, backgroundWidth / 2, backgroundHeight / 2, GRIDS.get(currentGrid));
		
		this.textRenderer.draw(matrices, note1, (backgroundWidth - textRenderer.getWidth(note1)) / 2, 202, TEXT_COLOR);
		this.textRenderer.draw(matrices, note2, (backgroundWidth - textRenderer.getWidth(note2)) / 2, 212, TEXT_COLOR);
	}
	
	protected void drawGrid(MatrixStack matrices, int startX, int startY, List<InkColor> grid) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, BACKGROUND);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		if(currentGrid == 0) {
			drawTexture(matrices, startX - 5, startY - 5, 0, 0, 10, 10);
		} else {
			drawTexture(matrices, startX - 14, startY - 14, 48, 0, 28, 28);
			RenderHelper.fillQuad(matrices, startX - 12, startY - 12, 24, 24, MAIN_GRID.get(currentGrid-1).getColor());
		}
		
		Iterator<Pair<Integer, Integer>> iOffset = SQUARE_OFFSETS.iterator();
		for(InkColor color : grid) {
			Pair<Integer, Integer> offset = iOffset.next();
			if(color != null) {
				drawTexture(matrices, startX + offset.getLeft() - 3, startY + offset.getRight() - 3, 10, 0, 38, 38);
				RenderHelper.fillQuad(matrices, startX + offset.getLeft(), startY + offset.getRight(), 32, 32, color.getColor());
			}
		}
	}
	
	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {

	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if(button == 1) {
			// select
			selectBack();
		} else {
			// back
			int startX = backgroundWidth / 2;
			int startY = backgroundHeight / 2;
			
			mouseX = mouseX - x;
			mouseY = mouseY - y;
			
			int centerElementSize = currentGrid == 0 ? 5 : 14;
			if(mouseX >= startX -centerElementSize && mouseX <= startX + centerElementSize && mouseY >= startY -centerElementSize && mouseY <= startY + centerElementSize) {
				selectCenter();
				return true;
			}
			
			List<InkColor> grid = GRIDS.get(currentGrid);
			Iterator<Pair<Integer, Integer>> iOffset = SQUARE_OFFSETS.iterator();
			int offsetID = 0;
			for (InkColor color : grid) {
				Pair<Integer, Integer> offset = iOffset.next();
				if (color != null) {
					if(mouseX >= startX + offset.getLeft() && mouseX <= startX + offset.getLeft() + 32 && mouseY >= startY + offset.getRight() && mouseY <= startY + offset.getRight() + 32) {
						switch (offsetID) {
							case 0 -> {
								selectLeft();
								return true;
							}
							case 1 -> {
								selectUp();
								return true;
							}
							case 2 -> {
								selectRight();
								return true;
							}
							case 3 -> {
								selectDown();
								return true;
							}
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
			selectLeft();
			return true;
		} else if(options.forwardKey.matchesKey(keyCode, scanCode)) {
			selectUp();
			return true;
		} else if(options.rightKey.matchesKey(keyCode, scanCode)) {
			selectRight();
			return true;
		} else if(options.backKey.matchesKey(keyCode, scanCode)) {
			selectDown();
			return true;
		} else if(options.dropKey.matchesKey(keyCode, scanCode) || options.inventoryKey.matchesKey(keyCode, scanCode)) {
			selectCenter();
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	private void selectCenter() {
		if(currentGrid == 0) {
			chooseColor(null);
		} else {
			chooseColor(MAIN_GRID.get(currentGrid-1));
		}
	}
	
	private void selectRight() {
		if(currentGrid == 0) {
			currentGrid = 3;
			client.world.playSound(client.player.getBlockPos(), SpectrumSoundEvents.PAINTBRUSH_SWITCH, SoundCategory.NEUTRAL, 0.5F, 1.0F, false);
		} else {
			InkColor selectedColor = GRIDS.get(currentGrid).get(2);
			if(selectedColor == null) {
				selectBack();
			} else {
				chooseColor(selectedColor);
			}
		}
	}
	
	private void selectUp() {
		if(currentGrid == 0) {
			currentGrid = 2;
			client.world.playSound(client.player.getBlockPos(), SpectrumSoundEvents.PAINTBRUSH_SWITCH, SoundCategory.NEUTRAL, 0.5F, 1.0F, false);
		} else {
			InkColor selectedColor = GRIDS.get(currentGrid).get(1);
			if(selectedColor == null) {
				selectBack();
			} else {
				chooseColor(selectedColor);
			}
		}
	}
	
	private void selectLeft() {
		if(currentGrid == 0) {
			currentGrid = 1;
			client.world.playSound(client.player.getBlockPos(), SpectrumSoundEvents.PAINTBRUSH_SWITCH, SoundCategory.NEUTRAL, 0.5F, 1.0F, false);
		} else {
			InkColor selectedColor = GRIDS.get(currentGrid).get(0);
			if(selectedColor == null) {
				selectBack();
			} else {
				chooseColor(selectedColor);
			}
		}
	}
	
	private void selectDown() {
		if(currentGrid == 0) {
			if(handler.hasAccessToWhites()) {
				currentGrid = 4;
				client.world.playSound(client.player.getBlockPos(), SpectrumSoundEvents.PAINTBRUSH_SWITCH, SoundCategory.NEUTRAL, 0.5F, 1.0F, false);
			} else {
				chooseColor(MAIN_GRID.get(3));
			}
		} else {
			InkColor selectedColor = GRIDS.get(currentGrid).get(3);
			if(selectedColor == null) {
				selectBack();
			} else {
				chooseColor(selectedColor);
			}
		}
	}
	
	private void selectBack() {
		if(currentGrid != 0) {
			currentGrid = 0;
			client.world.playSound(client.player.getBlockPos(), SpectrumSoundEvents.PAINTBRUSH_SWITCH, SoundCategory.NEUTRAL, 0.5F, 1.0F, false);
		}
	}
	
	private void chooseColor(@Nullable InkColor inkColor) {
		SpectrumC2SPacketSender.sendInkColorSelectedInGUI(inkColor);
		client.world.playSound(client.player.getBlockPos(), SpectrumSoundEvents.PAINTBRUSH_PAINT, SoundCategory.NEUTRAL, 0.6F, 1.0F, false);
		client.player.closeHandledScreen();
		
	}
	
}