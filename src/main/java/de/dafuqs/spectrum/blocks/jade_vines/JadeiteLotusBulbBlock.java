package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.registry.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;

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
			grow(world, random, pos, state);
		}
	}
	
	@Override
	public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
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
		world.getRegistryManager().get(RegistryKeys.CONFIGURED_FEATURE).get(SpectrumConfiguredFeatures.JADEITE_LOTUS).generate(world, world.getChunkManager().getChunkGenerator(), random, pos);
    }
	
}
