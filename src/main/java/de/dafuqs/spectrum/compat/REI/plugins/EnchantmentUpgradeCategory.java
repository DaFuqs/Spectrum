package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import me.shedaniel.rei.api.common.category.*;
import net.fabricmc.api.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;

@Environment(EnvType.CLIENT)
public class EnchantmentUpgradeCategory extends EnchanterCategory<EnchantmentUpgradeDisplay> {
	
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
		return display.requiredItemCount;
	}
	
	@Override
	public Text getDescriptionText(@NotNull EnchantmentUpgradeDisplay display) {
		if (display.requiredItemCount == 0) {
			return Text.empty();
		}
		return Text.translatable("container.spectrum.rei.enchantment_upgrade.required_item_count", display.requiredItemCount);
	}
	
}
