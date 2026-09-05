package de.dafuqs.spectrum.compat.REI.plugins;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

import java.util.*;

// see me.shedaniel.rei.plugin.client.categories.crafting.DefaultCraftingCategory
public class GatedCraftingCategory extends GatedDisplayCategory<GatedCraftingDisplay> {
	
	@Override
	public CategoryIdentifier<GatedCraftingDisplay> getCategoryIdentifier() {
		return SpectrumPlugins.GATED_CRAFTING;
	}
	
	@Override
	public ResourceLocation getIdentifier() {
		return SpectrumCommon.locate("gated_crafting");
	}
	
	@Override
	public Component getTitle() {
		return Component.translatable("container.spectrum.rei.gated_crafting.title");
	}
	
	@Override
	public Renderer getIcon() {
		return EntryStacks.of(Blocks.CRAFTING_TABLE);
	}
	
	@Override
	public void setupWidgets(Point startPoint, Rectangle bounds, List<Widget> widgets, GatedCraftingDisplay display) {
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 60, startPoint.y + 18)));
		widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 95, startPoint.y + 19)));
		List<InputIngredient<EntryStack<?>>> input = display.getInputIngredients(3, 3);
		List<Slot> slots = Lists.newArrayList();
		for (int y = 0; y < 3; y++)
			for (int x = 0; x < 3; x++)
				slots.add(Widgets.createSlot(new Point(startPoint.x + 1 + x * 18, startPoint.y + 1 + y * 18)).markInput());
		for (InputIngredient<EntryStack<?>> ingredient : input) {
			slots.get(ingredient.getIndex()).entries(ingredient.get());
		}
		widgets.addAll(slots);
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 95, startPoint.y + 19)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
		if (display.shapeless) {
			widgets.add(Widgets.createShapelessIcon(bounds));
		}
	}
}
