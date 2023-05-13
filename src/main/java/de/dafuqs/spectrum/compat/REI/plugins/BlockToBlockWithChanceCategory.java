package de.dafuqs.spectrum.compat.REI.plugins;

import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import net.fabricmc.api.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public abstract class BlockToBlockWithChanceCategory extends GatedDisplayCategory<BlockToBlockWithChanceDisplay> {
	
	@Override
	public void setupWidgets(Point startPoint, Rectangle bounds, List<Widget> widgets, @NotNull BlockToBlockWithChanceDisplay display) {
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4)));
		widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5)).entries(display.getInputEntries().get(0)).markInput());
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5)).entries(display.getOutputEntries().get(0)).disableBackground().markInput());
		widgets.add(Widgets.createLabel(new Point(startPoint.x + 4, startPoint.y + 30), Text.translatable("container.spectrum.rei.chance", display.getChance() * 100)).leftAligned().color(0x3f3f3f).noShadow());
	}
	
	@Override
	public int getDisplayHeight() {
		return 47;
	}
	
}
