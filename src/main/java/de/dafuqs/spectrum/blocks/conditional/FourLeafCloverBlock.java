package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.revelationary.api.revelations.RevelationAware;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.decoration.CloverBlock;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.Hashtable;
import java.util.Map;

public class FourLeafCloverBlock extends CloverBlock implements RevelationAware {
	
	public FourLeafCloverBlock(Settings settings) {
		super(settings);
		RevelationAware.register(this);
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return new Identifier(SpectrumCommon.MOD_ID, "milestones/reveal_four_leaf_clover");
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
