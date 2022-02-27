package de.dafuqs.spectrum.blocks.creature_spawn;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class CreatureSpawnBlockEntity extends BlockEntity {
	
	public CreatureSpawnBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.CREATURE_SPAWN, pos, state);
	}
	
}
