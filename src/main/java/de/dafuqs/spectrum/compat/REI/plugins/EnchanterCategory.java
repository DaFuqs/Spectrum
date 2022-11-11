package de.dafuqs.spectrum.compat.REI.plugins;

import com.google.common.collect.Lists;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
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
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EnchanterCategory implements DisplayCategory<EnchanterDisplay> {
	
	public final static Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/container/enchanter.png");
	public static final EntryIngredient ENCHANTER = EntryIngredients.of(SpectrumBlocks.ENCHANTER);
	
	@Override
	public CategoryIdentifier getCategoryIdentifier() {
		return SpectrumPlugins.ENCHANTER;
	}
	
	@Override
	public Text getTitle() {
		return new TranslatableText("container.spectrum.rei.enchanting.title");
	}
	
	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumBlocks.ENCHANTER);
	}
	
	@Override
	public List<Widget> setupDisplay(@NotNull EnchanterDisplay display, @NotNull Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 58 - 7, bounds.getCenterY() - 49);
		List<Widget> widgets = Lists.newArrayList();
		
		widgets.add(Widgets.createRecipeBase(bounds));
		
		if (!display.isUnlocked()) {
			widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 38), new TranslatableText("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_1")).leftAligned().color(0x3f3f3f).noShadow());
			widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 48), new TranslatableText("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_2")).leftAligned().color(0x3f3f3f).noShadow());
		} else {
			// enchanter structure background					            destinationX	 destinationY   sourceX, sourceY, width, height
			widgets.add(Widgets.createTexturedWidget(BACKGROUND_TEXTURE, startPoint.x + 12, startPoint.y + 21, 0, 0, 54, 54));
			
			// Knowledge Gem and Enchanter
			List<EntryIngredient> inputs = display.getInputEntries();
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 111, startPoint.y + 14)).markInput().entries(inputs.get(9)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 111, startPoint.y + 60)).entries(ENCHANTER).disableBackground());
			
			// center input slot
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 31, startPoint.y + 40)).markInput().entries(inputs.get(0)));
			
			// surrounding input slots
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 18, startPoint.y + 9)).markInput().entries(inputs.get(1)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 44, startPoint.y + 9)).markInput().entries(inputs.get(2)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 62, startPoint.y + 27)).markInput().entries(inputs.get(3)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 62, startPoint.y + 53)).markInput().entries(inputs.get(4)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 44, startPoint.y + 71)).markInput().entries(inputs.get(5)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 18, startPoint.y + 71)).markInput().entries(inputs.get(6)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x, startPoint.y + 53)).markInput().entries(inputs.get(7)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x, startPoint.y + 27)).markInput().entries(inputs.get(8)));
			
			// output arrow and slot
			List<EntryIngredient> output = display.getOutputEntries();
			widgets.add(Widgets.createArrow(new Point(startPoint.x + 80, startPoint.y + 40)).animationDurationTicks(display.craftingTime));
			widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 111, startPoint.y + 40)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 111, startPoint.y + 40)).markOutput().disableBackground().entries(output.get(0)));
			
			// duration and XP requirements
			// special handling for "1 second". Looks nicer
			TranslatableText text;
			if (display.craftingTime == 20) {
				text = new TranslatableText("container.spectrum.rei.enchanting.crafting_time_one_second", 1);
			} else {
				text = new TranslatableText("container.spectrum.rei.enchanting.crafting_time", (display.craftingTime / 20));
			}
			widgets.add(Widgets.createLabel(new Point(startPoint.x + 70, startPoint.y + 85), text).leftAligned().color(0x3f3f3f).noShadow());
		}
		return widgets;
	}
	
	@Override
	public int getDisplayHeight() {
		return 100;
	}
	
}
