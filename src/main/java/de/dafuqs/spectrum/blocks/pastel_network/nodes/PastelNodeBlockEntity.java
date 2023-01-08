package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.blocks.pastel_network.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.inventory.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public abstract class PastelNodeBlockEntity extends BlockEntity {

    public static int RANGE = 16;
    private final TickLooper tickTimer = new TickLooper(40);
    protected PastelNetwork network;
    protected Inventory connectedInventory;

    public PastelNodeBlockEntity(BlockEntityType blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public boolean canSee(PastelNodeBlockEntity node) {
        return node.pos.isWithinDistance(this.pos, RANGE);
    }

    public void updateConnectedInventory(World world, BlockPos blockPos, Direction attachedDirection) {
        BlockEntity connectedBlockEntity = world.getBlockEntity(blockPos.offset(attachedDirection));
        if (connectedBlockEntity instanceof Inventory inventory) {
            this.connectedInventory = inventory;
        }
    }

    public void onPlaced(World world, BlockPos pos, Direction attachedDirection) {
        tickTimer.checkCap();
        updateConnectedInventory(world, pos, attachedDirection);
        this.network = PastelNetworkManager.getNetworkForNewNode(this);
    }

    public void onBreak() {
        if (this.network != null) {
            this.network.removeNode(this);
        }
    }

    public PastelNetwork getNetwork() {
        return this.network;
    }

}
