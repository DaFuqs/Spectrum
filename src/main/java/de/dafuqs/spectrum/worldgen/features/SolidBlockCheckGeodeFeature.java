package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.configurations.*;

public class SolidBlockCheckGeodeFeature extends GeodeFeature {
	
	private static final int MAX_NON_SOLID_BLOCKS = 3;
	
	public SolidBlockCheckGeodeFeature(Codec<GeodeConfiguration> configCodec) {
		super(configCodec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<GeodeConfiguration> context) {
		int airBlocks = 0;
		
		WorldGenLevel world = context.level();
		BlockPos sourcePos = context.origin();
		int distance = (int) context.config().geodeLayerSettings.outerLayer;
		for (Direction direction : Direction.values()) {
			BlockPos offsetPos = sourcePos.relative(direction, distance);
			BlockState blockStateAtPos = world.getBlockState(offsetPos);
			
			if (blockStateAtPos.isAir() || !blockStateAtPos.isCollisionShapeFullBlock(world, offsetPos)) {
				airBlocks++;
				if (airBlocks > MAX_NON_SOLID_BLOCKS) {
					return false;
				}
			}
		}
		
		// one additional check double as high to prevent them sticking out of the ground a bit more often
		BlockPos upperPos = sourcePos.above(distance + 4);
		BlockState blockStateAtPos = world.getBlockState(upperPos);
		if (blockStateAtPos.isAir() || !blockStateAtPos.isCollisionShapeFullBlock(world, upperPos)) {
			airBlocks++;
			if (airBlocks > MAX_NON_SOLID_BLOCKS) {
				return false;
			}
		}
		
		return super.place(context);
	}
	
}
