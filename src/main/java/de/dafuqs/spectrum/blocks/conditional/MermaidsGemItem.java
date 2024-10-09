package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

import java.util.*;

public class MermaidsGemItem extends AliasedBlockItem implements RevelationAware {
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("place_pedestal");
	
	public MermaidsGemItem(Block block, Settings settings) {
		super(block, settings);
		RevelationAware.register(this);
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		return Map.of();
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this, Items.KELP);
	}
	
}
