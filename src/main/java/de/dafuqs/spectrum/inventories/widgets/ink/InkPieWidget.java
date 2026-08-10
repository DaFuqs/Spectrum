package de.dafuqs.spectrum.inventories.widgets.ink;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.ink.capability.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.ink.storage.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import org.jspecify.annotations.*;

import java.util.function.*;


public class InkPieWidget extends AbstractWidget {
	
	public static final double TICKS_PER_ROTATION = 400D;
	
	protected static final ResourceLocation BACKGROUND_SPRITE = SpectrumCommon.locate("widget/ink_pie_background");
	protected static final ResourceLocation FOREGROUND_SPRITE = SpectrumCommon.locate("widget/ink_pie_foreground");
	protected static final ResourceLocation FOREGROUND_SPRITE_THICK = SpectrumCommon.locate("widget/ink_pie_foreground_thick");
	
	protected final Supplier<InkCapability> inkCapability;
	protected boolean thickOutline;
	
	public InkPieWidget(int x, int y, Supplier<InkCapability> inkCapability) {
		super(x, y, 42, 42, Component.empty());
		this.inkCapability = inkCapability;
	}
	
	public InkPieWidget setThickOutline() {
		this.thickOutline = true;
		return this;
	}
	
	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		guiGraphics.blitSprite(BACKGROUND_SPRITE, this.getX(), this.getY(), 42, 42);
		
		InkStorage inkStorage = inkCapability.get().getStorage();
		long currentTotal = inkStorage.getCurrentTotal();
		if (currentTotal <= 0) return;
		
		int centerX = getX() + width / 2;
		int centerY = getY() + width / 2;
		int radius = 22;
		
		// --- Smooth, double-precision rotation source ---
		double time = Minecraft.getInstance().level.getGameTime();
		double base = time % TICKS_PER_ROTATION;
		double startRad = -(base / TICKS_PER_ROTATION) * Math.PI;
		
		for (InkColor color : InkColors.all()) {
			long currentInk = inkStorage.getEnergy(color);
			if (currentInk <= 0) continue;
			
			double remaining = (double) currentInk / (double) currentTotal;
			
			while (remaining > 0.0) {
				double curr = Math.min(0.20, remaining);
				remaining -= curr;
				
				double endRad = startRad + curr * 2.0 * Math.PI;
				float p2x = (float) (radius * Math.cos(startRad));
				float p2y = (float) (radius * Math.sin(startRad));
				float p3x = (float) (radius * Math.cos(endRad));
				float p3y = (float) (radius * Math.sin(endRad));
				
				RenderHelper.fillTriangle(guiGraphics.pose(),
						centerX, centerY,
						centerX + p3x, centerY + p3y,
						centerX + p2x, centerY + p2y,
						color.getColorVec());
				
				double middleRad = startRad + curr * Math.PI;
				float pmx = (float) (radius * Math.cos(middleRad));
				float pmy = (float) (radius * Math.sin(middleRad));
				
				RenderHelper.fillTriangle(guiGraphics.pose(),
						centerX + p3x, centerY + p3y,
						centerX + pmx, centerY + pmy,
						centerX + p2x, centerY + p2y,
						color.getColorVec());
				
				startRad = endRad;
			}
		}
		
		if(thickOutline) {
			guiGraphics.blitSprite(FOREGROUND_SPRITE_THICK, this.getX() - 1, this.getY() - 1, 44, 44);
		} else {
			guiGraphics.blitSprite(FOREGROUND_SPRITE, this.getX(), this.getY(), 42, 42);
		}
	}
	
	@Nullable
	public Tooltip getTooltip() {
		return inkCapability.get().getStorage().getWidgetTooltip();
	}
	
	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
	
	}
	
}
