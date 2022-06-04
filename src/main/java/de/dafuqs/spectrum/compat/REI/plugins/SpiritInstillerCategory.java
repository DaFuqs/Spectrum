package de.dafuqs.spectrum.compat.REI.plugins;

import com.google.common.collect.Lists;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpiritInstillerCategory implements DisplayCategory<SpiritInstillerRecipeDisplay> {
	
	private static final EntryIngredient SPIRIT_INSTILLER = EntryIngredients.of(new ItemStack(SpectrumBlocks.SPIRIT_INSTILLER));
	private static final EntryIngredient ITEM_BOWL_CALCITE = EntryIngredients.of(new ItemStack(SpectrumBlocks.ITEM_BOWL_CALCITE));

	@Override
	public CategoryIdentifier<SpiritInstillerRecipeDisplay> getCategoryIdentifier() {
		return SpectrumPlugins.SPIRIT_INSTILLER;
	}

	@Override
	public Text getTitle() {
		return SpectrumBlocks.SPIRIT_INSTILLER.getName();
	}

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumBlocks.SPIRIT_INSTILLER);
	}

	@Override
	public List<Widget> setupDisplay(@NotNull SpiritInstillerRecipeDisplay display, @NotNull Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 23);
		List<Widget> widgets = Lists.newArrayList();

		widgets.add(Widgets.createRecipeBase(bounds));
		
		if(!display.isUnlocked()) {
			widgets.add(Widgets.createLabel(new Point(startPoint.x - 6, startPoint.y + 13), new TranslatableText("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_1")).leftAligned().color(0x3f3f3f).noShadow());
			widgets.add(Widgets.createLabel(new Point(startPoint.x - 6, startPoint.y + 23), new TranslatableText("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_2")).leftAligned().color(0x3f3f3f).noShadow());
		} else {
			List<EntryIngredient> output = display.getOutputEntries();

			// input slots
			int ingredientSize  = display.craftingInputs.size();
			int startX = Math.max(0, 10 - ingredientSize * 10);
			widgets.add(Widgets.createSlot(new Point(startPoint.x + startX, startPoint.y)).markInput().entries(display.craftingInputs.get(0)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + startX + 40, startPoint.y)).markInput().entries(display.craftingInputs.get(1)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + startX + 20, startPoint.y)).markInput().entries(display.craftingInputs.get(2)));
			
			widgets.add(Widgets.createSlot(new Point(startPoint.x, startPoint.y + 17)).entries(ITEM_BOWL_CALCITE).disableBackground());
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 20, startPoint.y + 17)).entries(SPIRIT_INSTILLER).disableBackground());
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 40, startPoint.y + 17)).entries(ITEM_BOWL_CALCITE).disableBackground());
			
			// output arrow and slot
			widgets.add(Widgets.createArrow(new Point(startPoint.x + 60, startPoint.y + 9)));
			widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 95, startPoint.y + 9)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 95, startPoint.y + 9)).markOutput().disableBackground().entries(output.get(0)));

			// description text
			// special handling for "1 second". Looks nicer
			TranslatableText text;
			if (display.craftingTime == 20) {
				text = new TranslatableText("container.spectrum.rei.pedestal_crafting.crafting_time_one_second_and_xp", 1, display.experience);
			} else {
				text = new TranslatableText("container.spectrum.rei.pedestal_crafting.crafting_time_and_xp", (display.craftingTime / 20), display.experience);
			}
			widgets.add(Widgets.createLabel(new Point(startPoint.x - 10, startPoint.y + 39), text).leftAligned().color(0x3f3f3f).noShadow());
		}
		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 60;
	}

}
