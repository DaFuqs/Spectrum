package de.dafuqs.spectrum.blocks.conditional.amaranth;

import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.registries.client.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AmaranthBushelBlock extends PlantBlock implements RevelationAware {
	
	public AmaranthBushelBlock(Settings settings) {
		super(settings);
		RevelationAware.register(this);
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return AmaranthCropBlock.ADVANCEMENT_IDENTIFIER;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Map<BlockState, BlockState> map = new Hashtable<>();
		map.put(this.getDefaultState(), Blocks.FERN.getDefaultState());
		return map;
	}
	
	@Override
	public @Nullable Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), Blocks.FERN.asItem());
	}
	
	@Override
	public void onUncloak() {
		if (SpectrumColorProviders.amaranthBushelBlockColorProvider != null && SpectrumColorProviders.amaranthBushelItemColorProvider != null) {
			SpectrumColorProviders.amaranthBushelBlockColorProvider.setShouldApply(false);
			SpectrumColorProviders.amaranthBushelItemColorProvider.setShouldApply(false);
		}
	}
	
	@Override
	public void onCloak() {
		if (SpectrumColorProviders.amaranthBushelBlockColorProvider != null && SpectrumColorProviders.amaranthBushelItemColorProvider != null) {
			SpectrumColorProviders.amaranthBushelBlockColorProvider.setShouldApply(true);
			SpectrumColorProviders.amaranthBushelItemColorProvider.setShouldApply(true);
		}
	}
	
}