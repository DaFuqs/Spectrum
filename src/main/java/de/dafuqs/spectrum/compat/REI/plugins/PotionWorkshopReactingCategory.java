package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class PotionWorkshopReactingCategory extends GatedItemInformationPageCategory {
	
	public static final EntryStack POTION_WORKSHOP_ENTRY = EntryStacks.of(SpectrumBlocks.POTION_WORKSHOP);
	
	@Override
	public Renderer getIcon() {
		return POTION_WORKSHOP_ENTRY;
	}
	
	@Override
	public Text getTitle() {
		return new TranslatableText("container.spectrum.rei.potion_workshop_reacting.title");
	}
	
	@Override
	public CategoryIdentifier getCategoryIdentifier() {
		return SpectrumPlugins.POTION_WORKSHOP_REACTING;
	}
	
	@Override
	public EntryStack getBackgroundEntryStack() {
		return POTION_WORKSHOP_ENTRY;
	}
	
}
