package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.feature.*;

public class GiantGilledFungusFeature extends Feature<GilledFungusFeatureConfig> {
	
	public GiantGilledFungusFeature(Codec<GilledFungusFeatureConfig> codec) {
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<GilledFungusFeatureConfig> context) {
		WorldGenLevel world = context.level();
		BlockPos blockPos = context.origin();
		GilledFungusFeatureConfig config = context.config();
		Block validBaseBlock = config.validBase();
		
		BlockState baseBlock = world.getBlockState(blockPos.below());
		
		if (!baseBlock.is(validBaseBlock)) {
			return false;
		}
		
		RandomSource random = context.random();
		ChunkGenerator chunkGenerator = context.chunkGenerator();
		
		var maxHeight = config.baseStemHeight().sample(random);
		var mutable = blockPos.mutable();
		int stemHeight = 0;
		int stemGirth = 1;
		
		// Find out just how tall we can go
		for (; stemHeight < maxHeight; ++stemHeight) {
			if (!isReplaceable(world, mutable.move(Direction.UP), false)) {
				break;
			}
		}
		
		if (stemHeight < maxHeight / 1.667F)
			return false;
		
		stemGirth += (int) Math.floor(stemHeight / 33F);
		
		if (blockPos.getY() + stemHeight + 1 >= chunkGenerator.getGenDepth()) {
			return false;
		}
		
		world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 4);
		
		this.generateStemRing(blockPos.below(3), config, world, random, stemGirth, stemHeight + 3, true);
		
		if (stemHeight > 60 || (stemHeight > 30 && random.nextBoolean())) {
			this.generateHat(world, random, config, blockPos, stemHeight, 1F);
			this.generateHat(world, random, config, blockPos, Math.round(stemHeight * 0.65F), 0.7F);
		} else {
			this.generateHat(world, random, config, blockPos, stemHeight, 1F);
		}
		
		for (int i = 0; i < stemHeight / 1.5F; i++) {
			this.generateHat(world, random, config, blockPos.offset(random.nextInt(stemGirth * 3), 0, random.nextInt(stemGirth * 3)), random.nextInt(stemHeight / 5) + 1, (random.nextFloat() / (i + 1) * 0.5F) + 0.5F);
		}
		
		return true;
	}
	
	private static boolean isReplaceable(LevelAccessor world, BlockPos pos, boolean replacePlants) {
		return world.isStateAtPosition(pos, (state) -> state.canBeReplaced() || replacePlants);
	}
	
	private void generateStemRing(BlockPos blockPos, GilledFungusFeatureConfig config, LevelAccessor world, RandomSource random, int stemGirth, int stemHeight, boolean recursive) {
		var stemPos = blockPos.mutable();
		
		for (int x = -stemGirth; x <= stemGirth; x++) {
			for (int z = -stemGirth; z <= stemGirth; z++) {
				stemPos.setWithOffset(blockPos, x, 0, z);
				if (Math.sqrt(stemPos.distSqr(blockPos)) > stemGirth)
					continue;
				
				this.generateStem(world, config, stemPos, stemHeight);
				if (recursive && !stemPos.equals(blockPos) && random.nextFloat() <= 0.2F) {
					var height = Math.round((float) stemHeight * (random.nextFloat() * 0.667F));
					var offsetX = (int) (x * (2 + random.nextFloat()));
					var offsetZ = (int) (z * (2 + random.nextFloat()));
					
					generateStemRing(stemPos.offset(offsetX, 0, offsetZ), config, world, random, stemGirth / 2, height, false);
					this.generateHat(world, random, config, stemPos.offset((int) (offsetX * 1.5), 0, (int) (offsetZ * 1.5)), height, 0.333F + random.nextFloat() * 0.667F);
				}
				
			}
		}
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
	
	private void generateHat(LevelAccessor world, RandomSource random, GilledFungusFeatureConfig config, BlockPos pos, int stemHeight, float sizeMod) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		int hatRadius = Math.round(Math.min(Math.round(random.nextInt(Math.round(1 + stemHeight / 7F)) + (stemHeight / 7F) + 3) * sizeMod, 17));
		int currentRadius;
		
		BlockState gillsState = config.gills().defaultBlockState();
		BlockState capState = config.cap().defaultBlockState();
		var start = hatRadius > 11 ? -3 : -2;
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
							if (rad <= currentRadius / 1.5F && rad >= currentRadius / 3F)
								continue;
							
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
