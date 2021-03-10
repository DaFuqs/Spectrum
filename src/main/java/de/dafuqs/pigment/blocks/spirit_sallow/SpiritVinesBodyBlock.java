package de.dafuqs.pigment.blocks.spirit_sallow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class SpiritVinesBodyBlock extends AbstractPlantBlock implements SpiritVines {

    public SpiritVinesBodyBlock(Settings settings) {
        super(settings, Direction.DOWN, SHAPE, false);
        this.setDefaultState((this.stateManager.getDefaultState()).with(YIELD, YieldType.NONE));
    }

    protected AbstractPlantStemBlock getStem() {
        return (AbstractPlantStemBlock)Blocks.CAVE_VINES_HEAD;
    }

    protected BlockState copyState(BlockState from, BlockState to) {
        return to.with(YIELD, from.get(YIELD));
    }

    @Environment(EnvType.CLIENT)
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(SpiritVines.getYieldItem(state));
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return SpiritVines.pick(state, world, pos);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(YIELD);
    }

    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return false;
    }

    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state.with(YIELD, YieldType.NONE), 2);
    }
}
