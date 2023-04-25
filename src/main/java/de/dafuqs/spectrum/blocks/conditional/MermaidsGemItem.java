package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

import java.util.*;

public class MermaidsGemItem extends BlockItem implements RevelationAware {
	
	public MermaidsGemItem(Settings settings) {
		super(SpectrumBlocks.MERMAIDS_BRUSH, settings);
		RevelationAware.register(this);
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return MermaidsBrushBlock.UNLOCK_IDENTIFIER;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		return Map.of();
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this, Items.LIGHT_BLUE_DYE);
	}
	
}
