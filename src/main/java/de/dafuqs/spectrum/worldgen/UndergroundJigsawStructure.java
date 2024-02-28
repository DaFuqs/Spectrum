package de.dafuqs.spectrum.worldgen;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.registry.entry.*;
import net.minecraft.structure.pool.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.heightprovider.*;
import net.minecraft.world.gen.noise.*;
import net.minecraft.world.gen.structure.*;

import java.util.*;
import java.util.function.*;

/**
 * A Jigsaw Structure that has more control over where it can be placed (VerticalPlacement)
 * Since JigsawStructure and its properties are all final, we use our own implementation
 * This different jigsaw structure uses the chunk generator sample instead of a heightmap for its placement
 * Making it easier to place at a position that matches a certain condition
 */
public class UndergroundJigsawStructure extends Structure {
	
	public static final Codec<UndergroundJigsawStructure> CODEC = RecordCodecBuilder.<UndergroundJigsawStructure>mapCodec((instance) ->
			instance.group(UndergroundJigsawStructure.configCodecBuilder(instance),
					StructurePool.REGISTRY_CODEC.fieldOf("start_pool").forGetter((structure) -> structure.startPool),
					Identifier.CODEC.optionalFieldOf("start_jigsaw_name").forGetter((structure) -> structure.startJigsawName),
					Codec.intRange(0, 7).fieldOf("size").forGetter((structure) -> structure.size),
					HeightProvider.CODEC.fieldOf("start_height").forGetter((structure) -> structure.startHeight),
					IntProvider.NON_NEGATIVE_CODEC.fieldOf("bury_depth").forGetter((structure) -> structure.buryDepth),
					Codec.intRange(0, 64).fieldOf("placement_check_width").forGetter((structure) -> structure.placementCheckWidth),
					Codec.intRange(0, 64).fieldOf("placement_check_height").forGetter((structure) -> structure.placementCheckHeight),
					Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter((structure) -> structure.maxDistanceFromCenter)
			).apply(instance, UndergroundJigsawStructure::new)).codec();
	
	protected final RegistryEntry<StructurePool> startPool;
	protected final Optional<Identifier> startJigsawName;
	protected final int size;
	protected final int placementCheckWidth;
	protected final int placementCheckHeight;
	protected final HeightProvider startHeight;
	protected final IntProvider buryDepth;
	protected final int maxDistanceFromCenter;
	
	public UndergroundJigsawStructure(Structure.Config config, RegistryEntry<StructurePool> startPool, Optional<Identifier> startJigsawName, Integer size, HeightProvider startHeight,
									  IntProvider buryDepth, Integer placementCheckWidth, Integer placementCheckHeight, Integer maxDistanceFromCenter) {
		
		super(config);
		this.startPool = startPool;
		this.startJigsawName = startJigsawName;
		this.size = size;
		this.startHeight = startHeight;
		this.buryDepth = buryDepth;
		this.placementCheckWidth = placementCheckWidth;
		this.placementCheckHeight = placementCheckHeight;
		this.maxDistanceFromCenter = maxDistanceFromCenter;
	}
	
	@Override
	public Optional<Structure.StructurePosition> getStructurePosition(Structure.Context context) {
		ChunkRandom chunkRandom = context.random();
		HeightContext heightContext = new HeightContext(context.chunkGenerator(), context.world());
		
		int x = context.chunkPos().getStartX() + chunkRandom.nextInt(16);
		int z = context.chunkPos().getStartZ() + chunkRandom.nextInt(16);
		int y = this.startHeight.get(chunkRandom, heightContext);
		
		ChunkGenerator chunkGenerator = context.chunkGenerator();
		HeightLimitView world = context.world();
		NoiseConfig noiseConfig = context.noiseConfig();
		
		BlockBox structureBox = BlockBox.create(
				new BlockPos(x - placementCheckWidth / 2, y, z - placementCheckWidth / 2),
				new BlockPos(x + placementCheckWidth / 2, y + placementCheckHeight, z + placementCheckWidth / 2)
		);
		Optional<Integer> floorHeight = getFloorHeight(chunkRandom, chunkGenerator, world, noiseConfig, structureBox, buryDepth);
		if (floorHeight.isEmpty()) {
			return Optional.empty();
		}
		
		return StructurePoolBasedGenerator.generate(context, this.startPool, this.startJigsawName, this.size, new BlockPos(x, floorHeight.get(), z), false, Optional.empty(), this.maxDistanceFromCenter);
	}
	
	@Override
	public StructureType<UndergroundJigsawStructure> getType() {
		return SpectrumStructureTypes.UNDERGROUND_JIGSAW;
	}
	
	private static Optional<Integer> getFloorHeight(Random random, ChunkGenerator chunkGenerator, HeightLimitView world, NoiseConfig noiseConfig, BlockBox box, IntProvider buryDepth) {
		int lowestY = world.getBottomY() + 12;
		
		int floorY = box.getMinY();
		int structureHeight = box.getMaxY() - box.getMinY();
		if (floorY > chunkGenerator.getHeight(box.getMinX(), box.getMinZ(), Heightmap.Type.OCEAN_FLOOR_WG, world, noiseConfig) - structureHeight
				|| floorY > chunkGenerator.getHeight(box.getMinX(), box.getMaxZ(), Heightmap.Type.OCEAN_FLOOR_WG, world, noiseConfig) - structureHeight
				|| floorY > chunkGenerator.getHeight(box.getMaxZ(), box.getMinZ(), Heightmap.Type.OCEAN_FLOOR_WG, world, noiseConfig) - structureHeight
				|| floorY > chunkGenerator.getHeight(box.getMaxZ(), box.getMaxZ(), Heightmap.Type.OCEAN_FLOOR_WG, world, noiseConfig) - structureHeight) {
			
			return Optional.empty();
		}
		
		// if we are randomly picked a solid block:
		// search downwards until we find the first non-solid block
		// (so we do not place our structure in solid stone)
		VerticalBlockSample heightLimitView = chunkGenerator.getColumnSample(box.getCenter().getX(), box.getCenter().getZ(), world, noiseConfig);
		do {
			if (floorY < lowestY) {
				return Optional.empty();
			}
			if (!heightLimitView.getState(floorY).isSolid()) {
				break;
			}
			floorY--;
		} while (true);
		
		// then search down until we find a position 
		// that matches the criteria of at least 3/4 corner blocks
		VerticalBlockSample[] verticalBlockSamples = new VerticalBlockSample[]{
				chunkGenerator.getColumnSample(box.getMinX(), box.getMinZ(), world, noiseConfig),
				chunkGenerator.getColumnSample(box.getMinX(), box.getMaxZ(), world, noiseConfig),
				chunkGenerator.getColumnSample(box.getMaxX(), box.getMinZ(), world, noiseConfig),
				chunkGenerator.getColumnSample(box.getMaxX(), box.getMaxZ(), world, noiseConfig)
		};
		
		Predicate<BlockState> blockPredicate = Heightmap.Type.OCEAN_FLOOR_WG.getBlockPredicate();
		
		while (floorY >= lowestY) {
			int matchingBlocks = 0;
			for (VerticalBlockSample verticalBlockSample : verticalBlockSamples) {
				BlockState blockState = verticalBlockSample.getState(floorY);
				if (blockPredicate.test(blockState)) {
					matchingBlocks++;
					if (matchingBlocks == 3) {
						floorY -= buryDepth.get(random);
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