package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.feature.*;

public class GilledFungusFeature extends Feature<GilledFungusFeatureConfig> {
	
	public GilledFungusFeature(Codec<GilledFungusFeatureConfig> codec) {
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<GilledFungusFeatureConfig> context) {
		WorldGenLevel structureWorldAccess = context.level();
		BlockPos blockPos = context.origin();
		GilledFungusFeatureConfig hugeFungusFeatureConfig = context.config();
		Block validBaseBlock = hugeFungusFeatureConfig.validBase();
		BlockState baseBlock = structureWorldAccess.getBlockState(blockPos.below());
		
		if (!baseBlock.is(validBaseBlock)) {
			return false;
		}
		
		RandomSource random = context.random();
		ChunkGenerator chunkGenerator = context.chunkGenerator();
		
		int stemHeight = hugeFungusFeatureConfig.baseStemHeight().sample(random);
		if (random.nextInt(12) == 0) {
			stemHeight *= 2;
		}
		if (blockPos.getY() + stemHeight + 1 >= chunkGenerator.getGenDepth()) {
			return false;
		}
		
		structureWorldAccess.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 4);
		this.generateStem(structureWorldAccess, hugeFungusFeatureConfig, blockPos, stemHeight);
		this.generateHat(structureWorldAccess, random, hugeFungusFeatureConfig, blockPos, stemHeight);
		return true;
	}
	
	private static boolean isReplaceable(LevelAccessor world, BlockPos pos, boolean replacePlants) {
		return world.isStateAtPosition(pos, (state) -> state.canBeReplaced() || replacePlants);
	}
	
	private void generateStem(LevelAccessor world, GilledFungusFeatureConfig config, BlockPos pos, int stemHeight) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		BlockState blockState = config.stem().defaultBlockState();
		int i = 0;
		for (int x = -i; x <= i; ++x) {
			for (int z = -i; z <= i; ++z) {
				for (int y = 0; y < stemHeight; ++y) {
					mutable.setWithOffset(pos, x, y, z);
					if (isReplaceable(world, mutable, true)) {
						this.setBlock(world, mutable, blockState);
					}
				}
			}
		}
	}
	
	private void generateHat(LevelAccessor world, RandomSource random, GilledFungusFeatureConfig config, BlockPos pos, int stemHeight) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		int hatRadius = Math.min(random.nextInt(1 + stemHeight / 5) + 4, 9);
		int currentRadius;
		
		BlockState gillsState = config.gills().defaultBlockState();
		BlockState capState = config.cap().defaultBlockState();
		var start = hatRadius > 4 ? -2 : -1;
		var firstLoop = true;
		
		for (int y = start; y < Math.max(Math.round(hatRadius / 3F), 2); ++y) {
			var underHang = y < 0;
			boolean isLowestLevel = y == 0;
			
			currentRadius = underHang ? hatRadius : (int) Math.round(hatRadius / Math.pow(1.175, Math.max(y - 1, 0))) - (isLowestLevel ? 0 : 1);
			
			for (int x = -currentRadius; x <= currentRadius; ++x) {
				for (int z = -currentRadius; z <= currentRadius; ++z) {
					
					boolean isCorner = Math.abs(x) == currentRadius && Math.abs(z) == currentRadius;
					if (isCorner) {
						continue;
					}
					
					mutable.setWithOffset(pos, x, stemHeight + y, z);
					if (isReplaceable(world, mutable, false)) {
						var rad = Math.sqrt(mutable.distToCenterSqr(pos.getX(), mutable.getY(), pos.getZ()));
						
						if (underHang) {
							if (!(random.nextInt(3) == 0 && firstLoop) && rad <= currentRadius && rad > currentRadius - 1) {
								this.setBlock(world, mutable, capState);
							}
						} else if (isLowestLevel) {
							if (rad <= currentRadius - 1) {
								this.setBlock(world, mutable, gillsState.setValue(RotatedPillarBlock.AXIS, Math.abs(x) < Math.abs(z) ? Direction.Axis.X : Direction.Axis.Z));
							} else if (rad <= currentRadius) {
								this.setBlock(world, mutable, capState);
								
							}
						} else if (rad <= currentRadius) {
							this.setBlock(world, mutable, capState);
							
						}
					}
				}
			}
			
			firstLoop = false;
		}
	}
	
}
