package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.REI.*;
import me.shedaniel.rei.api.common.category.*;
import net.fabricmc.api.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

@Environment(EnvType.CLIENT)
public class PotionWorkshopBrewingCategory extends PotionWorkshopCategory {

	@Override
	public CategoryIdentifier getCategoryIdentifier() {
		return SpectrumPlugins.POTION_WORKSHOP_BREWING;
	}

	@Override
	public Identifier getIdentifier() {
		return SpectrumCommon.locate("potion_workshop_brewing");
	}

	@Override
	public Text getTitle() {
		return Text.translatable("container.spectrum.rei.potion_workshop_brewing.title");
	}
	
}
