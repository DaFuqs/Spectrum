package de.dafuqs.spectrum.blocks.redstone;

import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;

public class EntityDetectorBlock extends DetectorBlock {
	
	public EntityDetectorBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	protected void updateState(BlockState state, World world, BlockPos pos) {
		List<LivingEntity> entities = world.getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class), getDetectionBox(pos), LivingEntity::isAlive);
		
		int power = Math.min(entities.size(), 15);
		
		power = state.get(INVERTED) ? 15 - power : power;
		if (state.get(POWER) != power) {
			world.setBlockState(pos, state.with(POWER, power), 3);
		}
	}
	
	@Override
	int getUpdateFrequencyTicks() {
		return 20;
	}
	
}
