package de.dafuqs.spectrum.compat.REI;

import com.google.common.collect.Lists;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipe;
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
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.List;

public class AnvilCrushingCategory<R extends AnvilCrushingRecipe> implements DisplayCategory<AnvilCrushingRecipeDisplay<R>> {

	public static final CategoryIdentifier<AnvilCrushingRecipeDisplay> ID = CategoryIdentifier.of(new Identifier(SpectrumCommon.MOD_ID, "anvil_crushing"));

	private final static Identifier WALL_TEXTURE = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/anvil_crushing.png");
	private final static EntryIngredient ANVIL = EntryIngredients.of(new ItemStack(Items.ANVIL));

	@Override
	public CategoryIdentifier getCategoryIdentifier() {
		return ID;
	}

	@Override
	public Text getTitle() {
		return new TranslatableText("container.spectrum.rei.anvil_crushing.title");
	}

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(Blocks.ANVIL);
	}

	@Override
	public List<Widget> setupDisplay(AnvilCrushingRecipeDisplay display, Rectangle bounds) {

		Point startPoint = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 41);
		List<Widget> widgets = Lists.newArrayList();

		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 50, startPoint.y + 23)));
		widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 95, startPoint.y + 24)));

		List<EntryIngredient> input = display.getInputEntries();
		List<EntryIngredient> output = display.getOutputEntries();

		// input and output slots
		widgets.add(Widgets.createSlot(new Point(startPoint.x+20, startPoint.y+18)).entries(ANVIL).disableBackground().notInteractable());
		widgets.add(Widgets.createSlot(new Point(startPoint.x+20, startPoint.y+40)).markInput().entries(input.get(0)));
		widgets.add(Widgets.createSlot(new Point(startPoint.x+95, startPoint.y+23)).markOutput().disableBackground().entries(output.get(0)));

		// dirt  wall										  destinationX	 destinationY   sourceX, sourceY, width, height
		widgets.add(Widgets.createTexturedWidget(WALL_TEXTURE, startPoint.x, startPoint.y+9, 0, 0, 16, 48));

		// falling stripes for anvil
		widgets.add(Widgets.createTexturedWidget(WALL_TEXTURE, startPoint.x + 20, startPoint.y+8, 16, 0, 16, 16));

		// xp text
		widgets.add(Widgets.createLabel(new Point(startPoint.x+84, startPoint.y+48),
				new TranslatableText("container.spectrum.rei.anvil_crushing.plus_xp", display.experience)
		).leftAligned().color(0x3f3f3f).noShadow());

		// the tooltip text
		TranslatableText text;
		if(display.crushedItemsPerPointOfDamage >= 1) {
			text = new TranslatableText("container.spectrum.rei.anvil_crushing.low_force_required");
		} else if(display.crushedItemsPerPointOfDamage >= 0.5) {
			text = new TranslatableText("container.spectrum.rei.anvil_crushing.medium_force_required");
		} else {
			text = new TranslatableText("container.spectrum.rei.anvil_crushing.high_force_required");
		}
		widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 68), text).leftAligned().color(0x3f3f3f).noShadow());

		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 84;
	}

}
