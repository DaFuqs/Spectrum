package de.dafuqs.spectrum.data;

import net.fabricmc.fabric.api.datagen.v1.*;
import net.fabricmc.fabric.api.datagen.v1.provider.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.data.models.*;

public class SpectrumModelProvider extends FabricModelProvider {
	
	public SpectrumModelProvider(FabricDataOutput output) {
		super(output);
	}
	
	@Override
	public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
		SpectrumModelHelper.BLOCK_STATE_MODEL_REGISTRAR.flush(blockStateModelGenerator);
	}
	
	@Override
	public void generateItemModels(ItemModelGenerators itemModelGenerator) {
		SpectrumModelHelper.ITEM_MODEL_REGISTRAR.flush(itemModelGenerator);
	}
}