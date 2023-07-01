package de.dafuqs.spectrum.blocks.dd_deco;

import de.dafuqs.spectrum.blocks.*;
import net.minecraft.block.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class StrippingLootPillarBlock extends PillarBlock implements StrippableDrop {
    
    private final Block sourceBlock;
    private final Identifier strippingLootTableIdentifier;
    
    public StrippingLootPillarBlock(Settings settings, Block sourceBlock, Identifier strippingLootTableIdentifier) {
        super(settings);
        this.sourceBlock = sourceBlock;
        this.strippingLootTableIdentifier = strippingLootTableIdentifier;
    }
    
    @Override
    public Block getStrippedBlock() {
        return sourceBlock;
    }
    
    @Override
    public Identifier getStrippingLootTableIdentifier() {
        return strippingLootTableIdentifier;
    }
    
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        checkAndDropStrippedLoot(state, world, pos, newState, moved);
        super.onStateReplaced(state, world, pos, newState, moved);
    }
    
}
