package de.dafuqs.spectrum.blocks.energy;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.PastelNetworkNodeBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class InkDuctBlockEntity extends BlockEntity {

    public static int RANGE=64;

    public InkDuctBlockEntity(BlockEntityType blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public boolean canSee(InkDuctBlockEntity duct){return duct.pos.isWithinDistance(pos, RANGE);}

    private boolean canTransferTo(BlockEntity blockEntity) {
        return blockEntity instanceof InkDuctBlockEntity;
    }

    public int getRange(){
        return RANGE;
    }
}
