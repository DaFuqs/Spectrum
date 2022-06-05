package de.dafuqs.spectrum.inventories.widgets;

import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.energy.color.InkColors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ColorSelectionWidget extends ClickableWidget implements Drawable, Element {
	
	protected InkColor selectedColor;
	
	@Nullable
	private Consumer<InkColor> changedListener;
	
	public ColorSelectionWidget(int x, int y, int width, int height, InkColor selectedColor) {
		super(x, y, width, height, new TranslatableText(""));
		this.selectedColor = selectedColor;
	}
	
	public InkColor selectedColor() {
		return this.selectedColor;
	}
	
	public void setChangedListener(@Nullable Consumer<InkColor> changedListener) {
		this.changedListener = changedListener;
	}
	
	private void onChanged(InkColor newColor) {
		if (this.changedListener != null) {
			this.changedListener.accept(newColor);
		}
	}
	
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		boolean bl = mouseX >= (double) this.x && mouseX < (double) (this.x + this.width) && mouseY >= (double) this.y && mouseY < (double) (this.y + this.height);
		
		if (this.isFocused() && bl && button == 0) {
			int i = MathHelper.floor(mouseX) - this.x;
			int j = MathHelper.floor(mouseX) - this.y;
			
			// TODO: choose correct color
			this.selectedColor = InkColors.BROWN;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {
		builder.put(NarrationPart.TITLE, new TranslatableText("spectrum.narration.color_selection", this.selectedColor));
	}
	
}
