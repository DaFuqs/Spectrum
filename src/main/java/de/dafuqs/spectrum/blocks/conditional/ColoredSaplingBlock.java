package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.interfaces.Cloakable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.Hashtable;
import java.util.List;

public class ColoredSaplingBlock extends SaplingBlock implements Cloakable {
	
	public ColoredSaplingBlock(SaplingGenerator generator, Settings settings) {
		super(generator, settings);
		registerCloak();
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return new Identifier(SpectrumCommon.MOD_ID, "milestones/reveal_colored_saplings");
	}
	
	@Override
	public Hashtable<BlockState, BlockState> getBlockStateCloaks() {
		// Colored Logs => Oak logs
		Hashtable<BlockState, BlockState> hashtable = new Hashtable<>();
		for (int stage = 0; stage < 2; stage++) {
			hashtable.put(this.getDefaultState().with(SaplingBlock.STAGE, stage), Blocks.OAK_SAPLING.getDefaultState().with(SaplingBlock.STAGE, stage));
		}
		return hashtable;
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), Blocks.OAK_SAPLING.asItem());
	}
	
	
	@Deprecated
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return getCloakedDroppedStacks(state, builder);
	}
	
}
