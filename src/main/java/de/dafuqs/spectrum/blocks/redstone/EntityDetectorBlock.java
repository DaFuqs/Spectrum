package de.dafuqs.spectrum.blocks.redstone;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.entity.*;

import java.util.*;

public class EntityDetectorBlock extends DetectorBlock {
	
	public static final MapCodec<EntityDetectorBlock> CODEC = simpleCodec(EntityDetectorBlock::new);
	
	public EntityDetectorBlock(Properties settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends EntityDetectorBlock> codec() {
		return CODEC;
	}
	
	@Override
	protected void updateState(BlockState state, Level world, BlockPos pos) {
		List<LivingEntity> entities = world.getEntities(EntityTypeTest.forClass(LivingEntity.class), getDetectionBox(pos), LivingEntity::isAlive);
		
		int power = Math.min(entities.size(), 15);
		
		power = state.getValue(INVERTED) ? 15 - power : power;
		if (state.getValue(POWER) != power) {
			world.setBlock(pos, state.setValue(POWER, power), 3);
		}
	}
	
	@Override
	int getUpdateFrequencyTicks() {
		return 20;
	}
	
}
