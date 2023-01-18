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
import net.minecraft.text.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class EnchantmentUpgradeCategory implements DisplayCategory<EnchantmentUpgradeDisplay> {

	@Override
	public CategoryIdentifier getCategoryIdentifier() {
		return SpectrumPlugins.ENCHANTMENT_UPGRADE;
	}

	@Override
	public Text getTitle() {
		return Text.translatable("container.spectrum.rei.enchantment_upgrading.title");
	}

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumBlocks.ENCHANTER);
	}

	@Override
	public List<Widget> setupDisplay(@NotNull EnchantmentUpgradeDisplay display, @NotNull Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 58 - 7, bounds.getCenterY() - 49);
		List<Widget> widgets = Lists.newArrayList();

		widgets.add(Widgets.createRecipeBase(bounds));

		if (!display.isUnlocked()) {
			widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 38), Text.translatable("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_1")).leftAligned().color(0x3f3f3f).noShadow());
			widgets.add(Widgets.createLabel(new Point(startPoint.x, startPoint.y + 48), Text.translatable("container.spectrum.rei.pedestal_crafting.recipe_not_unlocked_line_2")).leftAligned().color(0x3f3f3f).noShadow());
		} else {
			// enchanter structure background					                              destinationX	 destinationY          sourceX, sourceY, width, height
			widgets.add(Widgets.createTexturedWidget(EnchanterCategory.BACKGROUND_TEXTURE, startPoint.x + 12, startPoint.y + 21, 0, 0, 54, 54));

			List<EntryIngredient> inputs = display.getInputEntries();

			// Knowledge Gem and Enchanter
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 111, startPoint.y + 14)).markInput().entries(inputs.get(9)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 111, startPoint.y + 60)).entries(EnchanterCategory.ENCHANTER).disableBackground());

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
			widgets.add(Widgets.createArrow(new Point(startPoint.x + 80, startPoint.y + 40)));
			widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 111, startPoint.y + 40)));
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 111, startPoint.y + 40)).markOutput().disableBackground().entries(display.getOutputEntries().get(0)));

			// required amount
			Text text;
			if (display.requiredItemCount > 0) {
				text = Text.translatable("container.spectrum.rei.enchantment_upgrade.required_item_count", display.requiredItemCount);
				widgets.add(Widgets.createLabel(new Point(startPoint.x + 67, startPoint.y + 78), text).leftAligned().color(0x3f3f3f).noShadow());
			}
		}
		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 100;
	}

}
