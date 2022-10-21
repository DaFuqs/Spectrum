package de.dafuqs.spectrum.blocks.conditional.amaranth;

import de.dafuqs.revelationary.api.revelations.RevelationAware;
import de.dafuqs.spectrum.registries.client.SpectrumColorProviders;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PlantBlock;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.Hashtable;
import java.util.Map;

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