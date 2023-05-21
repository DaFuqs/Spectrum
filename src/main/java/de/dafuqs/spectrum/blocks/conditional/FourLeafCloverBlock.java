package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.decoration.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

import java.util.*;

public class FourLeafCloverBlock extends CloverBlock implements RevelationAware {
	
	public FourLeafCloverBlock(Settings settings) {
		super(settings);
		RevelationAware.register(this);
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return SpectrumCommon.locate("milestones/reveal_four_leaf_clover");
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Hashtable<BlockState, BlockState> hashtable = new Hashtable<>();
		hashtable.put(this.getDefaultState(), SpectrumBlocks.CLOVER.getDefaultState());
		return hashtable;
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), SpectrumBlocks.CLOVER.asItem());
	}
	
}
