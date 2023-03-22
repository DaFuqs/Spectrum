package de.dafuqs.spectrum.blocks.dd_deco;

import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

public class RottenGroundBlock extends MudBlock {
    
    public static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 11, 16);
    
    public RottenGroundBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
    
}
