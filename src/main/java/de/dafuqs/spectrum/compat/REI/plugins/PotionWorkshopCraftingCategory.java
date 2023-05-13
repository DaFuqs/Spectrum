package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.REI.*;
import me.shedaniel.rei.api.common.category.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

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
