package de.dafuqs.spectrum.blocks.particle_spawner;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class CreativeParticleSpawnerBlockEntity extends ParticleSpawnerBlockEntity {
	
	public CreativeParticleSpawnerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntityRegistry.CREATIVE_PARTICLE_SPAWNER, blockPos, blockState);
	}
	
}
