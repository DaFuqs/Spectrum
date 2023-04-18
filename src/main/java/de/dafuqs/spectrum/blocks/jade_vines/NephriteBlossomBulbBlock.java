package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.*;

public class NephriteBlossomBulbBlock extends PlantBlock implements Fertilizable {
	
	public NephriteBlossomBulbBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean hasRandomTicks(BlockState state) {
		return true;
	}
	
	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return SpectrumItems.NEPHRITE_BLOSSOM_BULB.getDefaultStack();
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (random.nextFloat() < 0.025) {
			grow(world, random, pos, state);
		}
	}
	
	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		return true;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return random.nextFloat() < 0.075;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		world.getRegistryManager().get(Registry.CONFIGURED_FEATURE_KEY).get(SpectrumConfiguredFeatures.NEPHRITE_BLOSSOM_BULB).generate(world, world.getChunkManager().getChunkGenerator(), random, pos);
    }
}
