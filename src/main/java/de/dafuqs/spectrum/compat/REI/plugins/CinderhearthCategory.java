package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.util.*;
import net.fabricmc.api.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class CinderhearthCategory extends GatedDisplayCategory<CinderhearthDisplay> {
	
	@Override
	public CategoryIdentifier getCategoryIdentifier() {
		return SpectrumPlugins.CINDERHEARTH;
	}
	
	@Override
	public Text getTitle() {
		return Text.translatable("block.spectrum.cinderhearth");
	}
	
	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumBlocks.CINDERHEARTH);
	}
	
	@Override
	public void setupWidgets(Point startPoint, Rectangle bounds, List<Widget> widgets, @NotNull CinderhearthDisplay display) {
		widgets.add(Widgets.createSlot(new Point(startPoint.x - 6, startPoint.y + 2)).markInput().entries(display.getInputEntries().get(0))); // input slot
		widgets.add(Widgets.createBurningFire(new Point(startPoint.x - 6, startPoint.y + 1 + 20)).animationDurationMS(10000));
		widgets.add(Widgets.createArrow(new Point(startPoint.x - 6 + 18, startPoint.y + 2 + 5)).animationDurationTicks(display.craftingTime));
		
		// output arrow and slots
		List<Pair<ItemStack, Float>> outputs = display.outputsWithChance;
		for (int i = 0; i < outputs.size(); i++) {
			Pair<ItemStack, Float> currentOutput = outputs.get(i);
			ItemStack outputStack = currentOutput.getLeft();
			Float chance = currentOutput.getRight();
			
			Point point = new Point(startPoint.x - 6 + 49 + i * 28, startPoint.y + 1 + 5);
			widgets.add(Widgets.createResultSlotBackground(point));
			widgets.add(Widgets.createSlot(point).disableBackground().markOutput().entries(EntryIngredients.of(outputStack)));
			if (chance < 1.0) {
				widgets.add(Widgets.createLabel(new Point(point.x - 2, point.y + 23), Text.literal((int) (chance * 100) + " %")).leftAligned().color(0x3f3f3f).noShadow());
			}
		}
		
		// description text
		// special handling for "1 second". Looks nicer
		Text text;
		if (display.craftingTime == 20) {
			text = Text.translatable("container.spectrum.rei.pedestal_crafting.crafting_time_one_second_and_xp", 1, display.experience);
		} else {
			text = Text.translatable("container.spectrum.rei.pedestal_crafting.crafting_time_and_xp", (display.craftingTime / 20), display.experience);
		}
		widgets.add(Widgets.createLabel(new Point(startPoint.x - 6, startPoint.y + 1 + 43), text).leftAligned().color(0x3f3f3f).noShadow());
	}
	
	@Override
	public int getDisplayHeight() {
		return 65;
	}
	
}
