package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.fabricmc.api.*;
import net.minecraft.text.*;

@Environment(EnvType.CLIENT)
public class PotionWorkshopReactingCategory extends GatedItemInformationPageCategory {

	public static final EntryStack POTION_WORKSHOP_ENTRY = EntryStacks.of(SpectrumBlocks.POTION_WORKSHOP);

	@Override
	public Renderer getIcon() {
		return POTION_WORKSHOP_ENTRY;
	}

	@Override
	public Text getTitle() {
		return Text.translatable("container.spectrum.rei.potion_workshop_reacting.title");
	}

	@Override
	public CategoryIdentifier getCategoryIdentifier() {
		return SpectrumPlugins.POTION_WORKSHOP_REACTING;
	}

	@Override
	public EntryStack getBackgroundEntryStack() {
		return POTION_WORKSHOP_ENTRY;
	}

}
