package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.worldgen.SpectrumConfiguredFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.PlantBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class JadeiteLotusBulbBlock extends PlantBlock implements Fertilizable {

    public JadeiteLotusBulbBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return canPlantOnTop(world.getBlockState(pos.up()), world, pos.up());
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextFloat() < 0.025) {
            SpectrumConfiguredFeatures.JADEITE_LOTUS_BULB.value().generate(world, world.getChunkManager().getChunkGenerator(), random, pos);
        }
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
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return super.canPlantOnTop(floor, world, pos) || floor.isIn(SpectrumBlockTags.BASE_STONE_DEEPER_DOWN);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        SpectrumConfiguredFeatures.JADEITE_LOTUS_BULB.value().generate(world, world.getChunkManager().getChunkGenerator(), random, pos);
    }
}
