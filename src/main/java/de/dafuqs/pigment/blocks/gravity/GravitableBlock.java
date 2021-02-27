package de.dafuqs.pigment.blocks.gravity;

import de.dafuqs.pigment.entity.entity.GravityBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public class GravitableBlock extends FallingBlock {

    private final float gravityMod;

    public GravitableBlock(Settings settings, float gravityMod) {
        super(settings);
        this.gravityMod = gravityMod;
    }

    public float getGravityMod() {
        return gravityMod;
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos posIn, BlockState oldState, boolean notify) {
        worldIn.getBlockTickScheduler().schedule(posIn, this, this.getFallDelay());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facingIn, BlockState facingState, WorldAccess worldIn, BlockPos posIn, BlockPos facingPosIn) {
        worldIn.getBlockTickScheduler().schedule(posIn, this, this.getFallDelay());
        return super.getStateForNeighborUpdate(stateIn, facingIn, facingState, worldIn, posIn, facingPosIn);
    }

    @Override
    public void scheduledTick(BlockState stateIn, ServerWorld worldIn, BlockPos posIn, Random randIn) {
        this.checkGravitable(worldIn, posIn);
    }

    private void checkGravitable(World worldIn, BlockPos pos) {
        if (!worldIn.isClient) {
            BlockPos collisionBlockPos;
            if (gravityMod > 0) {
                collisionBlockPos = pos.up();
            } else {
                collisionBlockPos = pos.down();
            }

            if ((worldIn.isAir(collisionBlockPos) || canFallThrough(worldIn.getBlockState(collisionBlockPos)))) {
                GravityBlockEntity blockEntity = new GravityBlockEntity(worldIn, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, worldIn.getBlockState(pos));
                this.onStartFloating(blockEntity);
                worldIn.spawnEntity(blockEntity);
            }
        }
    }

    protected void onStartFloating(GravityBlockEntity entityIn) {
    }

    public void onEndFloating(World worldIn, BlockPos posIn, BlockState floatingState, BlockState hitState) {
    }

    @Override
    protected int getFallDelay() {
        return 2;
    }

}