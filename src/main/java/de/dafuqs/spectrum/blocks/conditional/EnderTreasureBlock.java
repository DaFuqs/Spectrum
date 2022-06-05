package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.interfaces.Cloakable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.Hashtable;
import java.util.List;

public class EnderTreasureBlock extends Block implements Cloakable {
	
	public EnderTreasureBlock(Settings settings) {
		super(settings);
		registerCloak();
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return new Identifier(SpectrumCommon.MOD_ID, "milestones/reveal_ender_treasure");
	}
	
	@Override
	public Hashtable<BlockState, BlockState> getBlockStateCloaks() {
		Hashtable<BlockState, BlockState> hashtable = new Hashtable<>();
		hashtable.put(this.getDefaultState(), Blocks.COBBLESTONE.getDefaultState());
		return hashtable;
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), Blocks.COBBLESTONE.asItem());
	}
	
	
	@Deprecated
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return getCloakedDroppedStacks(state, builder);
	}
	
}
