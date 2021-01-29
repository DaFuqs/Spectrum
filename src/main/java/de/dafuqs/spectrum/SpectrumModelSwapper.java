package de.dafuqs.spectrum;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.HashMap;

public class SpectrumModelSwapper {

    private HashMap<BlockState, BlockState> modelSwapper = new HashMap<>();

    public void swapModel(BlockState sourceBlockState, BlockState targetBlockState) {
        this.modelSwapper.put(sourceBlockState, targetBlockState);
    }

    public void unswapModel(BlockState blockState) {
        this.modelSwapper.remove(blockState);
    }

    public void unswapAllBlockStates(Block block) {
        HashMap<BlockState, BlockState> modelSwapperCopy = new HashMap<>();
        for(BlockState blockState : modelSwapper.keySet()) {
            if(blockState.getBlock().equals(block)) {
                modelSwapperCopy.remove(blockState);
            }
        }
        modelSwapper = modelSwapperCopy;
    }

    public boolean isSwapped(BlockState identifier) {
        return modelSwapper.containsKey(identifier);
    }

    public BlockState getTarget(BlockState identifier) {
        return this.modelSwapper.get(identifier);
    }

}
