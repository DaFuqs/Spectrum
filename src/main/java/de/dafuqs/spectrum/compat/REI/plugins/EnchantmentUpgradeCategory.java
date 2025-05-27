package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.blocks.enchanter.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.compat.REI.widgets.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.entry.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.component.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class EnchantmentUpgradeCategory extends EnchanterCategory<EnchantmentUpgradeDisplay> {
	
	public static final int NORMAL_COLOR = 0x4d3655;
	private static final int OVERCHANT_COLOR = 0xdb3564;
	
	@Override
	public CategoryIdentifier<EnchantmentUpgradeDisplay> getCategoryIdentifier() {
		return SpectrumPlugins.ENCHANTMENT_UPGRADE;
	}
	
	@Override
	public Text getTitle() {
		return Text.translatable("container.spectrum.rei.enchantment_upgrading.title");
	}
	
	@Override
	public int getCraftingTime(@NotNull EnchantmentUpgradeDisplay display) {
		return 0;
	}
	
	@Override
	public Text getDescriptionText(@NotNull EnchantmentUpgradeDisplay display) {
		return Text.translatable("container.spectrum.rei.enchantment_upgrade.required_item_count", 0);
	}
	
	@Override
	public void setupWidgets(Point startPoint, Rectangle bounds, List<Widget> widgets, @NotNull EnchantmentUpgradeDisplay display) {
		boolean overUnlocked = AdvancementHelper.hasAdvancement(MinecraftClient.getInstance().player, SpectrumAdvancements.OVERENCHANTING);
		List<EntryIngredient> inputs = display.getInputEntries();
		
		// enchanter structure background					            destinationX	 destinationY   sourceX, sourceY, width, height
		widgets.add(Widgets.createTexturedWidget(BACKGROUND_TEXTURE, startPoint.x - 8 + 12, startPoint.y - 7 + 21, 0, 0, 54, 54));
		
		// Overchanting Star
		if (overUnlocked && display.levelCap > display.maxNormal)
			widgets.add(Widgets.withTooltip(
					Widgets.withBounds(Widgets.createTexturedWidget(BACKGROUND_TEXTURE, startPoint.x - 10, startPoint.y + 2, 64, 0, 16, 16), new Rectangle(startPoint.x - 10, startPoint.y + 2, 16, 16)),
					Text.translatable(EnchanterBlockEntity.OVERCHANTING_TOOLTIP).styled(s -> s.withColor(OVERCHANT_COLOR))));
		
		var max = overUnlocked ? display.levelCap : display.maxNormal;
		widgets.add(Widgets.createButton(new Rectangle(startPoint.x - 8 + 84, startPoint.y + 20, 8, 8), Text.literal("-"))
				.onClick(b -> display.index = Math.clamp(display.index - 1, 1, max - 1))); // decrement
		widgets.add(Widgets.createButton(new Rectangle(startPoint.x - 8 + 94, startPoint.y + 20, 8, 8), Text.literal("+"))
				.onClick(b -> display.index = Math.clamp(display.index + 1, 1, max - 1))); // increment
		
		// surrounding input slots
		widgets.add(Widgets.createSlot(new Point(startPoint.x - 8 + 18, startPoint.y - 7 + 9)).markInput().entries(inputs.get(0)));
		widgets.add(Widgets.createSlot(new Point(startPoint.x - 8 + 44, startPoint.y - 7 + 9)).markInput().entries(inputs.get(1)));
		widgets.add(Widgets.createSlot(new Point(startPoint.x - 8 + 62, startPoint.y - 7 + 27)).markInput().entries(inputs.get(2)));
		widgets.add(Widgets.createSlot(new Point(startPoint.x - 8 + 62, startPoint.y - 7 + 53)).markInput().entries(inputs.get(3)));
		widgets.add(Widgets.createSlot(new Point(startPoint.x - 8 + 44, startPoint.y - 7 + 71)).markInput().entries(inputs.get(4)));
		widgets.add(Widgets.createSlot(new Point(startPoint.x - 8 + 18, startPoint.y - 7 + 71)).markInput().entries(inputs.get(5)));
		widgets.add(Widgets.createSlot(new Point(startPoint.x - 8, startPoint.y - 7 + 53)).markInput().entries(inputs.get(6)));
		widgets.add(Widgets.createSlot(new Point(startPoint.x - 8, startPoint.y - 7 + 27)).markInput().entries(inputs.get(7)));
		
		// Knowledge Gem and Enchanter
		widgets.add(new IndexedEntryWidget(new Point(startPoint.x - 8 + 111, startPoint.y - 7 + 14), () -> display.index - 1).markInput()
				.entries(inputs.get(overUnlocked ? EnchantmentUpgradeDisplay.OVERXP_INDEX : EnchantmentUpgradeDisplay.XP_INDEX)));
		widgets.add(Widgets.createSlot(new Point(startPoint.x - 8 + 111, startPoint.y - 7 + 60)).entries(ENCHANTER).disableBackground());
		
		// center input slot
		widgets.add(new IndexedEntryWidget(new Point(startPoint.x - 8 + 31, startPoint.y - 7 + 40), () -> display.index - 1).markInput()
				.entries(inputs.get(overUnlocked ? EnchantmentUpgradeDisplay.OVERCHANT_INDEX : EnchantmentUpgradeDisplay.NORMAL_INDEX)));
		
		// output arrow
		widgets.add(Widgets.createArrow(new Point(startPoint.x - 8 + 80, startPoint.y - 7 + 40)).animationDurationTicks(getCraftingTime(display)));
		widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x - 8 + 111, startPoint.y - 7 + 40)));
		
		// output slot
		var outputSlot = new IndexedEntryWidget(new Point(startPoint.x - 8 + 111, startPoint.y - 7 + 40), () -> display.index - 1).markOutput().disableBackground()
				.entries(overUnlocked ? display.overchantOutputs : display.normalOutputs);
		
		widgets.add(outputSlot);
		
		// labels
		var levelLabel = Widgets.createLabel(new Point(startPoint.x - 11 + 70, startPoint.y + 2), getDescriptionText(display)).leftAligned().color(NORMAL_COLOR).noShadow();
		levelLabel.setOnRender((drawContext, label) -> {
			var level = display.index;
			
			if (level > display.maxNormal)
				label.setColor(OVERCHANT_COLOR);
			else
				label.setColor(NORMAL_COLOR);
			
			label.setMessage(Text.translatable(EnchanterBlockEntity.LEVEL_TRANS, level, level + 1));
		});
		
		var costLabel = Widgets.createLabel(new Point(startPoint.x - 11 + 70, startPoint.y - 11 + 85), getDescriptionText(display)).leftAligned().color(NORMAL_COLOR).noShadow();
		costLabel.setOnRender((drawContext, label) -> {
			var level = display.index;
			
			label.setMessage(Text.translatable(EnchanterBlockEntity.ITEM_TRANS, display.itemScaling.apply(level)));
		});
		
		widgets.add(levelLabel);
		widgets.add(costLabel);
		widgets.add(Widgets.createLabel(new Point(startPoint.x - 7, startPoint.y + 2 + 82), display.transKey).color(NORMAL_COLOR).shadow(false).leftAligned());
	}
	
	@Override
	public int getDisplayHeight() {
		return super.getDisplayHeight() + 10;
	}
}
