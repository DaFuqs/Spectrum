package de.dafuqs.spectrum.progression.revelationary;

import de.dafuqs.spectrum.interfaces.RevelationAware;
import de.dafuqs.spectrum.interfaces.WorldRendererAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Environment(EnvType.CLIENT)
public class RevelationHolder {
	
	public static List<UncloakCallback> callbacks = new ArrayList<>();
	
	public interface UncloakCallback {
		void trigger(List<Identifier> advancements, List<Block> blocks, List<Item> items);
	}
	
	private static final List<BlockState> activeBlockStateSwaps = new ArrayList<>();
	private static final List<Item> activeItemSwaps = new ArrayList<>();
	
	public static void processNewAdvancements(List<Identifier> doneAdvancements, boolean triggerCallback) {
		if(!doneAdvancements.isEmpty()) {
			List<Item> revealedItems = new ArrayList<>();
			List<BlockState> revealedBlockStates = new ArrayList<>();
			List<Block> revealedBlocks = new ArrayList<>();
			for (Identifier doneAdvancement : doneAdvancements) {
				revealedItems.addAll(RevelationRegistry.getRevealedItems(doneAdvancement));
				revealedBlockStates.addAll(RevelationRegistry.getRevealedBlockStates(doneAdvancement));
				for (BlockState state : revealedBlockStates) {
					Block block = state.getBlock();
					if (!revealedBlocks.contains(block)) {
						revealedBlocks.add(block);
					}
				}
			}
			
			if (revealedBlockStates.size() > 0) {
				// uncloak the blocks
				for (BlockState revealedBlockState : revealedBlockStates) {
					activeBlockStateSwaps.remove(revealedBlockState);
					Item blockItem = revealedBlockState.getBlock().asItem();
					if (blockItem != null) {
						activeItemSwaps.remove(blockItem);
					}
				}
				rebuildAllChunks();
			}
			
			for (Block revealedBlock : revealedBlocks) {
				if (revealedBlock instanceof RevelationAware revelationAware) {
					revelationAware.onUncloak();
				}
			}
			for (Item revealedItem : revealedItems) {
				if (revealedItem instanceof RevelationAware revelationAware) {
					revelationAware.onUncloak();
				}
			}
			
			if (triggerCallback && (!revealedBlocks.isEmpty() || !revealedItems.isEmpty())) {
				for (UncloakCallback callback : callbacks) {
					callback.trigger(doneAdvancements, revealedBlocks, revealedItems);
				}
			}
		}
	}
	
	public static void processRemovedAdvancements(@NotNull Set<Identifier> removedAdvancements) {
		if(!removedAdvancements.isEmpty()) {
			List<Item> concealedItems = new ArrayList<>();
			List<BlockState> concealedBlockStates = new ArrayList<>();
			List<Block> concealedBlocks = new ArrayList<>();
			for (Identifier removedAdvancement : removedAdvancements) {
				concealedItems.addAll(RevelationRegistry.getRevealedItems(removedAdvancement));
				concealedBlockStates.addAll(RevelationRegistry.getRevealedBlockStates(removedAdvancement));
				for (BlockState state : concealedBlockStates) {
					Block block = state.getBlock();
					if (!concealedBlocks.contains(block)) {
						concealedBlocks.add(block);
					}
				}
			}
			
			if (concealedBlockStates.size() > 0) {
				// uncloak the blocks
				for (BlockState concealedBlockState : concealedBlockStates) {
					if(!activeBlockStateSwaps.contains(concealedBlockState)) {
						activeBlockStateSwaps.add(concealedBlockState);
					}
					Item blockItem = concealedBlockState.getBlock().asItem();
					if (blockItem != null) {
						if(!activeItemSwaps.contains(blockItem)) {
							activeItemSwaps.add(blockItem);
						}
					}
				}
				rebuildAllChunks();
			}
			
			for (Block concealedBlock : concealedBlocks) {
				if (concealedBlock instanceof RevelationAware revelationAware) {
					revelationAware.onCloak();
				}
			}
			for (Item concealedItem : concealedItems) {
				if (concealedItem instanceof RevelationAware revelationAware) {
					revelationAware.onCloak();
				}
			}
		}
	}
	
	public static void registerRevelationCallback(UncloakCallback callback) {
		callbacks.add(callback);
	}
	
	// rerender chunks to show newly swapped blocks
	static void rebuildAllChunks() {
		WorldRenderer renderer = MinecraftClient.getInstance().worldRenderer;
		((WorldRendererAccessor) renderer).rebuildAllChunks();
	}
	
	private static void cloak(BlockState blockState) {
		activeBlockStateSwaps.add(blockState);
		if(blockState instanceof RevelationAware revelationAware) {
			revelationAware.onCloak();
		}
	}
	
	private static void cloak(Item item) {
		activeItemSwaps.add(item);
		if(item instanceof RevelationAware revelationAware) {
			revelationAware.onCloak();
		}
	}
	
	// BLOCKS
	public static boolean isCloaked(Block block) {
		return activeBlockStateSwaps.contains(block.getDefaultState());
	}
	
	// BLOCKS
	public static boolean isCloaked(BlockState blockState) {
		return activeBlockStateSwaps.contains(blockState);
	}
	
	public static BlockState getCloakTarget(BlockState blockState) {
		if (isCloaked(blockState)) {
			return RevelationRegistry.getCloak(blockState);
		} else {
			return blockState;
		}
	}
	
	// ITEMS
	public static boolean isCloaked(Item item) {
		return activeItemSwaps.contains(item);
	}
	
	public static Item getCloakTarget(Item item) {
		if (isCloaked(item)) {
			return RevelationRegistry.getCloak(item);
		} else {
			return item;
		}
	}
	
	public static void cloakAll() {
		activeItemSwaps.clear();
		activeBlockStateSwaps.clear();
		
		for (List<BlockState> registeredRevelations : RevelationRegistry.getBlockStateEntries().values()) {
			for (BlockState registeredRevelation : registeredRevelations) {
				cloak(registeredRevelation);
			}
		}
		for (List<Item> registeredRevelations : RevelationRegistry.getItemEntries().values()) {
			for (Item registeredRevelation : registeredRevelations) {
				cloak(registeredRevelation);
			}
		}
	}
	
}
