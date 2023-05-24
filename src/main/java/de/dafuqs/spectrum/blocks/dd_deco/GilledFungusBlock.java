package de.dafuqs.spectrum.blocks.dd_deco;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.server.world.*;
import net.minecraft.tag.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.registry.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;

public class GilledFungusBlock extends PlantBlock implements Fertilizable {

    protected static final VoxelShape SHAPE = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 9.0, 12.0);
    private static final double GROW_CHANCE = 0.4;
    private final Identifier featureId;
    
    public GilledFungusBlock(AbstractBlock.Settings settings, Identifier featureId) {
        super(settings);
        this.featureId = featureId;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isIn(BlockTags.NYLIUM) || floor.isOf(Blocks.MYCELIUM) || floor.isOf(Blocks.SOUL_SOIL) || super.canPlantOnTop(floor, world, pos);
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return world.getBlockState(pos.down()).isOf(SpectrumBlocks.SHIMMEL);
    }
    
    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return random.nextFloat() < GROW_CHANCE;
    }
    
    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        getConfiguredFeatureRegistryEntry(world).value().generate(world, world.getChunkManager().getChunkGenerator(), random, pos);
    }
    
    private RegistryEntry<ConfiguredFeature<?, ?>> getConfiguredFeatureRegistryEntry(World world) {
        return world.getRegistryManager().get(Registry.CONFIGURED_FEATURE_KEY).entryOf(RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, featureId));
    }
    
}
