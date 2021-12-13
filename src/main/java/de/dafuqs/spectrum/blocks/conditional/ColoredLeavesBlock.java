package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.SpectrumClient;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.interfaces.Cloakable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.Hashtable;
import java.util.List;

public class ColoredLeavesBlock extends LeavesBlock implements Cloakable {

	public ColoredLeavesBlock(Settings settings) {
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
		for(int distance = 1; distance < 8; distance++) {
			hashtable.put(this.getDefaultState().with(LeavesBlock.DISTANCE, distance).with(LeavesBlock.PERSISTENT, false), Blocks.OAK_LEAVES.getDefaultState().with(LeavesBlock.DISTANCE, distance).with(LeavesBlock.PERSISTENT, false));
		}
		return hashtable;
	}

	@Override
	public Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), Blocks.OAK_LEAVES.asItem());
	}

	@Override
	public void onUncloak() {
		SpectrumClient.coloredLeavesBlockColorProvider.setShouldApply(false);
		SpectrumClient.coloredLeavesItemColorProvider.setShouldApply(false);
	}

	@Override
	public void onCloak() {
		SpectrumClient.coloredLeavesBlockColorProvider.setShouldApply(true);
		SpectrumClient.coloredLeavesItemColorProvider.setShouldApply(true);
	}

	@Deprecated
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return getCloakedDroppedStacks(state, builder);
	}

}
