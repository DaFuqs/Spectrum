package de.dafuqs.spectrum.compat.REI.plugins;

import com.google.common.collect.*;
import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.client.registry.display.*;
import net.fabricmc.api.*;
import net.minecraft.text.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public abstract class FluidConvertingCategory<T extends FluidConvertingDisplay> implements DisplayCategory<FluidConvertingDisplay> {
	
	@Override
	public List<Widget> setupDisplay(FluidConvertingDisplay display, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		
		if (!display.isUnlocked()) {
			widgets.add(Widgets.createLabel(new Point(startPoint.x - 23, startPoint.y + 4), Text.translatable("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_1")).leftAligned().color(0x3f3f3f).noShadow());
			widgets.add(Widgets.createLabel(new Point(startPoint.x - 23, startPoint.y + 14), Text.translatable("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_2")).leftAligned().color(0x3f3f3f).noShadow());
		} else {
			widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4)));
			widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5)).entries(display.getIn()).markInput());
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5)).entries(display.getOut()).disableBackground().markInput());
		}
		return widgets;
	}
	
	@Override
	public int getDisplayHeight() {
		return 36;
	}
	
}
