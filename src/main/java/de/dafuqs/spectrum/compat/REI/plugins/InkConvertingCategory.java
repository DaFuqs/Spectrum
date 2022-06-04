package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class InkConvertingCategory implements DisplayCategory<InkConvertingDisplay> {
	
	@Override
	public CategoryIdentifier<? extends InkConvertingDisplay> getCategoryIdentifier() {
		return SpectrumPlugins.INK_CONVERTING;
	}

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumBlocks.COLOR_PICKER);
	}

	@Override
	public Text getTitle() {
		return new TranslatableText("container.spectrum.rei.ink_converting.title");
	}

}
