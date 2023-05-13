package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.fabricmc.api.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

@Environment(EnvType.CLIENT)
public class EnchanterEnchantingCategory extends EnchanterCategory<EnchanterEnchantingDisplay> {
	
	public final static Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/container/enchanter.png");
	public static final EntryIngredient ENCHANTER = EntryIngredients.of(SpectrumBlocks.ENCHANTER);
	
	@Override
	public CategoryIdentifier getCategoryIdentifier() {
		return SpectrumPlugins.ENCHANTER_CRAFTING;
	}
	
	@Override
	public Text getTitle() {
		return Text.translatable("container.spectrum.rei.enchanting.title");
	}
	
	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumBlocks.ENCHANTER);
	}
	
	@Override
	public int getCraftingTime(@NotNull EnchanterEnchantingDisplay display) {
		return display.craftingTime;
	}
	
	@Override
	public Text getDescriptionText(@NotNull EnchanterEnchantingDisplay display) {
		// duration and XP requirements
		// special handling for "1 second". Looks nicer
		if (display.craftingTime == 20) {
			return Text.translatable("container.spectrum.rei.enchanting.crafting_time_one_second", 1);
		} else {
			return Text.translatable("container.spectrum.rei.enchanting.crafting_time", (display.craftingTime / 20));
		}
	}
	
}
