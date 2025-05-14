package de.dafuqs.spectrum.data;

import java.util.concurrent.*;

import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.datagen.v1.*;
import net.fabricmc.fabric.api.datagen.v1.provider.*;
import net.minecraft.core.*;

public class SpectrumEnchantmentTagProvider extends FabricTagProvider.EnchantmentTagProvider {
	
	public SpectrumEnchantmentTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
		super(output, completableFuture);
	}
	
	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		SpectrumEnchantments.provideEnchantmentTags(this::getOrCreateTagBuilder);
	}
	
}
