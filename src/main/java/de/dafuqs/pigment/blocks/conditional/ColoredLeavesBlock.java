package de.dafuqs.pigment.blocks.conditional;

import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.interfaces.Cloakable;
import de.dafuqs.pigment.misc.PigmentBlockCloaker;
import de.dafuqs.pigment.registries.PigmentBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.List;

public class ColoredLeavesBlock extends LeavesBlock implements Cloakable {

    public ColoredLeavesBlock(Settings settings) {
        super(settings);
        setupCloak();
    }

    @Override
    public Identifier getCloakAdvancementIdentifier() {
        return new Identifier(PigmentCommon.MOD_ID, "craft_colored_sapling");
    }

    @Deprecated
    @Environment(EnvType.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        //checkCloak(state);
        return super.isSideInvisible(state, stateFrom, direction);
    }

    public void setCloaked() {
        // Colored Leaves => Oak Leaves
        BlockState cloakDefaultState = Blocks.OAK_LEAVES.getDefaultState();
        for(DyeColor dyeColor : DyeColor.values()) {
            BlockState defaultState = PigmentBlocks.getColoredLeavesBlock(dyeColor).getDefaultState();
            PigmentBlockCloaker.swapModel(defaultState, cloakDefaultState);
            PigmentBlockCloaker.swapModel(defaultState.with(LeavesBlock.DISTANCE, 1).with(LeavesBlock.PERSISTENT, false), cloakDefaultState.with(LeavesBlock.DISTANCE, 1).with(LeavesBlock.PERSISTENT, false));
            PigmentBlockCloaker.swapModel(defaultState.with(LeavesBlock.DISTANCE, 2).with(LeavesBlock.PERSISTENT, false), cloakDefaultState.with(LeavesBlock.DISTANCE, 2).with(LeavesBlock.PERSISTENT, false));
            PigmentBlockCloaker.swapModel(defaultState.with(LeavesBlock.DISTANCE, 3).with(LeavesBlock.PERSISTENT, false), cloakDefaultState.with(LeavesBlock.DISTANCE, 3).with(LeavesBlock.PERSISTENT, false));
            PigmentBlockCloaker.swapModel(defaultState.with(LeavesBlock.DISTANCE, 4).with(LeavesBlock.PERSISTENT, false), cloakDefaultState.with(LeavesBlock.DISTANCE, 4).with(LeavesBlock.PERSISTENT, false));
            PigmentBlockCloaker.swapModel(defaultState.with(LeavesBlock.DISTANCE, 1).with(LeavesBlock.PERSISTENT, true), cloakDefaultState.with(LeavesBlock.DISTANCE, 1).with(LeavesBlock.PERSISTENT, true));
            PigmentBlockCloaker.swapModel(defaultState.with(LeavesBlock.DISTANCE, 2).with(LeavesBlock.PERSISTENT, true), cloakDefaultState.with(LeavesBlock.DISTANCE, 2).with(LeavesBlock.PERSISTENT, true));
            PigmentBlockCloaker.swapModel(defaultState.with(LeavesBlock.DISTANCE, 3).with(LeavesBlock.PERSISTENT, true), cloakDefaultState.with(LeavesBlock.DISTANCE, 3).with(LeavesBlock.PERSISTENT, true));
            PigmentBlockCloaker.swapModel(defaultState.with(LeavesBlock.DISTANCE, 4).with(LeavesBlock.PERSISTENT, true), cloakDefaultState.with(LeavesBlock.DISTANCE, 4).with(LeavesBlock.PERSISTENT, true));
            PigmentBlockCloaker.swapModel(PigmentBlocks.getColoredLeavesItem(dyeColor), Items.OAK_LEAVES); // item
        }
    }

    public void setUncloaked() {
        for(DyeColor dyeColor : DyeColor.values()) {
            Block block = PigmentBlocks.getColoredLeavesBlock(dyeColor);
            PigmentBlockCloaker.unswapAllBlockStates(block);
            PigmentBlockCloaker.unswapModel(PigmentBlocks.getColoredLeavesItem(dyeColor));
        }
    }

    @Deprecated
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        return getCloakedDroppedStacks(state, builder);
    }

}
