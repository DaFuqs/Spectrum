package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.util.*;
import net.fabricmc.api.*;
import net.minecraft.text.*;

@Environment(EnvType.CLIENT)
public class HeatingCategory extends BlockToBlockWithChanceCategory {

	@Override
	public CategoryIdentifier<? extends HeatingDisplay> getCategoryIdentifier() {
		return SpectrumPlugins.HEATING;
	}

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumBlocks.BLAZE_MOB_BLOCK);
	}

	@Override
	public Text getTitle() {
		return Text.translatable("container.spectrum.rei.heating.title");
	}

}
