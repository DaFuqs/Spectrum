package de.dafuqs.spectrum.progression;

import de.dafuqs.spectrum.interfaces.Cloakable;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.*;

public class BlockCloakManager {
	
	private static final HashMap<Identifier, List<Cloakable>> advancementBlockSwapTriggers = new HashMap<>();
	private static final HashMap<BlockState, BlockState> blockStateSwaps = new HashMap<>();
	private static final HashMap<Item, Item> itemSwaps = new HashMap<>();
	
	// BLOCKS
	public static void registerBlockStateCloaks(Hashtable<BlockState, BlockState> blockStates) {
		blockStateSwaps.putAll(blockStates);
	}
	
	public static BlockState getBlockStateCloak(BlockState blockState) {
		return blockStateSwaps.getOrDefault(blockState, blockState);
	}
	
	// ITEMS
	public static void registerItemCloak(Item sourceItem, Item targetItem) {
		itemSwaps.put(sourceItem, targetItem);
	}
	
	public static Item getItemCloak(Item item) {
		return itemSwaps.getOrDefault(item, item);
	}
	
	public static void registerAdvancementCloak(Cloakable cloakableBlock, Identifier advancementIdentifier) {
		if (advancementBlockSwapTriggers.containsKey(advancementIdentifier)) {
			if (!advancementBlockSwapTriggers.get(advancementIdentifier).contains(cloakableBlock)) {
				advancementBlockSwapTriggers.get(advancementIdentifier).add(cloakableBlock);
			}
		} else {
			List<Cloakable> blocks = new ArrayList<>();
			blocks.add(cloakableBlock);
			advancementBlockSwapTriggers.put(advancementIdentifier, blocks);
		}
	}
	
	public static boolean doesAdvancementTriggerRevelation(Identifier advancementIdentifier) {
		return advancementBlockSwapTriggers.containsKey(advancementIdentifier);
	}
	
	public static List<Cloakable> getRevelationsForAdvancement(Identifier advancementIdentifier) {
		if (advancementBlockSwapTriggers.containsKey(advancementIdentifier)) {
			return advancementBlockSwapTriggers.get(advancementIdentifier);
		} else {
			return new ArrayList<>();
		}
	}
	
	public static List<Cloakable> getBlocksToUncloak(Identifier advancementIdentifier) {
		if (advancementBlockSwapTriggers.containsKey(advancementIdentifier)) {
			return advancementBlockSwapTriggers.get(advancementIdentifier);
		} else {
			return new ArrayList<>();
		}
	}
	
	public static HashMap<Identifier, List<Cloakable>> getAdvancementIdentifiersAndRegisteredCloaks() {
		return advancementBlockSwapTriggers;
	}
	
	public static void setupCloaks() {
		Collection<List<Cloakable>> registeredCloakables = BlockCloakManager.getAdvancementIdentifiersAndRegisteredCloaks().values();
		for (List<Cloakable> cloakablesList : registeredCloakables) {
			for (Cloakable cloakable : cloakablesList) {
				BlockCloakManager.registerBlockStateCloaks(cloakable.getBlockStateCloaks());
				Pair<Item, Item> itemCloak = cloakable.getItemCloak();
				if (itemCloak != null) {
					BlockCloakManager.registerItemCloak(itemCloak.getLeft(), itemCloak.getRight());
				}
			}
		}
	}
	
}
