package de.dafuqs.spectrum.blocks.types.conditional;

import de.dafuqs.spectrum.SpectrumClient;
import de.dafuqs.spectrum.blocks.SpectrumBlocks;
import de.dafuqs.spectrum.interfaces.Cloakable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;

public class ColoredLogBlock extends PillarBlock implements Cloakable {

    private boolean wasLastCloaked;

    public ColoredLogBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isCloaked(ClientPlayerEntity clientPlayerEntity, BlockState blockState) {
        return clientPlayerEntity.getArmor() > 0;
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
            BlockState defaultState = SpectrumBlocks.getColoredLog(dyeColor).getDefaultState();
            SpectrumClient.getModelSwapper().swapModel(defaultState, cloakDefaultState);
        }
    }

    public void setUncloaked() {
        for(DyeColor dyeColor : DyeColor.values()) {
            Block block = SpectrumBlocks.getColoredLog(dyeColor);
            SpectrumClient.getModelSwapper().unswapAllBlockStates(block);
        }
    }

}
