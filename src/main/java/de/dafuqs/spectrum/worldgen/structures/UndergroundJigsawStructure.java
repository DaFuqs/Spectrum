package de.dafuqs.spectrum.worldgen.structures;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.*;
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
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class UndergroundJigsawStructure extends UndergroundStructure {
	
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
	
	public UndergroundJigsawStructure(Structure.StructureSettings config, Holder<StructureTemplatePool> startPool, Optional<ResourceLocation> startJigsawName, Integer size, HeightProvider startHeight,
									  IntProvider buryDepth, Integer placementCheckWidth, Integer placementCheckHeight, Integer maxDistanceFromCenter) {
		
		super(config, size, startHeight, buryDepth, placementCheckWidth, placementCheckHeight, maxDistanceFromCenter);
		this.startPool = startPool;
		this.startJigsawName = startJigsawName;
	}
	
	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
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
	public StructureType<?> type() {
		return SpectrumStructureTypes.UNDERGROUND_JIGSAW;
	}
}
