package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.decoration.CloverBlock;
import de.dafuqs.spectrum.interfaces.Cloakable;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.Hashtable;
import java.util.List;

public class FourLeafCloverBlock extends CloverBlock implements Cloakable {
	
	public FourLeafCloverBlock(Settings settings) {
		super(settings);
		registerCloak();
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return new Identifier(SpectrumCommon.MOD_ID, "milestones/reveal_four_leaf_clover");
	}
	
	@Override
	public Hashtable<BlockState, BlockState> getBlockStateCloaks() {
		Hashtable<BlockState, BlockState> hashtable = new Hashtable<>();
		hashtable.put(this.getDefaultState(), SpectrumBlocks.CLOVER.getDefaultState());
		return hashtable;
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), SpectrumBlocks.CLOVER.asItem());
	}
	
	@Deprecated
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return getCloakedDroppedStacks(state, builder);
	}
	
}
