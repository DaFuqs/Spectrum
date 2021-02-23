package de.dafuqs.pigment.blocks.conditional;

import de.dafuqs.pigment.PigmentBlocks;
import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.interfaces.Cloakable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class ColoredSaplingBlock extends SaplingBlock implements Cloakable {

    private boolean wasLastCloaked;

    public ColoredSaplingBlock(SaplingGenerator generator, Settings settings) {
        super(generator, settings);
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

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if(world instanceof ClientWorld) {
            checkCloak(state);
        }
        return SHAPE;
    }

    public void setCloaked() {
        // Colored Logs => Oak logs
        BlockState cloakDefaultState = Blocks.OAK_SAPLING.getDefaultState();
        for(DyeColor dyeColor : DyeColor.values()) {
            BlockState defaultState = PigmentBlocks.getColoredSaplingBlock(dyeColor).getDefaultState();
            PigmentCommon.getBlockCloaker().swapModel(defaultState, cloakDefaultState); // block
            PigmentCommon.getBlockCloaker().swapModel(PigmentBlocks.getColoredSaplingItem(dyeColor), Items.OAK_SAPLING); // item
        }
    }

    public void setUncloaked() {
        for(DyeColor dyeColor : DyeColor.values()) {
            Block block = PigmentBlocks.getColoredSaplingBlock(dyeColor);
            PigmentCommon.getBlockCloaker().unswapAllBlockStates(block);
            PigmentCommon.getBlockCloaker().unswapModel(PigmentBlocks.getColoredLogItem(dyeColor));
        }
    }

}
