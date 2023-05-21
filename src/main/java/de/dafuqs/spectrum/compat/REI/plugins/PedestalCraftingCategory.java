package de.dafuqs.spectrum.compat.REI.plugins;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.enums.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.fabricmc.api.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class PedestalCraftingCategory extends GatedDisplayCategory<PedestalCraftingDisplay> {
	
	@Override
	public CategoryIdentifier<PedestalCraftingDisplay> getCategoryIdentifier() {
		return SpectrumPlugins.PEDESTAL_CRAFTING;
	}
	
	@Override
	public Identifier getIdentifier() {
		return SpectrumCommon.locate("pedestal_crafting");
	}
	
	@Override
	public Text getTitle() {
		return Text.translatable("container.spectrum.rei.pedestal_crafting.title");
	}
	
	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumBlocks.PEDESTAL_BASIC_AMETHYST);
	}
	
	@Override
	public void setupWidgets(Point startPoint, Rectangle bounds, List<Widget> widgets, @NotNull PedestalCraftingDisplay display) {
		Identifier backgroundTexture = PedestalScreen.getBackgroundTextureForTier(display.getTier());
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 60, startPoint.y + 1 + 18)).animationDurationTicks(display.craftingTime));
		
		// crafting grid slots
		List<Slot> slots = Lists.newArrayList();
		for (int y = 0; y < 3; y++)
			for (int x = 0; x < 3; x++)
				slots.add(Widgets.createSlot(new Point(startPoint.x + 1 + x * 18, startPoint.y + 1 + 1 + y * 18)).disableBackground().markInput());
		
		// set crafting slot contents
		List<EntryIngredient> input = display.getInputEntries();
		int shownGemstoneSlotCount = display.getTier() == PedestalRecipeTier.COMPLEX ? 5 : display.getTier() == PedestalRecipeTier.ADVANCED ? 4 : 3;
		int gemstoneDustStartSlot = 9;
		for (int i = 0; i < 9; i++) {
			if (!input.get(i).isEmpty()) {
				slots.get(i).disableBackground().entries(input.get(i));
			}
		}
		
		// gemstone dust slots
		int gemstoneSlotStartX = shownGemstoneSlotCount == 5 ? -45 : shownGemstoneSlotCount == 4 ? -40 : -31;
		int gemstoneSlotTextureStartX = shownGemstoneSlotCount == 5 ? 43 : shownGemstoneSlotCount == 4 ? 52 : 61;
		for (int x = 0; x < shownGemstoneSlotCount; x++) {
			slots.add(Widgets.createSlot(new Point(bounds.getCenterX() + x * 18 + gemstoneSlotStartX, startPoint.y + 1 + 60)).disableBackground().markInput());
			if (!input.get(gemstoneDustStartSlot + x).isEmpty()) {
				slots.get(9 + x).entries(input.get(gemstoneDustStartSlot + x));
			}
		}
		widgets.addAll(slots);
		
		// output
		List<EntryIngredient> results = display.getOutputEntries();
		EntryIngredient result = EntryIngredient.of(results.get(0));
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 95, startPoint.y + 1 + 19)).entries(result).disableBackground().markOutput());
		
		// the gemstone slot background texture				  destinationX				 destinationY	   sourceX, sourceY, width, height
		widgets.add(Widgets.createTexturedWidget(backgroundTexture, bounds.getCenterX() + gemstoneSlotStartX - 1, startPoint.y + 1 + 59, gemstoneSlotTextureStartX, 76, 18 * shownGemstoneSlotCount, 18));
		// crafting input texture
		widgets.add(Widgets.createTexturedWidget(backgroundTexture, startPoint.x, startPoint.y + 1, 29, 18, 54, 54));
		// crafting output texture
		widgets.add(Widgets.createTexturedWidget(backgroundTexture, startPoint.x + 94 - 4, startPoint.y + 1 + 18 - 4, 122, 32, 26, 26));
		// miniature gemstones texture
		widgets.add(Widgets.createTexturedWidget(backgroundTexture, startPoint.x + 94 - 12, startPoint.y + 1 + 18 + 20, 200, 0, 40, 16));
		
		// description text
		// special handling for "1 second". Looks nicer
		Text text;
		if (display.craftingTime == 20) {
			text = Text.translatable("container.spectrum.rei.pedestal_crafting.crafting_time_one_second_and_xp", 1, display.experience);
		} else {
			text = Text.translatable("container.spectrum.rei.pedestal_crafting.crafting_time_and_xp", (display.craftingTime / 20), display.experience);
		}
		widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 82), text).leftAligned().color(0x3f3f3f).noShadow());
	}
	
	@Override
	public int getDisplayHeight() {
		return 100;
	}
	
}
