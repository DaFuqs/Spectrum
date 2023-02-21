package de.dafuqs.spectrum.blocks.dd_deco;

import de.dafuqs.spectrum.deeper_down.*;
import net.minecraft.block.*;
import net.minecraft.block.enums.*;
import net.minecraft.server.world.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

import java.util.*;

public class TallDragonjagBlock extends TallPlantBlock implements Dragonjag, Fertilizable {

    protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);
    public static final BooleanProperty DEAD = BooleanProperty.of("dead");
    protected static final Map<Dragonjag.Variant, TallDragonjagBlock> VARIANTS = new HashMap<>();
    protected final Dragonjag.Variant variant;

    public TallDragonjagBlock(Settings settings, Dragonjag.Variant variant) {
        super(settings);
        this.variant = variant;
        VARIANTS.put(variant, this);
        this.setDefaultState(this.stateManager.getDefaultState().with(HALF, DoubleBlockHalf.LOWER).with(DEAD, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return Dragonjag.canPlantOnTop(floor, world, pos);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HALF, DEAD);
    }

    @Override
    public Dragonjag.Variant getVariant() {
        return variant;
    }

    public static TallDragonjagBlock getBlockForVariant(Variant variant) {
        return VARIANTS.get(variant);
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return !state.get(DEAD);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return !state.get(DEAD);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        boolean success = false;
        switch (variant) {
            case RED -> {
                success = DDConfiguredFeatures.RED_DRAGONJAGS.generate(world, world.getChunkManager().getChunkGenerator(), random, pos.up());
            }
            case PINK -> {
                success = DDConfiguredFeatures.PINK_DRAGONJAGS.generate(world, world.getChunkManager().getChunkGenerator(), random, pos.up());
            }
            case BLACK -> {
                success = DDConfiguredFeatures.BLACK_DRAGONJAGS.generate(world, world.getChunkManager().getChunkGenerator(), random, pos.up());
            }
            case GREEN -> {
                success = DDConfiguredFeatures.GREEN_DRAGONJAGS.generate(world, world.getChunkManager().getChunkGenerator(), random, pos.up());
            }
            case PURPLE -> {
                success = DDConfiguredFeatures.PURPLE_DRAGONJAGS.generate(world, world.getChunkManager().getChunkGenerator(), random, pos.up());
            }
        }

        if (success) {
            setDead(world, pos, state);
        }
    }

    private void setDead(World world, BlockPos pos, BlockState state) {
        BlockState posState = world.getBlockState(pos);
        if (posState.isOf(this)) {
            world.setBlockState(pos, posState.with(DEAD, true));
        }
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            posState = world.getBlockState(pos.up());
            if (posState.isOf(this)) {
                world.setBlockState(pos.up(), posState.with(DEAD, true));
            }
        } else {
            posState = world.getBlockState(pos.down());
            if (posState.isOf(this)) {
                world.setBlockState(pos.down(), posState.with(DEAD, true));
            }
        }
    }

}
