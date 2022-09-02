package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class PotionWorkshopCraftingCategory extends PotionWorkshopCategory {
	
	@Override
	public CategoryIdentifier getCategoryIdentifier() {
		return SpectrumPlugins.POTION_WORKSHOP_CRAFTING;
	}
	
	@Override
	public Identifier getIdentifier() {
		return SpectrumCommon.locate("potion_workshop_crafting");
	}
	
	@Override
	public Text getTitle() {
		return Text.translatable("container.spectrum.rei.potion_workshop_crafting.title");
	}
	
}
