package de.dafuqs.spectrum.worldgen.structures;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.heightproviders.*;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.*;
import net.minecraft.world.level.levelgen.structure.pools.*;
import net.minecraft.world.level.levelgen.structure.pools.alias.*;
import net.minecraft.world.level.levelgen.structure.structures.*;

import java.util.*;
import java.util.function.*;

/**
 * A Jigsaw Structure that has more control over where it can be placed (VerticalPlacement)
 * Since JigsawStructure and its properties are all final, we use our own implementation
 * This different jigsaw structure uses the chunk generator sample instead of a heightmap for its placement
 * Making it easier to place at a position that matches a certain condition
 */
public abstract class UndergroundStructure extends Structure {
	
	protected final int size;
	protected final int placementCheckWidth;
	protected final int placementCheckHeight;
	protected final HeightProvider startHeight;
	protected final IntProvider buryDepth;
	protected final int maxDistanceFromCenter;
	
	public UndergroundStructure(StructureSettings config, Integer size, HeightProvider startHeight,
                                IntProvider buryDepth, Integer placementCheckWidth, Integer placementCheckHeight, Integer maxDistanceFromCenter) {
		
		super(config);
		this.size = size;
		this.startHeight = startHeight;
		this.buryDepth = buryDepth;
		this.placementCheckWidth = placementCheckWidth;
		this.placementCheckHeight = placementCheckHeight;
		this.maxDistanceFromCenter = maxDistanceFromCenter;
	}
	
	
	protected static Optional<Integer> getFloorHeight(RandomSource random, ChunkGenerator chunkGenerator, LevelHeightAccessor world, RandomState noiseConfig, BoundingBox box, IntProvider buryDepth) {
		int lowestY = world.getMinBuildHeight() + 12;
		
		int floorY = box.minY();
		int structureHeight = box.maxY() - box.minY();
		if (floorY > chunkGenerator.getBaseHeight(box.minX(), box.minZ(), Heightmap.Types.OCEAN_FLOOR_WG, world, noiseConfig) - structureHeight
				|| floorY > chunkGenerator.getBaseHeight(box.minX(), box.maxZ(), Heightmap.Types.OCEAN_FLOOR_WG, world, noiseConfig) - structureHeight
				|| floorY > chunkGenerator.getBaseHeight(box.maxZ(), box.minZ(), Heightmap.Types.OCEAN_FLOOR_WG, world, noiseConfig) - structureHeight
				|| floorY > chunkGenerator.getBaseHeight(box.maxZ(), box.maxZ(), Heightmap.Types.OCEAN_FLOOR_WG, world, noiseConfig) - structureHeight) {
			
			return Optional.empty();
		}
		
		// if we randomly picked a solid block:
		// search downwards until we find the first non-solid block
		// (so we do not place our structure in solid stone)
		NoiseColumn heightLimitView = chunkGenerator.getBaseColumn(box.getCenter().getX(), box.getCenter().getZ(), world, noiseConfig);
		do {
			if (floorY < lowestY) {
				return Optional.empty();
			}
			if (!heightLimitView.getBlock(floorY).isSolid()) {
				break;
			}
			floorY--;
		} while (true);
		
		// then search down until we find a position 
		// that matches the criteria of at least 2/4 corner blocks
		NoiseColumn[] verticalBlockSamples = new NoiseColumn[]{
				chunkGenerator.getBaseColumn(box.minX(), box.minZ(), world, noiseConfig),
				chunkGenerator.getBaseColumn(box.minX(), box.maxZ(), world, noiseConfig),
				chunkGenerator.getBaseColumn(box.maxX(), box.minZ(), world, noiseConfig),
				chunkGenerator.getBaseColumn(box.maxX(), box.maxZ(), world, noiseConfig)
		};
		
		Predicate<BlockState> blockPredicate = Heightmap.Types.OCEAN_FLOOR_WG.isOpaque();
		
		while (floorY >= lowestY) {
			int matchingBlocks = 0;
			for (NoiseColumn verticalBlockSample : verticalBlockSamples) {
				BlockState blockState = verticalBlockSample.getBlock(floorY);
				if (blockPredicate.test(blockState)) {
					matchingBlocks++;
					if (matchingBlocks == 2) {
						floorY -= buryDepth.sample(random);
						if (floorY < lowestY) {
							return Optional.empty();
						}
						return Optional.of(floorY);
					}
				}
			}
			floorY--;
		}
		
		return Optional.empty();
	}
	
}
