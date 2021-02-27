package de.dafuqs.pigment.blocks.fluid;

import de.dafuqs.pigment.registries.PigmentBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class LiquidCrystalFluidBlock extends FluidBlock {

    public LiquidCrystalFluidBlock(FlowableFluid fluid, Settings settings) {
        super(fluid, settings);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (this.receiveNeighborFluids(world, pos, state)) {
            world.getFluidTickScheduler().schedule(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (this.receiveNeighborFluids(world, pos, state)) {
            world.getFluidTickScheduler().schedule(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
        }
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return true;
    }

    /**
     * @param world The world
     * @param pos The position in the world
     * @param state BlockState of the liquid crystal. Included the height/fluid level
     * @return Dunno, actually. I just mod things.
     */
    private boolean receiveNeighborFluids(World world, BlockPos pos, BlockState state) {
        Direction[] var5 = Direction.values();
        int var6 = var5.length;
        for(int var7 = 0; var7 < var6; ++var7) {
            Direction direction = var5[var7];
            BlockPos blockPos = pos.offset(direction);
            if (world.getFluidState(blockPos).isIn(FluidTags.WATER)) {
                Block block = world.getFluidState(pos).isStill() ? PigmentBlocks.FROSTBITE_CRYSTAL : Blocks.CALCITE;
                world.setBlockState(pos, block.getDefaultState());
                this.playExtinguishSound(world, pos);
                return false;
            }
            if (world.getFluidState(blockPos).isIn(FluidTags.LAVA)) {
                Block block = world.getFluidState(pos).isStill() ? PigmentBlocks.BLAZING_CRYSTAL : Blocks.TUFF;
                world.setBlockState(pos, block.getDefaultState());
                this.playExtinguishSound(world, pos);
                return false;
            }
        }
        return true;
    }

    // TODO: other sound
    private void playExtinguishSound(WorldAccess world, BlockPos pos) {
        world.syncWorldEvent(1501, pos, 0);
    }


}
