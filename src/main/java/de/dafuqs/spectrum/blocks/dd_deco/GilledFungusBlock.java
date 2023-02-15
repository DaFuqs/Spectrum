package de.dafuqs.spectrum.blocks.dd_deco;

import de.dafuqs.spectrum.worldgen.features.*;
import net.minecraft.block.*;
import net.minecraft.server.world.*;
import net.minecraft.tag.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.registry.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;

import java.util.function.*;

public class GilledFungusBlock extends PlantBlock implements Fertilizable {

    protected static final VoxelShape SHAPE = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 9.0, 12.0);
    private static final double GROW_CHANCE = 0.4;
    private final Supplier<RegistryEntry<ConfiguredFeature<?, ?>>> feature;

    public GilledFungusBlock(AbstractBlock.Settings settings, Supplier<RegistryEntry<ConfiguredFeature<?, ?>>> feature) {
        super(settings);
        this.feature = feature;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isIn(BlockTags.NYLIUM) || floor.isOf(Blocks.MYCELIUM) || floor.isOf(Blocks.SOUL_SOIL) || super.canPlantOnTop(floor, world, pos);
    }

    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        Block validBaseBlock = ((GilledFungusFeatureConfig) this.feature.get().value().config()).validBaseBlock.getBlock();
        BlockState baseBlock = world.getBlockState(pos.down());
        return baseBlock.isOf(validBaseBlock);
    }

    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return random.nextFloat() < GROW_CHANCE;
    }

    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        this.feature.get().value().generate(world, world.getChunkManager().getChunkGenerator(), random, pos);
    }

}
