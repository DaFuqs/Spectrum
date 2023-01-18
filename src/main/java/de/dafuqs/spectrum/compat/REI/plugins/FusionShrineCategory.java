package de.dafuqs.spectrum.compat.REI.plugins;

import com.google.common.collect.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.client.registry.display.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.fabricmc.api.*;
import net.minecraft.fluid.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class FusionShrineCategory implements DisplayCategory<FusionShrineDisplay> {

	private static final EntryIngredient FUSION_SHRINE_BASALT = EntryIngredients.of(SpectrumBlocks.FUSION_SHRINE_BASALT);

	@Override
	public CategoryIdentifier getCategoryIdentifier() {
		return SpectrumPlugins.FUSION_SHRINE;
	}

	@Override
	public Text getTitle() {
		return Text.translatable("block.spectrum.fusion_shrine");
	}

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumBlocks.FUSION_SHRINE_BASALT);
	}

	@Override
	public List<Widget> setupDisplay(@NotNull FusionShrineDisplay display, @NotNull Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 43);
		List<Widget> widgets = Lists.newArrayList();

		widgets.add(Widgets.createRecipeBase(bounds));

		if (!display.isUnlocked()) {
			widgets.add(Widgets.createLabel(new Point(startPoint.x - 6, startPoint.y + 33), Text.translatable("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_1")).leftAligned().color(0x3f3f3f).noShadow());
			widgets.add(Widgets.createLabel(new Point(startPoint.x - 6, startPoint.y + 43), Text.translatable("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_2")).leftAligned().color(0x3f3f3f).noShadow());
		} else {
			List<EntryIngredient> inputs = display.getInputEntries();

			// shrine + fluid
			if (!inputs.get(0).equals(EntryIngredients.of(Fluids.EMPTY))) {
				widgets.add(Widgets.createSlot(new Point(startPoint.x + 10, startPoint.y + 35)).entries(FUSION_SHRINE_BASALT).disableBackground());
				widgets.add(Widgets.createSlot(new Point(startPoint.x + 30, startPoint.y + 35)).markInput().entries(inputs.get(0)));
			} else {
				widgets.add(Widgets.createSlot(new Point(startPoint.x + 20, startPoint.y + 35)).entries(FUSION_SHRINE_BASALT).disableBackground());
			}

			// input slots
			int ingredientSize = inputs.size() - 1;
			int startX = Math.max(-10, 10 - ingredientSize * 10);
			for (int i = 0; i < ingredientSize; i++) {
				EntryIngredient currentIngredient = inputs.get(i + 1);
				widgets.add(Widgets.createSlot(new Point(startPoint.x + startX + i * 20, startPoint.y + 9)).markInput().entries(currentIngredient));
			}

			// output arrow and slot
			widgets.add(Widgets.createArrow(new Point(startPoint.x + 60, startPoint.y + 35)));
			widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 95, startPoint.y + 35)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 95, startPoint.y + 35)).markOutput().disableBackground().entries(display.getOutputEntries().get(0)));

			if (display.getDescription().isPresent()) {
				Text description = display.getDescription().get();
				widgets.add(Widgets.createLabel(new Point(startPoint.x - 10, startPoint.y + 65), description).leftAligned().color(0x3f3f3f).noShadow());
			}

			// description text
			// special handling for "1 second". Looks nicer
			Text text;
			if (display.craftingTime == 20) {
				text = Text.translatable("container.spectrum.rei.pedestal_crafting.crafting_time_one_second_and_xp", 1, display.experience);
			} else {
				text = Text.translatable("container.spectrum.rei.pedestal_crafting.crafting_time_and_xp", (display.craftingTime / 20), display.experience);
			}
			widgets.add(Widgets.createLabel(new Point(startPoint.x - 10, startPoint.y + 75), text).leftAligned().color(0x3f3f3f).noShadow());


		}
		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 100;
	}

}
