package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.REI.*;
import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class AnvilCrushingCategory extends GatedDisplayCategory<AnvilCrushingDisplay> {
	
	private final static Identifier WALL_TEXTURE = SpectrumCommon.locate("textures/gui/container/anvil_crushing.png");
	private final static EntryIngredient ANVIL = EntryIngredients.of(Items.ANVIL);
	
	@Override
	public CategoryIdentifier<AnvilCrushingDisplay> getCategoryIdentifier() {
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
	public void setupWidgets(Point startPoint, Rectangle bounds, List<Widget> widgets, @NotNull AnvilCrushingDisplay display) {
		widgets.add(Widgets.createArrow(new Point(startPoint.x + 50, startPoint.y - 8 + 23)));
		widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 95, startPoint.y - 8 + 24)));
		
		List<EntryIngredient> input = display.getInputEntries();
		List<EntryIngredient> output = display.getOutputEntries();
		
		// input and output slots
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 20, startPoint.y - 8 + 18)).entries(ANVIL).disableBackground().notInteractable());
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 20, startPoint.y - 8 + 40)).markInput().entries(input.get(0)));
		widgets.add(Widgets.createSlot(new Point(startPoint.x + 95, startPoint.y - 8 + 23)).markOutput().disableBackground().entries(output.get(0)));
		
		// dirt  wall										  destinationX	 destinationY   sourceX, sourceY, width, height
		widgets.add(Widgets.createTexturedWidget(WALL_TEXTURE, startPoint.x, startPoint.y - 8 + 9, 0, 0, 16, 48));
		
		// falling stripes for anvil
		widgets.add(Widgets.createTexturedWidget(WALL_TEXTURE, startPoint.x + 20, startPoint.y - 8 + 8, 16, 0, 16, 16));
		
		// xp text
		widgets.add(Widgets.createLabel(new Point(startPoint.x + 84, startPoint.y - 8 + 48),
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
		widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y - 10 + 64), text).leftAligned().color(0x3f3f3f).noShadow());
	}
	
	@Override
	public int getDisplayHeight() {
		return 72;
	}
	
}
