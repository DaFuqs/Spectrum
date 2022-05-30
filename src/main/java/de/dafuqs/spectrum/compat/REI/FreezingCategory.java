package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.registries.SpectrumBlocks;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class FreezingCategory extends BlockToBlockWithChanceCategory {
	
	@Override
	public CategoryIdentifier<? extends FreezingDisplay> getCategoryIdentifier() {
		return SpectrumPlugins.FREEZING;
	}

	@Override
	public Renderer getIcon() {
		return EntryStacks.of(SpectrumBlocks.POLAR_BEAR_MOB_BLOCK);
	}

	@Override
	public Text getTitle() {
		return new TranslatableText("container.spectrum.rei.freezing.title");
	}

}
