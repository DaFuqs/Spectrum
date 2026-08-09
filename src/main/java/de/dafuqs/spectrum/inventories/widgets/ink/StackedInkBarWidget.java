package de.dafuqs.spectrum.inventories.widgets.ink;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.ink.capability.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.ink.storage.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import org.jetbrains.annotations.*;

import javax.annotation.Nullable;
import java.util.function.*;


public class StackedInkBarWidget extends AbstractWidget {
	
	protected static final ResourceLocation BACKGROUND_SPRITE = SpectrumCommon.locate("widget/stacked_ink_bar");
	protected static final ResourceLocation BACKGROUND_SPRITE_THICK = SpectrumCommon.locate("widget/stacked_ink_bar_thick");
	
	protected final Supplier<InkCapability> inkCapability;
	protected boolean thickOutline;
	
	public StackedInkBarWidget(int x, int y, Supplier<InkCapability> inkCapability) {
		super(x, y, 6, 42, Component.empty());
		this.inkCapability = inkCapability;
	}
	
	public StackedInkBarWidget setThickOutline() {
		this.thickOutline = true;
		return this;
	}
	
	@Override
	protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		if(thickOutline) {
			guiGraphics.blitSprite(BACKGROUND_SPRITE_THICK, this.getX() - 1, this.getY() - 1, 8, 44);
		} else {
			guiGraphics.blitSprite(BACKGROUND_SPRITE, this.getX(), this.getY(), 6, 42);
		}
		
		InkStorage inkStorage = inkCapability.get().getStorage();
		if (inkStorage.getCurrentTotal() <= 0) {
			return;
		}
		
		long maxTotal = inkStorage.getMaxTotal();
		
		// drawn from bottom to top
		int currentHeight = getY() + getHeight() - 1;
		for (InkColor color : InkColors.all()) {
			long amount = inkStorage.getEnergy(color);
			if (amount > 0) {
				int height = Math.round(((float) amount / (float) maxTotal * getHeight()));
				if (height > 0) {
					RenderHelper.fillQuad(guiGraphics.pose(), getX() + 1, currentHeight - height, height, getWidth() - 2, color.getColorVec());
				}
				currentHeight -= height;
			}
		}
	}
	
	@Override
	protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
	
	}
	
	@Nullable
	public Tooltip getTooltip() {
		InkStorage inkStorage = inkCapability.get().getStorage();
		String readableCurrentTotalString = Support.getShortenedNumberString(inkStorage.getCurrentTotal());
		String percent = Support.getSensiblePercentString(inkStorage.getCurrentTotal(), (inkStorage.getMaxTotal()));
		return Tooltip.create(Component.translatable("spectrum.tooltip.ink_powered.percent_filled", readableCurrentTotalString, percent));
	}
	
}
