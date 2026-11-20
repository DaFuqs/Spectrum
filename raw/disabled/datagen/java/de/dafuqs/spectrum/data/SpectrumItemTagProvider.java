package de.dafuqs.spectrum.data;

import java.util.concurrent.*;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.tags.*;

public class SpectrumItemTagProvider extends FabricTagProvider.ItemTagProvider {
	
	public SpectrumItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookupFuture) {
		super(output, lookupFuture);
	}
	
	@Override
	protected void addTags(HolderLookup.Provider lookup) {
		SpectrumEnchantments.provideItemTags(this::getOrCreateTagBuilder);
		
		getOrCreateTagBuilder(SpectrumItemTags.COOKBOOKS).add(
				SpectrumItems.BREWERS_HANDBOOK,
				SpectrumItems.IMBRIFER_COOKBOOK,
				SpectrumItems.IMPERIAL_COOKBOOK,
				SpectrumItems.MELOCHITES_COOKBOOK_VOL_1,
				SpectrumItems.MELOCHITES_COOKBOOK_VOL_2,
				SpectrumItems.POISONERS_HANDBOOK);
		
		getOrCreateTagBuilder(ItemTags.BOOKSHELF_BOOKS)
				.addTag(SpectrumItemTags.COOKBOOKS)
				.add(SpectrumItems.GILDED_BOOK, SpectrumItems.GUIDEBOOK);
	}
	
}
