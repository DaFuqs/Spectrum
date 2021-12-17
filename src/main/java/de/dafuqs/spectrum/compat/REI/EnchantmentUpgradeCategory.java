package de.dafuqs.spectrum.compat.REI;

import com.google.common.collect.Lists;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.EnchantmentUpgradeRecipe;
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
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EnchantmentUpgradeCategory<R extends EnchantmentUpgradeRecipe> implements DisplayCategory<EnchantmentUpgradeRecipeDisplay<R>> {
	
	@Override
	public CategoryIdentifier getCategoryIdentifier() {
		return SpectrumPlugins.ENCHANTMENT_UPGRADE;
	}

	@Override
	public Text getTitle() {
		return new TranslatableText("container.spectrum.rei.enchantment_upgrading.title");
	}

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumBlocks.ENCHANTER);
	}

	@Override
	public List<Widget> setupDisplay(@NotNull EnchantmentUpgradeRecipeDisplay display, @NotNull Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 58 - 7, bounds.getCenterY() - 49);
		List<Widget> widgets = Lists.newArrayList();

		widgets.add(Widgets.createRecipeBase(bounds));

		// Searching for the usage or recipes for items will not trigger a
		// pedestal crafting recipe display. Searching for all recipes, that can
		// be triggered with a pedestal will, though.
		//
		// For the sake of not spoiling the surprise there will just be
		// a placeholder displayed instead of the actual recipe.
		//
		// It would be way better to just skip not unlocked recipes altogether.
		// but howwwwww...
		if(!display.isUnlocked()) {
			widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 33), new TranslatableText("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_1")).leftAligned().color(0x3f3f3f).noShadow());
			widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 43), new TranslatableText("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_2")).leftAligned().color(0x3f3f3f).noShadow());
		} else {
			// Knowledge Gem and Enchanter
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 111, startPoint.y + 14)).markInput().entries((EntryIngredient) display.inputs.get(9)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 111, startPoint.y + 60)).entries(EnchanterCategory.ENCHANTER).disableBackground());
			
			// center input slot
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 31, startPoint.y + 40)).markInput().entries((EntryIngredient) display.inputs.get(0)));
			
			// surrounding input slots
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 18, startPoint.y + 10)).markInput().entries((EntryIngredient) display.inputs.get(1)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 44, startPoint.y + 10)).markInput().entries((EntryIngredient) display.inputs.get(2)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 62, startPoint.y + 28)).markInput().entries((EntryIngredient) display.inputs.get(3)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 62, startPoint.y + 54)).markInput().entries((EntryIngredient) display.inputs.get(4)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 44, startPoint.y + 72)).markInput().entries((EntryIngredient) display.inputs.get(5)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 18, startPoint.y + 72)).markInput().entries((EntryIngredient) display.inputs.get(6)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x, startPoint.y + 54)).markInput().entries((EntryIngredient) display.inputs.get(7)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x, startPoint.y + 28)).markInput().entries((EntryIngredient) display.inputs.get(8)));
			
			// output arrow and slot
			List<EntryIngredient> output = display.getOutputEntries();
			widgets.add(Widgets.createArrow(new Point(startPoint.x + 80, startPoint.y + 40)));
			widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 111, startPoint.y + 40)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 111, startPoint.y + 40)).markOutput().disableBackground().entries(output.get(0)));
			
			// required amount
			TranslatableText text;
			if (display.requiredItemCount > 0) {
				text = new TranslatableText("container.spectrum.rei.enchantment_upgrade.required_item_count", display.requiredItemCount);
				widgets.add(Widgets.createLabel(new Point(startPoint.x + 67, startPoint.y + 78), text).leftAligned().color(0x3f3f3f).noShadow());
			}
		}
		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 100;
	}

}
