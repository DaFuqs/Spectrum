package de.dafuqs.spectrum.inventories.widgets;

import de.dafuqs.spectrum.energy.InkStorage;
import de.dafuqs.spectrum.energy.InkStorageBlockEntity;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.helpers.RenderHelper;
import de.dafuqs.spectrum.helpers.Support;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class StackedInkMeterWidget extends DrawableHelper implements Drawable, Element, Selectable {
	
	public int x;
	public int y;
	public int width;
	public int height;
	protected boolean hovered;
	
	protected Screen screen;
	protected InkStorageBlockEntity blockEntity;
	
	public StackedInkMeterWidget(int x, int y, int width, int height, Screen screen, InkStorageBlockEntity blockEntity) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		this.screen = screen;
		this.blockEntity = blockEntity;
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
		InkStorage inkStorage = this.blockEntity.getEnergyStorage();
		long currentTotal = inkStorage.getCurrentTotal();
		String readableCurrentTotalString = Support.getShortenedNumberString(currentTotal);
		String percent = Support.getSensiblePercent(inkStorage.getCurrentTotal(), (inkStorage.getMaxTotal()));
		screen.renderTooltip(matrices,
				List.of(new TranslatableText("spectrum.tooltip.ink_powered.percent_filled", readableCurrentTotalString, percent)),
				Optional.empty(), x, y);
	}
	
	public void draw(MatrixStack matrices) {
		InkStorage inkStorage = this.blockEntity.getEnergyStorage();
		long currentTotal = inkStorage.getCurrentTotal();
		
		if (currentTotal > 0) {
			long maxTotal = inkStorage.getMaxTotal();
			
			int currentHeight = this.y + this.height;
			for (Map.Entry<InkColor, Long> entry : inkStorage.getEnergy().entrySet()) {
				long amount = entry.getValue();
				if(amount > 0) {
					int height = Math.max(1, Math.round (((float) amount / ((float) maxTotal / this.height))));
					RenderHelper.fillQuad(matrices, this.x, currentHeight - height, height, this.width, entry.getKey().getColor());
					currentHeight -= height;
				}
			}
		}
	}
	
}
