package de.dafuqs.spectrum.blocks.deeper_down.flora;

import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class WeepingGalaFrondsBlock extends PlantBlock {

    public WeepingGalaFrondsBlock(Settings settings) {
        super(settings);
    }

    @Override
    public float getMaxHorizontalModelOffset() {
        return 0.1F;
    }

    @Override
    public boolean canPlantOnTop(BlockState roof, BlockView world, BlockPos pos) {
        return roof.isOf(SpectrumBlocks.WEEPING_GALA_LEAVES) || roof.isOf(SpectrumBlocks.WEEPING_GALA_FRONDS);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        var roof = pos.up();
        var roofState = world.getBlockState(roof);

        if (roofState.isOf(this))
            return true;

        return canPlantOnTop(roofState, world, roof);
    }
}
