package de.dafuqs.spectrum.compat.REI.plugins;

import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import net.fabricmc.api.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public abstract class FluidConvertingCategory<T extends FluidConvertingDisplay> extends GatedDisplayCategory<FluidConvertingDisplay> {
	
	@Override
	public void setupWidgets(Point startPoint, Rectangle bounds, List<Widget> widgets, @NotNull FluidConvertingDisplay display) {
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4)));
		widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5)).entries(display.getIn()).markInput());
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5)).entries(display.getOut()).disableBackground().markInput());
	}
	
	@Override
	public int getDisplayHeight() {
		return 36;
	}
	
}
