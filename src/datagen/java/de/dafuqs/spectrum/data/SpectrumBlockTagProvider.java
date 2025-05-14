package de.dafuqs.spectrum.data;

import java.util.concurrent.*;

import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.datagen.v1.*;
import net.fabricmc.fabric.api.datagen.v1.provider.*;
import net.minecraft.core.*;

public class SpectrumBlockTagProvider extends FabricTagProvider.BlockTagProvider {
	
	public SpectrumBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}
	
	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		SpectrumBlockTags.provideTags(this::getOrCreateTagBuilder);
	}
	
}