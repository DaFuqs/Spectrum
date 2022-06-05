package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.GeodeFeature;
import net.minecraft.world.gen.feature.GeodeFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SolidBlockCheckGeodeFeature extends GeodeFeature {
	
	private static final int MAX_NON_SOLID_BLOCKS = 3;
	
	public SolidBlockCheckGeodeFeature(Codec<GeodeFeatureConfig> configCodec) {
		super(configCodec);
	}
	
	@Override
	public boolean generate(FeatureContext<GeodeFeatureConfig> context) {
		int airBlocks = 0;
		
		StructureWorldAccess world = context.getWorld();
		BlockPos sourcePos = context.getOrigin();
		int distance = (int) context.getConfig().layerThicknessConfig.outerLayer;
		for (Direction direction : Direction.values()) {
			BlockPos offsetPos = sourcePos.offset(direction, distance);
			BlockState blockStateAtPos = world.getBlockState(offsetPos);
			
			if (blockStateAtPos.isAir() || !blockStateAtPos.isFullCube(world, offsetPos)) {
				airBlocks++;
				if (airBlocks > MAX_NON_SOLID_BLOCKS) {
					return false;
				}
			}
		}
		
		// one additional check double as high to prevent them sticking out of the ground a bit more often
		BlockPos upperPos = sourcePos.up(distance + 4);
		BlockState blockStateAtPos = world.getBlockState(upperPos);
		if (blockStateAtPos.isAir() || !blockStateAtPos.isFullCube(world, upperPos)) {
			airBlocks++;
			if (airBlocks > MAX_NON_SOLID_BLOCKS) {
				return false;
			}
		}
		
		return super.generate(context);
	}
	
}
