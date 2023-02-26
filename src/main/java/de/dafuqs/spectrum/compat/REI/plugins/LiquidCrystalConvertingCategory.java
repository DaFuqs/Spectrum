package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.util.*;
import net.fabricmc.api.*;
import net.minecraft.text.*;

@Environment(EnvType.CLIENT)
public class LiquidCrystalConvertingCategory extends FluidConvertingCategory<LiquidCrystalConvertingDisplay> {

	@Override
	public CategoryIdentifier<? extends LiquidCrystalConvertingDisplay> getCategoryIdentifier() {
		return SpectrumPlugins.LIQUID_CRYSTAL_CONVERTING;
	}

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumItems.LIQUID_CRYSTAL_BUCKET);
	}

	@Override
	public Text getTitle() {
		return Text.translatable("container.spectrum.rei.liquid_crystal_converting.title");
	}

}
