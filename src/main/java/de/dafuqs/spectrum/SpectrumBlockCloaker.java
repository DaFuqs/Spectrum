package de.dafuqs.spectrum;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;

import java.util.HashMap;

public class SpectrumBlockCloaker {

    private HashMap<BlockState, BlockState> blockSwaps = new HashMap<>();
    private HashMap<Item, Item> itemSwaps = new HashMap<>();

    // BLOCKS
    public void swapModel(BlockState sourceBlockState, BlockState targetBlockState) {
        this.blockSwaps.put(sourceBlockState, targetBlockState);
    }

    public void unswapModel(BlockState blockState) {
        this.blockSwaps.remove(blockState);
    }

    public void unswapAllBlockStates(Block block) {
        HashMap<BlockState, BlockState> modelSwapperCopy = new HashMap<>();
        for(BlockState blockState : blockSwaps.keySet()) {
            if(blockState.getBlock().equals(block)) {
                modelSwapperCopy.remove(blockState);
            }
        }
        blockSwaps = modelSwapperCopy;
    }

    public boolean isSwapped(BlockState blockState) {
        return blockSwaps.containsKey(blockState);
    }

    public BlockState getTarget(BlockState blockState) {
        return this.blockSwaps.get(blockState);
    }
    
    // ITEMS
    public void swapModel(Item sourceItem, Item targetItem) {
        this.itemSwaps.put(sourceItem, targetItem);
    }

    public void unswapModel(Item item) {
        this.itemSwaps.remove(item);
    }

    public boolean isSwapped(Item item) {
        return itemSwaps.containsKey(item);
    }

    public Item getTarget(Item item) {
        return this.itemSwaps.get(item);
    }


}
