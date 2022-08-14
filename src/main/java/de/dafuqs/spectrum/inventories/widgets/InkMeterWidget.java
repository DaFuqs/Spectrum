package de.dafuqs.spectrum.inventories.widgets;

import de.dafuqs.spectrum.energy.InkStorageBlockEntity;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.energy.storage.IndividualCappedInkStorage;
import de.dafuqs.spectrum.helpers.RenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class InkMeterWidget extends DrawableHelper implements Drawable, Element, Selectable {
	
	public static int WIDTH_PER_COLOR = 4;
	public static int SPACE_BETWEEN_COLORS = 2;
	
	public int x;
	public int y;
	public int width;
	public int height;
	protected boolean hovered;
	
	protected Screen screen;
	protected InkStorageBlockEntity<IndividualCappedInkStorage> inkStorageBlockEntity;
	
	public InkMeterWidget(int x, int y, int height, Screen screen, InkStorageBlockEntity<IndividualCappedInkStorage> inkStorageBlockEntity) {
		this.x = x;
		this.y = y;
		this.width = inkStorageBlockEntity.getEnergyStorage().getSupportedColors().size() * (WIDTH_PER_COLOR + SPACE_BETWEEN_COLORS) - SPACE_BETWEEN_COLORS;
		this.height = height;
		
		this.screen = screen;
		this.inkStorageBlockEntity = inkStorageBlockEntity;
	}
	
	public boolean isMouseOver(double mouseX, double mouseY) {
		return mouseX >= (double) this.x && mouseX < (double) (this.x + this.width) && mouseY >= (double) this.y && mouseY < (double) (this.y + this.height);
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
	}
	
	@Override
	public SelectionType getType() {
		return this.hovered ? SelectionType.HOVERED : SelectionType.NONE;
	}
	
	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {
	
	}
	
	public void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
		List<Text> tooltip = new ArrayList<>();
		inkStorageBlockEntity.getEnergyStorage().addTooltip(tooltip, false);
		screen.renderTooltip(matrices, tooltip, Optional.empty(), x, y);
	}
	
	public void draw(MatrixStack matrices) {
		int startHeight = this.y + this.height;
		int currentXOffset = 0;
		
		IndividualCappedInkStorage inkStorage = inkStorageBlockEntity.getEnergyStorage();
		long total = inkStorage.getMaxPerColor();
		for(InkColor inkColor : inkStorage.getSupportedColors()) {
			long amount = inkStorage.getEnergy(inkColor);
			if (amount > 0) {
				int height = Math.max(1, Math.round(((float) amount / ((float) total / this.height))));
				RenderHelper.fillQuad(matrices, this.x + currentXOffset, startHeight - height, height, WIDTH_PER_COLOR, inkColor.getColor());
			}
			currentXOffset = currentXOffset + WIDTH_PER_COLOR + SPACE_BETWEEN_COLORS;
		}
		

	}
	
}
