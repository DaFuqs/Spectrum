package de.dafuqs.spectrum.blocks.redstone;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class EntityDetectorBlock extends DetectorBlock {
	
	public EntityDetectorBlock(Settings settings) {
		super(settings);
	}
	
	protected void updateState(BlockState state, World world, BlockPos pos) {
		List<LivingEntity> entities = world.getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class), getBoxWithRadius(pos, 10), LivingEntity::isAlive);
		
		int power = Math.min(entities.size(), 15);
		
		if (state.get(POWER) != power) {
			world.setBlockState(pos, state.with(POWER, power), 3);
		}
	}
	
	@Override
	int getUpdateFrequencyTicks() {
		return 20;
	}
	
}
