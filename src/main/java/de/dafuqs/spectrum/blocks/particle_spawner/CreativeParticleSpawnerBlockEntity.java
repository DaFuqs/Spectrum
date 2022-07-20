package de.dafuqs.spectrum.blocks.particle_spawner;

import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class CreativeParticleSpawnerBlockEntity extends ParticleSpawnerBlockEntity {
	
	public CreativeParticleSpawnerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.CREATIVE_PARTICLE_SPAWNER, blockPos, blockState);
	}
	
}
