package de.dafuqs.spectrum.compat.REI.plugins;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.REI.*;
import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.client.registry.display.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class AnvilCrushingCategory implements DisplayCategory<AnvilCrushingDisplay> {

	private final static Identifier WALL_TEXTURE = SpectrumCommon.locate("textures/gui/container/anvil_crushing.png");
	private final static EntryIngredient ANVIL = EntryIngredients.of(Items.ANVIL);

	@Override
	public CategoryIdentifier getCategoryIdentifier() {
		return SpectrumPlugins.ANVIL_CRUSHING;
	}

	@Override
	public Text getTitle() {
		return Text.translatable("container.spectrum.rei.anvil_crushing.title");
	}

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(Blocks.ANVIL);
	}

	@Override
	public List<Widget> setupDisplay(AnvilCrushingDisplay display, Rectangle bounds) {

		Point startPoint = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 41);
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));

		if (!display.isUnlocked()) {
			widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 33), Text.translatable("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_1")).leftAligned().color(0x3f3f3f).noShadow());
			widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 43), Text.translatable("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_2")).leftAligned().color(0x3f3f3f).noShadow());
		} else {
			widgets.add(Widgets.createArrow(new Point(startPoint.x + 50, startPoint.y + 23)));
			widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 95, startPoint.y + 24)));

			List<EntryIngredient> input = display.getInputEntries();
			List<EntryIngredient> output = display.getOutputEntries();

			// input and output slots
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 20, startPoint.y + 18)).entries(ANVIL).disableBackground().notInteractable());
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 20, startPoint.y + 40)).markInput().entries(input.get(0)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 95, startPoint.y + 23)).markOutput().disableBackground().entries(output.get(0)));

			// dirt  wall										  destinationX	 destinationY   sourceX, sourceY, width, height
			widgets.add(Widgets.createTexturedWidget(WALL_TEXTURE, startPoint.x, startPoint.y + 9, 0, 0, 16, 48));

			// falling stripes for anvil
			widgets.add(Widgets.createTexturedWidget(WALL_TEXTURE, startPoint.x + 20, startPoint.y + 8, 16, 0, 16, 16));

			// xp text
			widgets.add(Widgets.createLabel(new Point(startPoint.x + 84, startPoint.y + 48),
					Text.translatable("container.spectrum.rei.anvil_crushing.plus_xp", display.experience)
			).leftAligned().color(0x3f3f3f).noShadow());

			// the tooltip text
			Text text;
			if (display.crushedItemsPerPointOfDamage >= 1) {
				text = Text.translatable("container.spectrum.rei.anvil_crushing.low_force_required");
			} else if (display.crushedItemsPerPointOfDamage >= 0.5) {
				text = Text.translatable("container.spectrum.rei.anvil_crushing.medium_force_required");
			} else {
				text = Text.translatable("container.spectrum.rei.anvil_crushing.high_force_required");
			}
			widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 68), text).leftAligned().color(0x3f3f3f).noShadow());
		}

		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 84;
	}

}
