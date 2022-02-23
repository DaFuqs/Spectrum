package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.SpectrumCommon;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class PotionWorkshopCraftingCategory extends PotionWorkshopCategory {
	
	@Override
	public CategoryIdentifier getCategoryIdentifier() {
		return SpectrumPlugins.POTION_WORKSHOP_CRAFTING;
	}

	@Override
	public Identifier getIdentifier() {
		return new Identifier(SpectrumCommon.MOD_ID, "potion_workshop_crafting");
	}

	@Override
	public Text getTitle() {
		return new TranslatableText("container.spectrum.rei.potion_workshop_crafting.title");
	}

}
