package de.dafuqs.spectrum.inventories.widgets;

import de.dafuqs.spectrum.blocks.energy.ColorPickerBlockEntity;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.energy.color.InkColors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static de.dafuqs.spectrum.helpers.RenderHelper.fillQuad;

@Environment(EnvType.CLIENT)
public class ColorSelectionWidget extends ClickableWidget implements Drawable, Element {
	
	protected ColorPickerBlockEntity colorPicker;
	
	@Nullable
	private Consumer<InkColor> changedListener;
	
	int selectedDotX;
	int selectedDotY;
	
	public ColorSelectionWidget(int x, int y, int selectedDotX, int selectedDotY, ColorPickerBlockEntity colorPicker) {
		super(x, y, 56, 14, new TranslatableText(""));
		this.colorPicker = colorPicker;
		this.selectedDotX = selectedDotX;
		this.selectedDotY = selectedDotY;
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
		
		if (bl && button == 0) {
			int xOffset = MathHelper.floor(mouseX) - this.x;
			int yOffset = MathHelper.floor(mouseY) - this.y;
			
			int horizontalColorOffset = xOffset / 7;
			int verticalColorOffset = yOffset / 7;
			int newColorIndex = horizontalColorOffset + verticalColorOffset * 8;
			InkColor newColor = InkColor.all().get(newColorIndex);
			if(this.colorPicker.getSelectedColor() != newColor) {
				onChanged(newColor);
			}
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {
		builder.put(NarrationPart.TITLE, new TranslatableText("spectrum.narration.color_selection", this.colorPicker.getSelectedColor()));
	}
	
	public void draw(MatrixStack matrices) {
		// draw selection icons
		int i = -1;
		int currentX = this.x + 1;
		int currentY = this.y + 1;
		for (InkColor color : InkColor.all()) {
			fillQuad(matrices, currentX, currentY, 5, 5, color.getColor());
			i = i + 1;
			currentX = currentX + 7;
			if (i == 7) {
				currentY = currentY + 7;
				currentX = this.x + 1;
			}
		}
		
		// draw selected icon
		InkColor selectedColor = this.colorPicker.getSelectedColor();
		if(selectedColor != null) {
			fillQuad(matrices, selectedDotX, selectedDotY, 5, 5, selectedColor.getColor());
		}
	}
	
	
}
