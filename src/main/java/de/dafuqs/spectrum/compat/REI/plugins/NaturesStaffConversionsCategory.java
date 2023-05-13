package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.util.*;
import net.fabricmc.api.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class NaturesStaffConversionsCategory extends GatedDisplayCategory<NaturesStaffConversionsDisplay> {
	
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
	public void setupWidgets(Point startPoint, Rectangle bounds, List<Widget> widgets, @NotNull NaturesStaffConversionsDisplay display) {
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4)));
		widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5)).entries(display.getInputEntries().get(0)).markInput());
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5)).entries(display.getOutputEntries().get(0)).disableBackground().markInput());
	}
	
	@Override
	public int getDisplayHeight() {
		return 36;
	}
	
}
