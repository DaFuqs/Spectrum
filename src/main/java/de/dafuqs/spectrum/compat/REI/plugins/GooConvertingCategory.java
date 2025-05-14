package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.util.*;
import net.fabricmc.api.*;
import net.minecraft.network.chat.*;

@Environment(EnvType.CLIENT)
public class GooConvertingCategory extends FluidConvertingCategory<GooConvertingDisplay> {
	
	@Override
	public CategoryIdentifier<? extends GooConvertingDisplay> getCategoryIdentifier() {
		return SpectrumPlugins.GOO_CONVERTING;
	}
	
	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumItems.GOO_BUCKET);
	}
	
	@Override
	public Component getTitle() {
		return Component.translatable("container.spectrum.rei.goo_converting.title");
	}
	
}
