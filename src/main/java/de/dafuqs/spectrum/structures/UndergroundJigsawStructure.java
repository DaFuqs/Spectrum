package de.dafuqs.spectrum.structures;

import com.google.common.collect.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.structure.pool.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.random.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.heightprovider.*;
import net.minecraft.world.gen.noise.*;
import net.minecraft.world.gen.structure.*;

import java.util.*;

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
					StructurePlacementType.CODEC.fieldOf("placement_type").forGetter((structure) -> structure.structurePlacementType),
					Codec.intRange(0, 64).fieldOf("placement_check_width").forGetter((structure) -> structure.placementCheckWidth),
					Codec.intRange(0, 64).fieldOf("placement_check_height").forGetter((structure) -> structure.placementCheckHeight),
					Codec.BOOL.fieldOf("placement_required").forGetter((structure) -> structure.placementRequired),
					Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter((structure) -> structure.maxDistanceFromCenter)
			).apply(instance, UndergroundJigsawStructure::new)).codec();
	
	protected final RegistryEntry<StructurePool> startPool;
	protected final Optional<Identifier> startJigsawName;
	protected final int size;
	protected final int placementCheckWidth;
	protected final int placementCheckHeight;
	protected final boolean placementRequired;
	protected final HeightProvider startHeight;
	protected final StructurePlacementType structurePlacementType;
	protected final int maxDistanceFromCenter;
	
	public UndergroundJigsawStructure(Structure.Config config, RegistryEntry<StructurePool> startPool, Optional<Identifier> startJigsawName, Integer size, HeightProvider startHeight,
									  StructurePlacementType structurePlacementType, int placementCheckWidth, int placementCheckHeight, boolean placementRequired, Integer maxDistanceFromCenter) {
		
		super(config);
		this.startPool = startPool;
		this.startJigsawName = startJigsawName;
		this.size = size;
		this.startHeight = startHeight;
		this.structurePlacementType = structurePlacementType;
		this.placementCheckWidth = placementCheckWidth;
		this.placementCheckHeight = placementCheckHeight;
		this.placementRequired = placementRequired;
		this.maxDistanceFromCenter = maxDistanceFromCenter;
	}
	
	@Override
	public Optional<Structure.StructurePosition> getStructurePosition(Structure.Context context) {
		ChunkRandom chunkRandom = context.random();
		int x = context.chunkPos().getStartX() + chunkRandom.nextInt(16);
		int z = context.chunkPos().getStartZ() + chunkRandom.nextInt(16);
		HeightContext heightContext = new HeightContext(context.chunkGenerator(), context.world());
		int y = this.startHeight.get(chunkRandom, heightContext);
		
		ChunkGenerator chunkGenerator = context.chunkGenerator();
		HeightLimitView world = context.world();
		NoiseConfig noiseConfig = context.noiseConfig();
		
		BlockBox structureBox = BlockBox.create(
				new BlockPos(x - placementCheckWidth / 2, y, z - placementCheckWidth / 2),
				new BlockPos(x + placementCheckWidth / 2, y + placementCheckHeight, z + placementCheckWidth / 2)
		);
		Optional<Integer> floorHeight = getFloorHeight(chunkRandom, chunkGenerator, structurePlacementType, y, placementCheckHeight, structureBox, world, noiseConfig);
		if (floorHeight.isPresent()) {
			y = floorHeight.get();
		} else if (!placementRequired) {
			return Optional.empty();
		}
		
		return StructurePoolBasedGenerator.generate(context, this.startPool, this.startJigsawName, this.size, new BlockPos(x, y, z), false, Optional.empty(), this.maxDistanceFromCenter);
	}
	
	@Override
	public StructureType<UndergroundJigsawStructure> getType() {
		return SpectrumStructureTypes.UNDERGROUND_JIGSAW;
	}
	
	private static Optional<Integer> getFloorHeight(Random random, ChunkGenerator chunkGenerator, StructurePlacementType structurePlacementType, int startHeight, int structureHeight, BlockBox box, HeightLimitView world, NoiseConfig noiseConfig) {
		int y = startHeight;
		int lowestY = world.getBottomY() + 15;
		
		// if we are randomly picked a solid block:
		// search downwards until we find the first non-solid block
		// (so we do not place our structure in solid stone)
		VerticalBlockSample heightLimitView = chunkGenerator.getColumnSample(box.getCenter().getX(), box.getCenter().getZ(), world, noiseConfig);
		do {
			if (y < lowestY) {
				return Optional.empty();
			}
			if (!heightLimitView.getState(y).getMaterial().isSolid()) {
				break;
			}
			y--;
		} while (true);
		
		// then search down until we find a position 
		// that matches the criteria of at least 3/4 corner blocks
		List<BlockPos> boxCorners = ImmutableList.of(new BlockPos(box.getMinX(), 0, box.getMinZ()), new BlockPos(box.getMaxX(), 0, box.getMinZ()), new BlockPos(box.getMinX(), 0, box.getMaxZ()), new BlockPos(box.getMaxX(), 0, box.getMaxZ()));
		List<VerticalBlockSample> verticalBlockSamples = boxCorners.stream().map((blockPos) -> chunkGenerator.getColumnSample(blockPos.getX(), blockPos.getZ(), world, noiseConfig)).toList();
		Heightmap.Type type = structurePlacementType == StructurePlacementType.ON_GROUND_WATER ? Heightmap.Type.OCEAN_FLOOR_WG : Heightmap.Type.WORLD_SURFACE_WG;
		
		int result;
		for (result = y; result > lowestY; --result) {
			int m = 0;
			for (VerticalBlockSample verticalBlockSample : verticalBlockSamples) {
				BlockState blockState = verticalBlockSample.getState(result);
				if (type.getBlockPredicate().test(blockState)) {
					++m;
					if (m == 3) {
						if (structurePlacementType == StructurePlacementType.PARTLY_BURIED) {
							y = -MathHelper.nextBetween(random, structureHeight / 8, structureHeight / 2);
						}
						return Optional.of(result);
					}
				}
			}
		}
		
		return Optional.of(result);
	}
	
}