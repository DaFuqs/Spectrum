package de.dafuqs.spectrum.compat.REI.plugins;

import com.google.common.collect.Lists;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import ladysnake.satin.api.event.PickEntityShaderCallback;
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
import me.shedaniel.rei.plugin.client.categories.cooking.DefaultCookingCategory;
import me.shedaniel.rei.plugin.common.displays.cooking.DefaultBlastingDisplay;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CinderhearthCategory implements DisplayCategory<CinderhearthDisplay> {
	
	private static final EntryIngredient CINDERHEARTH = EntryIngredients.of(SpectrumBlocks.CINDERHEARTH);
	
	@Override
	public CategoryIdentifier getCategoryIdentifier() {
		return SpectrumPlugins.CINDERHEARTH;
	}
	
	@Override
	public Text getTitle() {
		return new TranslatableText("block.spectrum.cinderhearth");
	}
	
	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumBlocks.CINDERHEARTH);
	}
	
	@Override
	public List<Widget> setupDisplay(@NotNull CinderhearthDisplay display, @NotNull Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 62, bounds.y + 9);
		
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));

		if (!display.isUnlocked()) {
			widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 33), new TranslatableText("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_1")).leftAligned().color(0x3f3f3f).noShadow());
			widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 43), new TranslatableText("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_2")).leftAligned().color(0x3f3f3f).noShadow());
		} else {
			widgets.add(Widgets.createSlot(new Point(startPoint.x, startPoint.y)).markInput().entries(display.getInputEntries().get(0))); // input slot
			
			// output arrow and slot
			widgets.add(Widgets.createArrow(new Point(startPoint.x + 19, startPoint.y + 5)).animationDurationTicks(display.craftingTime));
			widgets.add(Widgets.createBurningFire(new Point(startPoint.x + 1, startPoint.y + 20)).animationDurationMS(10000));
			
			List<Pair<ItemStack, Float>> outputs = display.outputsWithChance;
			for (int i = 0; i < outputs.size(); i++) {
				Pair<ItemStack, Float> currentOutput = outputs.get(i);
				ItemStack outputStack = currentOutput.getLeft();
				Float chance = currentOutput.getRight();
				
				Point point = new Point(startPoint.x + 50 + i * 28, startPoint.y + 5);
				widgets.add(Widgets.createResultSlotBackground(point));
				widgets.add(Widgets.createSlot(point).disableBackground().markOutput().entries(EntryIngredients.of(outputStack)));
				if(chance < 1.0) {
					widgets.add(Widgets.createLabel(new Point(point.x - 2, point.y + 23), new LiteralText((int) (chance * 100) + " %")).leftAligned().color(0x3f3f3f).noShadow());
				}
			}
			
			// description text
			// special handling for "1 second". Looks nicer
			TranslatableText text;
			if (display.craftingTime == 20) {
				text = new TranslatableText("container.spectrum.rei.pedestal_crafting.crafting_time_one_second_and_xp", 1, display.experience);
			} else {
				text = new TranslatableText("container.spectrum.rei.pedestal_crafting.crafting_time_and_xp", (display.craftingTime / 20), display.experience);
			}
			widgets.add(Widgets.createLabel(new Point(startPoint.x - 6, startPoint.y + 43), text).leftAligned().color(0x3f3f3f).noShadow());
		}
		return widgets;
	}
	
	@Override
	public int getDisplayHeight() {
		return 65;
	}
	
}
