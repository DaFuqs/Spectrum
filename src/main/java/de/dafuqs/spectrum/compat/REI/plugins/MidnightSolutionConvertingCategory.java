package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.util.*;
import net.fabricmc.api.*;
import net.minecraft.text.*;

@Environment(EnvType.CLIENT)
public class MidnightSolutionConvertingCategory extends FluidConvertingCategory<MidnightSolutionConvertingDisplay> {

	@Override
	public CategoryIdentifier<? extends MidnightSolutionConvertingDisplay> getCategoryIdentifier() {
		return SpectrumPlugins.MIDNIGHT_SOLUTION_CONVERTING;
	}

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumItems.MIDNIGHT_SOLUTION_BUCKET);
	}

	@Override
	public Text getTitle() {
		return Text.translatable("container.spectrum.rei.midnight_solution_converting.title");
	}

}
