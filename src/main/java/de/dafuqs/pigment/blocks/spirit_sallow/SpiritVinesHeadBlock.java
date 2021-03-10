package de.dafuqs.pigment.blocks.spirit_sallow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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

public class SpiritVinesHeadBlock extends AbstractPlantStemBlock implements SpiritVines {

    public SpiritVinesHeadBlock(Settings settings) {
        super(settings, Direction.DOWN, SHAPE, false, 0.0D);
        this.setDefaultState((this.stateManager.getDefaultState()).with(YIELD, YieldType.NONE));
    }

    protected int getGrowthLength(Random random) {
        return 1;
    }

    protected boolean chooseStemState(BlockState state) {
        return state.isAir();
    }

    protected Block getPlant() {
        return Blocks.CAVE_VINES_BODY;
    }

    protected BlockState copyState(BlockState from, BlockState to) {
        return to.with(YIELD, from.get(YIELD));
    }

    protected BlockState age(BlockState state, Random random) {
        return super.age(state, random).with(YIELD, YieldType.NONE);
    }

    @Environment(EnvType.CLIENT)
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(SpiritVines.getYieldItem(state));
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return SpiritVines.pick(state, world, pos);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
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
