package de.dafuqs.spectrum.blocks.dd_deco;

import de.dafuqs.spectrum.features.*;
import de.dafuqs.spectrum.registries.SpectrumConfiguredFeatures;
import net.minecraft.block.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;

import java.util.function.*;

public class GilledFungusBlock extends PlantBlock implements Fertilizable {

    protected static final VoxelShape SHAPE = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 9.0, 12.0);
    private static final double GROW_CHANCE = 0.4;
    private final Identifier id;

    public GilledFungusBlock(AbstractBlock.Settings settings, Identifier id) {
        super(settings);
        this.id = id;
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
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        Block validBaseBlock = ((GilledFungusFeatureConfig) world.getRegistryManager().get(RegistryKeys.PLACED_FEATURE).getOrEmpty(id).get().feature().value().config()).validBase();
        BlockState baseBlock = world.getBlockState(pos.down());
        return baseBlock.isOf(validBaseBlock);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return random.nextFloat() < GROW_CHANCE;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        world.getRegistryManager().get(RegistryKeys.PLACED_FEATURE).getOrEmpty(id).get().generate(world, world.getChunkManager().getChunkGenerator(), random, pos);
    }

}
