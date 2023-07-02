package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.util.*;
import net.fabricmc.api.*;
import net.minecraft.text.*;

@Environment(EnvType.CLIENT)
public class MudConvertingCategory extends FluidConvertingCategory<MudConvertingDisplay> {
	
	@Override
	public CategoryIdentifier<? extends MudConvertingDisplay> getCategoryIdentifier() {
		return SpectrumPlugins.MUD_CONVERTING;
	}
	
	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumItems.MUD_BUCKET);
	}
	
	@Override
	public Text getTitle() {
		return Text.translatable("container.spectrum.rei.mud_converting.title");
	}
	
}
