package de.dafuqs.pigment.blocks.mob_head;

import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.WallSkullBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PigmentWallSkullBlock extends WallSkullBlock {

    public PigmentWallSkullBlock(SkullBlock.SkullType skullType, Settings settings) {
        super(skullType, settings);
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PigmentSkullBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return null;
    }

}
