package de.dafuqs.pigment.blocks.types.conditional;

import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.blocks.PigmentBlocks;
import de.dafuqs.pigment.interfaces.Cloakable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;

import java.util.List;

public class ColoredLogBlock extends PillarBlock implements Cloakable {

    private boolean wasLastCloaked;

    public ColoredLogBlock(Settings settings) {
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
        // Colored Logs => Oak logs
        BlockState cloakDefaultState = Blocks.OAK_LOG.getDefaultState();
        for(DyeColor dyeColor : DyeColor.values()) {
            BlockState defaultState = PigmentBlocks.getColoredLogBlock(dyeColor).getDefaultState();
            PigmentCommon.getModelSwapper().swapModel(defaultState, cloakDefaultState); // block
            PigmentCommon.getModelSwapper().swapModel(PigmentBlocks.getColoredLogItem(dyeColor), Items.OAK_LOG); // item
        }
    }

    public void setUncloaked() {
        for(DyeColor dyeColor : DyeColor.values()) {
            Block block = PigmentBlocks.getColoredLogBlock(dyeColor);
            PigmentCommon.getModelSwapper().unswapAllBlockStates(block);
            PigmentCommon.getModelSwapper().unswapModel(PigmentBlocks.getColoredLogItem(dyeColor));
        }
    }

    @Deprecated
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        return getCloakedDroppedStacks(state, builder);
    }

}
