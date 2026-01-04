package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.network.chat.*;


public class SludgeConvertingCategory extends FluidConvertingCategory<SludgeConvertingDisplay> {
	
	@Override
	public CategoryIdentifier<? extends SludgeConvertingDisplay> getCategoryIdentifier() {
		return SpectrumPlugins.SLUDGE_CONVERTING;
	}
	
	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumItems.SLUDGE_BUCKET);
	}
	
	@Override
	public Component getTitle() {
		return Component.translatable("container.spectrum.rei.sludge_converting.title");
	}
	
}
