//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package de.dafuqs.spectrum.inventories.widgets;

import de.dafuqs.spectrum.energy.InkStorage;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.energy.color.InkColors;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.inventories.ColorPickerScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class InkGaugeWidget extends DrawableHelper implements Drawable, Element, Selectable {
	
	public int x;
	public int y;
	public int width;
	public int height;
	protected boolean hovered;
	
	protected Screen screen;
	protected InkStorage inkStorage;
	
	public InkGaugeWidget(int x, int y, int width, int height, Screen screen, InkStorage inkStorage) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		this.screen = screen;
		this.inkStorage = inkStorage;
	}
	
	public boolean isMouseOver(double mouseX, double mouseY) {
		return mouseX >= (double) this.x && mouseX < (double) (this.x + this.width) && mouseY >= (double) this.y && mouseY < (double) (this.y + this.height);
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
	}
	
	public boolean isHovered() {
		return this.hovered;
	}
	
	@Override
	public SelectionType getType() {
		return this.hovered ? SelectionType.HOVERED : SelectionType.NONE;
	}
	
	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {
	
	}
	
	public void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
		List<Text> list = new ArrayList<>();
		for (InkColor color : InkColor.all()) {
			long amount = inkStorage.getEnergy(color);
			if (amount > 0) {
				list.add(new LiteralText(amount + " ").append(new TranslatableText("spectrum.tooltip.ink_powered.bullet." + color.toString())));
			}
		}
		if (list.size() == 0) {
			list.add(new TranslatableText("spectrum.tooltip.ink_powered.empty"));
		}
		
		screen.renderTooltip(matrices, list, Optional.empty(), x, y);
	}
	
	
	public void draw(ColorPickerScreen screen, MatrixStack matrices) {
		long totalInk = inkStorage.getCurrentTotal();
		
		if (totalInk > 0) {
			int centerX = x + width / 2;
			int centerY = y + width / 2;
			int radius = 21;
			
			double currentRad = 0;
			for (InkColor color : InkColor.all()) {
				long currentInk = inkStorage.getEnergy(color);
				if (currentInk > 0) {
					double thisRad = ((double) currentInk / (double) totalInk) * 2 * Math.PI;
					
					int p2x = centerX + (int) (radius * Math.cos(thisRad));
					int p2y = centerY + (int) (radius * Math.sin(thisRad));
					int p3x = centerX + (int) (radius * Math.cos(currentRad));
					int p3y = centerY + (int) (radius * Math.sin(currentRad));
					
					screen.fillTri(matrices.peek().getPositionMatrix(),
							centerX, centerY, // center point
							p2x, p2y, // end point
							p3x, p3y, // start point
							ColorHelper.getVec(color.getDyeColor()));
					
					screen.fillTri(matrices.peek().getPositionMatrix(),
							p2x, p2y, // end point
							p3x, p2y, // outside point
							p3x, p3y, // start point
							ColorHelper.getVec(color.getDyeColor()));
					
					currentRad = thisRad;
				}
			}
		}
	}
	
}
