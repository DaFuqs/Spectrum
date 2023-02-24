package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.util.*;
import net.fabricmc.api.*;
import net.minecraft.text.*;

@Environment(EnvType.CLIENT)
public class DragonrotConvertingCategory extends FluidConvertingCategory<DragonrotConvertingDisplay> {

	@Override
	public CategoryIdentifier<? extends FluidConvertingDisplay> getCategoryIdentifier() {
		return SpectrumPlugins.DRAGONROT_CONVERTING;
	}

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumItems.DRAGONROT_BUCKET);
	}

	@Override
	public Text getTitle() {
		return Text.translatable("container.spectrum.rei.dragonrot_converting.title");
	}

}
