package de.dafuqs.spectrum.compat.REI.plugins;

import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import net.minecraft.text.Text;

import java.util.List;

public abstract class BlockToBlockWithChanceCategory implements DisplayCategory<BlockToBlockWithChanceDisplay> {
	
	@Override
	public List<Widget> setupDisplay(BlockToBlockWithChanceDisplay display, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 44, bounds.getCenterY() - 19);
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		
		if (!display.isUnlocked()) {
			widgets.add(Widgets.createLabel(new Point(startPoint.x - 23, startPoint.y + 10), Text.translatable("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_1")).leftAligned().color(0x3f3f3f).noShadow());
			widgets.add(Widgets.createLabel(new Point(startPoint.x - 23, startPoint.y + 20), Text.translatable("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_2")).leftAligned().color(0x3f3f3f).noShadow());
		} else {
			widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4)));
			widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5)).entries(display.getInputEntries().get(0)).markInput());
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5)).entries(display.getOutputEntries().get(0)).disableBackground().markInput());
			widgets.add(Widgets.createLabel(new Point(startPoint.x + 4, startPoint.y + 30), Text.translatable("container.spectrum.rei.chance", display.getChance() * 100)).leftAligned().color(0x3f3f3f).noShadow());
		}
		return widgets;
	}
	
	@Override
	public int getDisplayHeight() {
		return 50;
	}
	
}
