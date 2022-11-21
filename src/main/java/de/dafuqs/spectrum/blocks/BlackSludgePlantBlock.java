package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class BlackSludgePlantBlock extends PlantBlock {

    public BlackSludgePlantBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isIn(SpectrumBlockTags.BLACK_SLUDGE_BLOCKS) || super.canPlantOnTop(floor, world, pos);
    }
    
    @Override
    public float getMaxHorizontalModelOffset() {
        return 0.08F;
    }
}
