package de.dafuqs.pigment.blocks.conditional;

import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.PigmentBlocks;
import de.dafuqs.pigment.interfaces.Cloakable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;

import java.util.List;

public class ColoredLeavesBlock extends LeavesBlock implements Cloakable {

    private boolean wasLastCloaked;

    public ColoredLeavesBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isCloaked(PlayerEntity playerEntity, BlockState blockState) {
        return playerEntity.getArmor() < 1;
    }

    @Override
    public boolean wasLastCloaked() {
        return wasLastCloaked;
    }

    @Override
    public void setLastCloaked(boolean lastCloaked) {
        wasLastCloaked = lastCloaked;
    }

    @Deprecated
    @Environment(EnvType.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        checkCloak(state);
        return super.isSideInvisible(state, stateFrom, direction);
    }

    public void setCloaked() {
        // Colored Leaves => Oak Leaves
        BlockState cloakDefaultState = Blocks.OAK_LEAVES.getDefaultState();
        for(DyeColor dyeColor : DyeColor.values()) {
            BlockState defaultState = PigmentBlocks.getColoredLeavesBlock(dyeColor).getDefaultState();
            PigmentCommon.getBlockCloaker().swapModel(defaultState, cloakDefaultState);
            PigmentCommon.getBlockCloaker().swapModel(defaultState.with(LeavesBlock.DISTANCE, 1).with(LeavesBlock.PERSISTENT, false), cloakDefaultState.with(LeavesBlock.DISTANCE, 1).with(LeavesBlock.PERSISTENT, false));
            PigmentCommon.getBlockCloaker().swapModel(defaultState.with(LeavesBlock.DISTANCE, 2).with(LeavesBlock.PERSISTENT, false), cloakDefaultState.with(LeavesBlock.DISTANCE, 2).with(LeavesBlock.PERSISTENT, false));
            PigmentCommon.getBlockCloaker().swapModel(defaultState.with(LeavesBlock.DISTANCE, 3).with(LeavesBlock.PERSISTENT, false), cloakDefaultState.with(LeavesBlock.DISTANCE, 3).with(LeavesBlock.PERSISTENT, false));
            PigmentCommon.getBlockCloaker().swapModel(defaultState.with(LeavesBlock.DISTANCE, 4).with(LeavesBlock.PERSISTENT, false), cloakDefaultState.with(LeavesBlock.DISTANCE, 4).with(LeavesBlock.PERSISTENT, false));
            PigmentCommon.getBlockCloaker().swapModel(defaultState.with(LeavesBlock.DISTANCE, 1).with(LeavesBlock.PERSISTENT, true), cloakDefaultState.with(LeavesBlock.DISTANCE, 1).with(LeavesBlock.PERSISTENT, true));
            PigmentCommon.getBlockCloaker().swapModel(defaultState.with(LeavesBlock.DISTANCE, 2).with(LeavesBlock.PERSISTENT, true), cloakDefaultState.with(LeavesBlock.DISTANCE, 2).with(LeavesBlock.PERSISTENT, true));
            PigmentCommon.getBlockCloaker().swapModel(defaultState.with(LeavesBlock.DISTANCE, 3).with(LeavesBlock.PERSISTENT, true), cloakDefaultState.with(LeavesBlock.DISTANCE, 3).with(LeavesBlock.PERSISTENT, true));
            PigmentCommon.getBlockCloaker().swapModel(defaultState.with(LeavesBlock.DISTANCE, 4).with(LeavesBlock.PERSISTENT, true), cloakDefaultState.with(LeavesBlock.DISTANCE, 4).with(LeavesBlock.PERSISTENT, true));
            PigmentCommon.getBlockCloaker().swapModel(PigmentBlocks.getColoredLeavesItem(dyeColor), Items.OAK_LEAVES); // item
        }
    }

    public void setUncloaked() {
        for(DyeColor dyeColor : DyeColor.values()) {
            Block block = PigmentBlocks.getColoredLeavesBlock(dyeColor);
            PigmentCommon.getBlockCloaker().unswapAllBlockStates(block);
            PigmentCommon.getBlockCloaker().unswapModel(PigmentBlocks.getColoredLeavesItem(dyeColor));
        }
    }

    @Deprecated
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        return getCloakedDroppedStacks(state, builder);
    }

}
