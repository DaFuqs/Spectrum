package de.dafuqs.spectrum.blocks.types.conditional;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.SpectrumBlocks;
import de.dafuqs.spectrum.interfaces.Cloakable;
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
            BlockState defaultState = SpectrumBlocks.getColoredLeavesBlock(dyeColor).getDefaultState();
            SpectrumCommon.getModelSwapper().swapModel(defaultState, cloakDefaultState);
            SpectrumCommon.getModelSwapper().swapModel(defaultState.with(LeavesBlock.DISTANCE, 1).with(LeavesBlock.PERSISTENT, false), cloakDefaultState.with(LeavesBlock.DISTANCE, 1).with(LeavesBlock.PERSISTENT, false));
            SpectrumCommon.getModelSwapper().swapModel(defaultState.with(LeavesBlock.DISTANCE, 2).with(LeavesBlock.PERSISTENT, false), cloakDefaultState.with(LeavesBlock.DISTANCE, 2).with(LeavesBlock.PERSISTENT, false));
            SpectrumCommon.getModelSwapper().swapModel(defaultState.with(LeavesBlock.DISTANCE, 3).with(LeavesBlock.PERSISTENT, false), cloakDefaultState.with(LeavesBlock.DISTANCE, 3).with(LeavesBlock.PERSISTENT, false));
            SpectrumCommon.getModelSwapper().swapModel(defaultState.with(LeavesBlock.DISTANCE, 4).with(LeavesBlock.PERSISTENT, false), cloakDefaultState.with(LeavesBlock.DISTANCE, 4).with(LeavesBlock.PERSISTENT, false));
            SpectrumCommon.getModelSwapper().swapModel(defaultState.with(LeavesBlock.DISTANCE, 1).with(LeavesBlock.PERSISTENT, true), cloakDefaultState.with(LeavesBlock.DISTANCE, 1).with(LeavesBlock.PERSISTENT, true));
            SpectrumCommon.getModelSwapper().swapModel(defaultState.with(LeavesBlock.DISTANCE, 2).with(LeavesBlock.PERSISTENT, true), cloakDefaultState.with(LeavesBlock.DISTANCE, 2).with(LeavesBlock.PERSISTENT, true));
            SpectrumCommon.getModelSwapper().swapModel(defaultState.with(LeavesBlock.DISTANCE, 3).with(LeavesBlock.PERSISTENT, true), cloakDefaultState.with(LeavesBlock.DISTANCE, 3).with(LeavesBlock.PERSISTENT, true));
            SpectrumCommon.getModelSwapper().swapModel(defaultState.with(LeavesBlock.DISTANCE, 4).with(LeavesBlock.PERSISTENT, true), cloakDefaultState.with(LeavesBlock.DISTANCE, 4).with(LeavesBlock.PERSISTENT, true));
            SpectrumCommon.getModelSwapper().swapModel(SpectrumBlocks.getColoredLeavesItem(dyeColor), Items.OAK_LEAVES); // item
        }
    }

    public void setUncloaked() {
        for(DyeColor dyeColor : DyeColor.values()) {
            Block block = SpectrumBlocks.getColoredLeavesBlock(dyeColor);
            SpectrumCommon.getModelSwapper().unswapAllBlockStates(block);
            SpectrumCommon.getModelSwapper().unswapModel(SpectrumBlocks.getColoredLeavesItem(dyeColor));
        }
    }

    @Deprecated
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        return getCloakedDroppedStacks(state, builder);
    }

}
