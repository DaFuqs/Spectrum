package de.dafuqs.spectrum.blocks.dd_deco;

import net.minecraft.block.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

import java.util.*;

public class SmallDragonjagBlock extends PlantBlock implements Dragonjag, Fertilizable {

    protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 12.0, 13.0, 14.0);
    protected static final Map<Dragonjag.Variant, Block> VARIANTS = new HashMap<>();
    protected final Dragonjag.Variant variant;

    public SmallDragonjagBlock(Settings settings, Dragonjag.Variant variant) {
        super(settings);
        this.variant = variant;
        VARIANTS.put(variant, this);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return Dragonjag.canPlantOnTop(floor, world, pos);
    }

    @Override
    public Variant getVariant() {
        return variant;
    }

    public static Block getBlockForVariant(Variant variant) {
        return VARIANTS.get(variant);
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        TallDragonjagBlock tallVariant = TallDragonjagBlock.getBlockForVariant(variant);
        if (tallVariant.getDefaultState().canPlaceAt(world, pos) && world.isAir(pos.up())) {
            TallDragonjagBlock.placeAt(world, tallVariant.getDefaultState(), pos, 2);
        }
    }

}