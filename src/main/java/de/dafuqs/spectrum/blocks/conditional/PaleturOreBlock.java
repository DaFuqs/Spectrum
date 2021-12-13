package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import java.util.Hashtable;

public class PaleturOreBlock extends CloakedOreBlock {

	public PaleturOreBlock(Settings settings, UniformIntProvider uniformIntProvider) {
		super(settings, uniformIntProvider, false);
		registerCloak();
	}

	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return new Identifier(SpectrumCommon.MOD_ID, "milestones/reveal_paletur");
	}

	@Override
	public Hashtable<BlockState, BlockState> getBlockStateCloaks() {
		Hashtable<BlockState, BlockState> hashtable = new Hashtable<>();
		hashtable.put(this.getDefaultState(), Blocks.END_STONE.getDefaultState());
		return hashtable;
	}

	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), Blocks.END_STONE.asItem());
	}

}
