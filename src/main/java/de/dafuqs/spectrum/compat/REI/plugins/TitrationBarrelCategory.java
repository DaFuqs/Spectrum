package de.dafuqs.spectrum.compat.REI.plugins;

import com.google.common.collect.Lists;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.recipe.titration_barrel.TitrationBarrelRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TitrationBarrelCategory implements DisplayCategory<TitrationBarrelDisplay> {
	
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
	public List<Widget> setupDisplay(@NotNull TitrationBarrelDisplay display, @NotNull Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 23);
		List<Widget> widgets = Lists.newArrayList();
		
		widgets.add(Widgets.createRecipeBase(bounds));
		
		if (!display.isUnlocked()) {
			widgets.add(Widgets.createLabel(new Point(startPoint.x - 6, startPoint.y + 13), new TranslatableText("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_1")).leftAligned().color(0x3f3f3f).noShadow());
			widgets.add(Widgets.createLabel(new Point(startPoint.x - 6, startPoint.y + 23), new TranslatableText("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_2")).leftAligned().color(0x3f3f3f).noShadow());
		} else {
			List<EntryIngredient> inputs = display.getInputEntries();
			
			// input slots
			int ingredientSize = inputs.size();
			int startX = startPoint.x + Math.max(-5, 15 - ingredientSize * 10);
			int startY = startPoint.y + (ingredientSize > 2 ? 0 : 10);
			for (int i = 0; i < ingredientSize; i++) {
				EntryIngredient currentIngredient = inputs.get(i);
				int yOffset;
				int xOffset;
				if(i < 3) {
					xOffset = i * 20;
					yOffset = 0;
				} else {
					xOffset = (i - 3) * 20;
					yOffset = 20;
				}
				widgets.add(Widgets.createSlot(new Point(startX + xOffset, startY + yOffset)).markInput().entries(currentIngredient));
			}
			
			// output arrow and slot
			if(display.tappingIngredient.isEmpty()) {
				widgets.add(Widgets.createArrow(new Point(startPoint.x + 60, startPoint.y + 10)).animationDurationTicks(display.minFermentationTimeHours * 20));
			} else {
				widgets.add(Widgets.createArrow(new Point(startPoint.x + 60, startPoint.y + 2)).animationDurationTicks(display.minFermentationTimeHours * 20));
				widgets.add(Widgets.createSlot(new Point(startPoint.x + 64, startPoint.y + 20)).markInput().entries(display.tappingIngredient));
			}
			widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 95, startPoint.y + 10)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 95, startPoint.y + 10)).markOutput().disableBackground().entries(display.getOutputEntries().get(0)));
			
			// duration text
			MutableText text = TitrationBarrelRecipe.getDurationText(display.minFermentationTimeHours, display.fermentationData);
			widgets.add(Widgets.createLabel(new Point(startPoint.x - 10, startPoint.y + 40), text).leftAligned().color(0x3f3f3f).noShadow());
		}
		return widgets;
	}
	
	@Override
	public int getDisplayHeight() {
		return 60;
	}
	
}
