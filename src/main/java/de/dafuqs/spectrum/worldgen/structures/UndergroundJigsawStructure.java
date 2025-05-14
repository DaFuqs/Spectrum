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
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class UndergroundJigsawStructure extends Structure {
	
	public static final MapCodec<UndergroundJigsawStructure> CODEC = RecordCodecBuilder.mapCodec((instance) ->
			instance.group(UndergroundJigsawStructure.settingsCodec(instance),
					StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter((structure) -> structure.startPool),
					ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter((structure) -> structure.startJigsawName),
					Codec.intRange(0, 7).fieldOf("size").forGetter((structure) -> structure.size),
					HeightProvider.CODEC.fieldOf("start_height").forGetter((structure) -> structure.startHeight),
					IntProvider.NON_NEGATIVE_CODEC.fieldOf("bury_depth").forGetter((structure) -> structure.buryDepth),
					Codec.intRange(0, 64).fieldOf("placement_check_width").forGetter((structure) -> structure.placementCheckWidth),
					Codec.intRange(0, 64).fieldOf("placement_check_height").forGetter((structure) -> structure.placementCheckHeight),
					Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter((structure) -> structure.maxDistanceFromCenter)
			).apply(instance, UndergroundJigsawStructure::new));
	
	protected final Holder<StructureTemplatePool> startPool;
	protected final Optional<ResourceLocation> startJigsawName;
	protected final int size;
	protected final int placementCheckWidth;
	protected final int placementCheckHeight;
	protected final HeightProvider startHeight;
	protected final IntProvider buryDepth;
	protected final int maxDistanceFromCenter;
	
	public UndergroundJigsawStructure(Structure.StructureSettings config, Holder<StructureTemplatePool> startPool, Optional<ResourceLocation> startJigsawName, Integer size, HeightProvider startHeight,
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
	public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
		WorldgenRandom chunkRandom = context.random();
		WorldGenerationContext heightContext = new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor());
		
		int x = context.chunkPos().getMinBlockX() + chunkRandom.nextInt(16);
		int z = context.chunkPos().getMinBlockZ() + chunkRandom.nextInt(16);
		int y = this.startHeight.sample(chunkRandom, heightContext);
		
		ChunkGenerator chunkGenerator = context.chunkGenerator();
		LevelHeightAccessor world = context.heightAccessor();
		RandomState noiseConfig = context.randomState();
		
		BoundingBox structureBox = BoundingBox.fromCorners(
				new BlockPos(x - placementCheckWidth / 2, y, z - placementCheckWidth / 2),
				new BlockPos(x + placementCheckWidth / 2, y + placementCheckHeight, z + placementCheckWidth / 2)
		);
		Optional<Integer> floorHeight = getFloorHeight(chunkRandom, chunkGenerator, world, noiseConfig, structureBox, buryDepth);
		if (floorHeight.isEmpty()) {
			return Optional.empty();
		}
		
		return JigsawPlacement.addPieces(
				context,
				this.startPool,
				this.startJigsawName,
				this.size,
				new BlockPos(x, floorHeight.get(), z),
				false,
				Optional.empty(),
				this.maxDistanceFromCenter,
				PoolAliasLookup.EMPTY,
				JigsawStructure.DEFAULT_DIMENSION_PADDING,
				JigsawStructure.DEFAULT_LIQUID_SETTINGS);
	}
	
	@Override
	public StructureType<UndergroundJigsawStructure> type() {
		return SpectrumStructureTypes.UNDERGROUND_JIGSAW;
	}
	
	private static Optional<Integer> getFloorHeight(RandomSource random, ChunkGenerator chunkGenerator, LevelHeightAccessor world, RandomState noiseConfig, BoundingBox box, IntProvider buryDepth) {
		int lowestY = world.getMinBuildHeight() + 12;
		
		int floorY = box.minY();
		int structureHeight = box.maxY() - box.minY();
		if (floorY > chunkGenerator.getBaseHeight(box.minX(), box.minZ(), Heightmap.Types.OCEAN_FLOOR_WG, world, noiseConfig) - structureHeight
				|| floorY > chunkGenerator.getBaseHeight(box.minX(), box.maxZ(), Heightmap.Types.OCEAN_FLOOR_WG, world, noiseConfig) - structureHeight
				|| floorY > chunkGenerator.getBaseHeight(box.maxZ(), box.minZ(), Heightmap.Types.OCEAN_FLOOR_WG, world, noiseConfig) - structureHeight
				|| floorY > chunkGenerator.getBaseHeight(box.maxZ(), box.maxZ(), Heightmap.Types.OCEAN_FLOOR_WG, world, noiseConfig) - structureHeight) {
			
			return Optional.empty();
		}
		
		// if we are randomly picked a solid block:
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
		// that matches the criteria of at least 3/4 corner blocks
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
					if (matchingBlocks == 3) {
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
