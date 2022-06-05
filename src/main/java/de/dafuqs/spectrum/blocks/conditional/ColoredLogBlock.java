package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.interfaces.Cloakable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;

import java.util.Hashtable;
import java.util.List;

public class ColoredLogBlock extends PillarBlock implements Cloakable {
	
	public ColoredLogBlock(Settings settings) {
		super(settings);
		registerCloak();
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return new Identifier(SpectrumCommon.MOD_ID, "milestones/reveal_colored_trees");
	}
	
	@Override
	public Hashtable<BlockState, BlockState> getBlockStateCloaks() {
		Hashtable<BlockState, BlockState> hashtable = new Hashtable<>();
		for (Direction.Axis axis : PillarBlock.AXIS.getValues()) {
			hashtable.put(this.getDefaultState().with(PillarBlock.AXIS, axis), Blocks.OAK_LOG.getDefaultState().with(PillarBlock.AXIS, axis));
		}
		return hashtable;
	}
	
	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), Blocks.OAK_LOG.asItem());
	}
	
	@Deprecated
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return getCloakedDroppedStacks(state, builder);
	}
	
}
