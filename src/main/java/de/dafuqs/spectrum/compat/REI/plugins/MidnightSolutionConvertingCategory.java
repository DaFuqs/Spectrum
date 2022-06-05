package de.dafuqs.spectrum.compat.REI.plugins;

import com.google.common.collect.Lists;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.registries.SpectrumItems;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.List;

public class MidnightSolutionConvertingCategory implements DisplayCategory<MidnightSolutionConvertingDisplay> {
	
	@Override
	public CategoryIdentifier<? extends MidnightSolutionConvertingDisplay> getCategoryIdentifier() {
		return SpectrumPlugins.MIDNIGHT_SOLUTION_CONVERTING;
	}
	
	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumItems.MIDNIGHT_SOLUTION_BUCKET);
	}
	
	@Override
	public Text getTitle() {
		return new TranslatableText("container.spectrum.rei.midnight_solution_converting.title");
	}
	
	@Override
	public List<Widget> setupDisplay(MidnightSolutionConvertingDisplay display, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		
		if (!display.isUnlocked()) {
			widgets.add(Widgets.createLabel(new Point(startPoint.x - 23, startPoint.y + 4), new TranslatableText("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_1")).leftAligned().color(0x3f3f3f).noShadow());
			widgets.add(Widgets.createLabel(new Point(startPoint.x - 23, startPoint.y + 14), new TranslatableText("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_2")).leftAligned().color(0x3f3f3f).noShadow());
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
