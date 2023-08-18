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
		
		int powderSlotCount = display.getTier().getPowderSlotCount();
		int gemstoneSlotStartX = startPoint.x + 58 - powderSlotCount * 9;
		int gemstoneSlotTextureU = 88 - powderSlotCount * 9;
		
		List<EntryIngredient> input = display.getInputEntries();
		int gemstoneDustStartSlot = 9;
		
		// crafting grid slots
		List<Slot> slots = Lists.newArrayList();
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				slots.add(Widgets.createSlot(new Point(startPoint.x + 1 + x * 18, startPoint.y + 2 + y * 18)).disableBackground().markInput().disableBackground().entries(input.get(y * 3 + x)));
			}
		}
		
		// powder slots
		for (int i = 0; i < powderSlotCount; i++) {
			slots.add(Widgets.createSlot(new Point(gemstoneSlotStartX + 1 + i * 18, startPoint.y + 61)).disableBackground().markInput().entries(input.get(gemstoneDustStartSlot + i)));
		}
		widgets.addAll(slots);
		
		// the gemstone slot background texture				  destinationX				 destinationY	   sourceX, sourceY, width, height
		widgets.add(Widgets.createTexturedWidget(backgroundTexture, gemstoneSlotStartX, startPoint.y + 60, gemstoneSlotTextureU, 76, powderSlotCount * 18, 18));
		// crafting input texture
		widgets.add(Widgets.createTexturedWidget(backgroundTexture, startPoint.x, startPoint.y + 1, 29, 18, 54, 54));
		// crafting output texture
		widgets.add(Widgets.createTexturedWidget(backgroundTexture, startPoint.x + 90, startPoint.y + 15, 122, 32, 26, 26));
		// miniature gemstones texture
		widgets.add(Widgets.createTexturedWidget(backgroundTexture, startPoint.x + 82, startPoint.y + 39, 200, 0, 40, 16));
		
		// description text
		// special handling for "1 second". Looks nicer
		Text text;
		if (display.craftingTime == 20) {
			text = Text.translatable("container.spectrum.rei.pedestal_crafting.crafting_time_one_second_and_xp", 1, display.experience);
		} else {
			text = Text.translatable("container.spectrum.rei.pedestal_crafting.crafting_time_and_xp", (display.craftingTime / 20), display.experience);
		}
		widgets.add(Widgets.createLabel(new Point(bounds.getCenterX(), startPoint.y + 82), text).centered().color(0x3f3f3f).noShadow());
		
		if (display.shapeless) {
			widgets.add(Widgets.createShapelessIcon(new Point(startPoint.x + 108, startPoint.y + 4)));
		}
		
		// output
		List<EntryIngredient> results = display.getOutputEntries();
		EntryIngredient result = EntryIngredient.of(results.get(0));
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 95, startPoint.y + 20)).entries(result).disableBackground().markOutput());
	}
	
	@Override
	public int getDisplayHeight() {
		return 100;
	}
	
}
