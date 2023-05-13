package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.titration_barrel.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.fabricmc.api.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class TitrationBarrelCategory extends GatedDisplayCategory<TitrationBarrelDisplay> {
	
	@Override
	public CategoryIdentifier<TitrationBarrelDisplay> getCategoryIdentifier() {
		return SpectrumPlugins.TITRATION_BARREL;
	}
	
	@Override
	public Text getTitle() {
		return SpectrumBlocks.TITRATION_BARREL.getName();
	}
	
	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumBlocks.TITRATION_BARREL);
	}
	
	@Override
	public void setupWidgets(Point startPoint, Rectangle bounds, List<Widget> widgets, @NotNull TitrationBarrelDisplay display) {
		List<EntryIngredient> inputs = display.getInputEntries();
		
		// input slots
		int ingredientSize = inputs.size();
		int startX = startPoint.x + Math.max(-5, 15 - ingredientSize * 10);
		int startY = startPoint.y + (ingredientSize > 3 ? 1 : 11);
		for (int i = 0; i < ingredientSize; i++) {
			EntryIngredient currentIngredient = inputs.get(i);
			int yOffset;
			int xOffset;
			if (i < 3) {
				xOffset = i * 20;
				yOffset = 0;
			} else {
				xOffset = (i - 3) * 20;
				yOffset = 20;
			}
			widgets.add(Widgets.createSlot(new Point(startX + xOffset, startY + yOffset)).markInput().entries(currentIngredient));
		}
		
		// output arrow and slot
		if (display.tappingIngredient.isEmpty()) {
			widgets.add(Widgets.createArrow(new Point(startPoint.x + 60, startPoint.y + 11)).animationDurationTicks(display.minFermentationTimeHours * 20));
		} else {
			widgets.add(Widgets.createArrow(new Point(startPoint.x + 60, startPoint.y + 3)).animationDurationTicks(display.minFermentationTimeHours * 20));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 64, startPoint.y + 21)).markInput().entries(display.tappingIngredient));
		}
		widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 95, startPoint.y + 11)));
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 95, startPoint.y + 11)).markOutput().disableBackground().entries(display.getOutputEntries().get(0)));
		
		// duration text
		MutableText text = TitrationBarrelRecipe.getDurationText(display.minFermentationTimeHours, display.fermentationData);
		widgets.add(Widgets.createLabel(new Point(startPoint.x - 10, startPoint.y + 42), text).leftAligned().color(0x3f3f3f).noShadow());
	}
	
	@Override
	public int getDisplayHeight() {
		return 59;
	}
	
}
