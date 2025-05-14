package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.rei.api.client.gui.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.util.*;
import net.fabricmc.api.*;
import net.minecraft.network.chat.*;

@Environment(EnvType.CLIENT)
public class FreezingCategory extends BlockToBlockWithChanceCategory {
	
	@Override
	public CategoryIdentifier<? extends FreezingDisplay> getCategoryIdentifier() {
		return SpectrumPlugins.FREEZING;
	}
	
	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumBlocks.POLAR_BEAR_IDOL);
	}
	
	@Override
	public Component getTitle() {
		return Component.translatable("container.spectrum.rei.freezing.title");
	}
	
}
