package de.dafuqs.pigment.blocks;

import de.dafuqs.pigment.PigmentBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.GlassBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class GemGlassBlock extends GlassBlock {

    public GemGlassBlock(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        if(stateFrom.isOf(this)) {
            return true;
        }
        if(state.getBlock().equals(PigmentBlocks.AMETHYST_GLASS) && stateFrom.getBlock().equals(PigmentBlocks.AMETHYST_PLAYER_ONLY_GLASS)
            || state.getBlock().equals(PigmentBlocks.CITRINE_GLASS) && stateFrom.getBlock().equals(PigmentBlocks.CITRINE_PLAYER_ONLY_GLASS)
            || state.getBlock().equals(PigmentBlocks.TOPAZ_GLASS) && stateFrom.getBlock().equals(PigmentBlocks.TOPAZ_PLAYER_ONLY_GLASS)
            || state.getBlock().equals(PigmentBlocks.MOONSTONE_GLASS) && stateFrom.getBlock().equals(PigmentBlocks.MOONSTONE_PLAYER_ONLY_GLASS)
            || state.getBlock().equals(PigmentBlocks.ONYX_GLASS) && stateFrom.getBlock().equals(PigmentBlocks.ONYX_PLAYER_ONLY_GLASS)) {
            return true;
        }
        return super.isSideInvisible(state, stateFrom, direction);
    }

    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

}
