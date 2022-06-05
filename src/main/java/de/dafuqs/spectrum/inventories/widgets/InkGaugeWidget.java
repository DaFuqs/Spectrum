//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package de.dafuqs.spectrum.inventories.widgets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class InkGaugeWidget extends DrawableHelper implements Drawable, Element, Selectable {
	
	protected int width;
	protected int height;
	public int x;
	public int y;
	
	protected boolean hovered;
	
	public InkGaugeWidget(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
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
	
}
