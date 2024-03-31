package de.dafuqs.spectrum.blocks.dd_deco;

import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.FernBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class AshFloraBlock extends FernBlock {

    public AshFloraBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(SpectrumBlocks.ASH) || floor.isOf(SpectrumBlocks.ASHEN_BLACKSLAG) || super.canPlantOnTop(floor, world, pos);
    }
}
