package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.fabricmc.api.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public abstract class PotionWorkshopCategory extends GatedDisplayCategory<PotionWorkshopRecipeDisplay> {
	
	public final static Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/container/potion_workshop_3_slots.png");
	
	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumBlocks.POTION_WORKSHOP);
	}
	
	
	@Override
	public void setupWidgets(Point startPoint, Rectangle bounds, List<Widget> widgets, @NotNull PotionWorkshopRecipeDisplay display) {
		// bubbles
		widgets.add(Widgets.createTexturedWidget(BACKGROUND_TEXTURE, startPoint.x + 18, startPoint.y + 1 + 19, 197, 0, 10, 27));
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 60, startPoint.y + 1 + 18)).animationDurationTicks(display.craftingTime));
		
		// input slots
		List<EntryIngredient> inputs = display.getInputEntries();
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 15, startPoint.y + 1 + 49)).entries(inputs.get(0)).markInput()); // mermaids gem
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 63, startPoint.y + 1)).entries(inputs.get(1)).markInput()); // base input
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 15, startPoint.y + 1)).entries(inputs.get(2)).markInput()); // input 1
		widgets.add(Widgets.createSlot(new Point(startPoint.x, startPoint.y + 1 + 20)).entries(inputs.get(3)).markInput()); // input 2
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 30, startPoint.y + 1 + 20)).entries(inputs.get(4)).markInput()); // input 3
		
		// output slot
		List<EntryIngredient> results = display.getOutputEntries();
		EntryIngredient result = EntryIngredient.of(results.get(0));
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 95, startPoint.y + 1 + 19)).entries(result).markOutput());
		
		// description text
		Text text = Text.translatable("container.spectrum.rei.potion_workshop.crafting_time", (display.craftingTime / 20));
		widgets.add(Widgets.createLabel(new Point(startPoint.x + 40, startPoint.y + 1 + 54), text).leftAligned().color(0x3f3f3f).noShadow());
	}
	
	@Override
	public int getDisplayHeight() {
		return 77;
	}
	
}
