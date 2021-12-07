package de.dafuqs.spectrum.compat.REI;

import com.google.common.collect.Lists;
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

import java.util.List;

public class NaturesStaffConversionsCategory implements DisplayCategory<NaturesStaffConversionsDisplay> {
	
	@Override
	public CategoryIdentifier<? extends NaturesStaffConversionsDisplay> getCategoryIdentifier() {
		return SpectrumPlugins.NATURES_STAFF;
	}

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumItems.NATURES_STAFF);
	}

	@Override
	public Text getTitle() {
		return SpectrumItems.NATURES_STAFF.getName();
	}

	@Override
	public List<Widget> setupDisplay(NaturesStaffConversionsDisplay display, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4)));
		widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5)).entries(display.getIn()).markInput());
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5)).entries(display.getOut()).disableBackground().markInput());
		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 36;
	}

}
