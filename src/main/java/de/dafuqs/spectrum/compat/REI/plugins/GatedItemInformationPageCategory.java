package de.dafuqs.spectrum.compat.REI.plugins;

import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;

public abstract class GatedItemInformationPageCategory implements DisplayCategory<GatedItemInformationDisplay> {
	
	@Override
	public List<Widget> setupDisplay(GatedItemInformationDisplay display, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 60, bounds.getCenterY() - 42);
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		
		if (!display.isUnlocked()) {
			widgets.add(Widgets.createLabel(new Point(startPoint.x - 23, startPoint.y + 4), Text.translatable("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_1")).leftAligned().color(0x3f3f3f).noShadow());
			widgets.add(Widgets.createLabel(new Point(startPoint.x - 23, startPoint.y + 14), Text.translatable("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_2")).leftAligned().color(0x3f3f3f).noShadow());
		} else {
			Item item = display.getItem();
			widgets.add(Widgets.createSlot(new Point(startPoint.x, startPoint.y)).entries(display.getInputEntries().get(0)).markInput());
			widgets.add(Widgets.createLabel(new Point(startPoint.x + 20, startPoint.y + 4), item.getName()).leftAligned().color(0x000000).noShadow());
			
			Rectangle rectangle = new Rectangle(bounds.getCenterX() - (bounds.width / 2), bounds.y + 30, bounds.width, bounds.height - 30);
			widgets.add(Widgets.createSlotBase(rectangle));
			
			List<OrderedText> descriptionLines = MinecraftClient.getInstance().textRenderer.wrapLines(display.getDescription(), bounds.width - 11);
			widgets.add(new ScrollableTextWidget(rectangle, descriptionLines));
			
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 112, startPoint.y + 70)).entry(getBackgroundEntryStack()).disableBackground().notInteractable().disableHighlight().disableTooltips());
			
			return widgets;
		}
		return widgets;
	}
	
	@Override
	public int getDisplayHeight() {
		return 100;
	}
	
	public abstract EntryStack getBackgroundEntryStack();
	
}
