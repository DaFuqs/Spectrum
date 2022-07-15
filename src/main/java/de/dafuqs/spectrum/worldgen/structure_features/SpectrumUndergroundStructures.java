package de.dafuqs.spectrum.worldgen.structure_features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.heightprovider.HeightProvider;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Optional;

// thank you, @TelepathicGrunt
// source: https://github.com/TelepathicGrunt/StructureTutorialMod/blob/1.18.2-Fabric-Jigsaw/src/main/java/com/telepathicgrunt/structure_tutorial/structures/SkyStructures.java
public class SpectrumUndergroundStructures extends Structure {
	public static final Codec<SpectrumUndergroundStructures> CODEC = RecordCodecBuilder.<SpectrumUndergroundStructures>mapCodec(instance ->
			instance.group(SpectrumUndergroundStructures.configCodecBuilder(instance),
					StructurePool.REGISTRY_CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
					Identifier.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName),
					Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size),
					HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
					Heightmap.Type.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap),
					Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter)
			).apply(instance, SpectrumUndergroundStructures::new)).codec();

	private final RegistryEntry<StructurePool> startPool;
	private final Optional<Identifier> startJigsawName;
	private final int size;
	private final HeightProvider startHeight;
	private final Optional<Heightmap.Type> projectStartToHeightmap;
	private final int maxDistanceFromCenter;
	public SpectrumUndergroundStructures(Structure.Config config, RegistryEntry<StructurePool> startPool, Optional<Identifier> startJigsawName, int size, HeightProvider startHeight, Optional<Heightmap.Type> projectStartToHeightmap, int maxDistanceFromCenter) {
		super(config);
		this.startPool = startPool;
		this.startJigsawName = startJigsawName;
		this.size = size;
		this.startHeight = startHeight;
		this.projectStartToHeightmap = projectStartToHeightmap;
		this.maxDistanceFromCenter = maxDistanceFromCenter;
	}

	private static boolean extraSpawningChecks(Structure.Context context) {
		// Grabs the chunk position we are at
		ChunkPos chunkpos = context.chunkPos();

		// Checks to make sure our structure does not spawn above land that's higher than y = 150
		// to demonstrate how this method is good for checking extra conditions for spawning
		return context.chunkGenerator().getHeightInGround(
				chunkpos.getStartX(),
				chunkpos.getStartZ(),
				Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
				context.world(),
				context.noiseConfig()) < 150;
	}

	@Override
	public Optional<Structure.StructurePosition> getStructurePosition(Structure.Context context) {

		// Check if the spot is valid for our structure. This is just as another method for cleanness.
		// Returning an empty optional tells the game to skip this spot as it will not generate the structure.
		if (!SpectrumUndergroundStructures.extraSpawningChecks(context)) {
			return Optional.empty();
		}

		// Set's our spawning blockpos's y offset to be 60 blocks up.
		// Since we are going to have heightmap/terrain height spawning set to true further down, this will make it so we spawn 60 blocks above terrain.
		// If we wanted to spawn on ocean floor, we would set heightmap/terrain height spawning to false and the grab the y value of the terrain with OCEAN_FLOOR_WG heightmap.
		int startY = this.startHeight.get(context.random(), new HeightContext(context.chunkGenerator(), context.world()));

		// Turns the chunk coordinates into actual coordinates we can use. (Gets corner of that chunk)
		ChunkPos chunkPos = context.chunkPos();
		BlockPos blockPos = new BlockPos(chunkPos.getStartX(), startY, chunkPos.getStartZ());

		Optional<StructurePosition> structurePiecesGenerator =
				StructurePoolBasedGenerator.generate(
						context, // Used for StructurePoolBasedGenerator to get all the proper behaviors done.
						this.startPool, // The starting pool to use to create the structure layout from
						this.startJigsawName, // Can be used to only spawn from one Jigsaw block. But we don't need to worry about this.
						this.size, // How deep a branch of pieces can go away from center piece. (5 means branches cannot be longer than 5 pieces from center piece)
						blockPos, // Where to spawn the structure.
						false, // "useExpansionHack" This is for legacy villages to generate properly. You should keep this false always.
						this.projectStartToHeightmap, // Adds the terrain height's y value to the passed in blockpos's y value. (This uses WORLD_SURFACE_WG heightmap which stops at top water too)
						// Here, blockpos's y value is 60 which means the structure spawn 60 blocks above terrain height.
						// Set this to false for structure to be place only at the passed in blockpos's Y value instead.
						// Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.
						this.maxDistanceFromCenter); // Maximum limit for how far pieces can spawn from center. You cannot set this bigger than 128 or else pieces gets cutoff.

		/*
		 * Note, you are always free to make your own StructurePoolBasedGenerator class and implementation of how the structure
		 * should generate. It is tricky but extremely powerful if you are doing something that vanilla's jigsaw system cannot do.
		 * Such as for example, forcing 3 pieces to always spawn every time, limiting how often a piece spawns, or remove the intersection limitation of pieces.
		 */

		// Return the pieces generator that is now set up so that the game runs it when it needs to create the layout of structure pieces.
		return structurePiecesGenerator;
	}


	@Override
	public StructureType<?> getType() {
		return SpectrumStructureFeatures.UNDERGROUND_STRUCTURES; // Helps the game know how to turn this structure back to json to save to chunks
	}

		/*
	}
	public SpectrumUndergroundStructures() {
		super(SpectrumUndergroundStructurePoolFeatureConfig.CODEC, SpectrumUndergroundStructures::createPiecesGenerator, PostPlacementProcessor.EMPTY);
	}

		 */

	/*
	 * This is where extra checks can be done to determine if the structure can spawn here.
	 * This only needs to be overridden if you're adding additional spawn conditions.
	 *
	 * Fun fact, if you set your structure separation/spacing to be 0/1, you can use
	 * isFeatureChunk to return true only if certain chunk coordinates are passed in
	 * which allows you to spawn structures only at certain coordinates in the world.
	 *
	 * Basically, this method is used for determining if the land is at a suitable height,
	 * if certain other structures are too close or not, or some other restrictive condition.
	 *
	 * For example, Pillager Outposts added a check to make sure it cannot spawn within 10 chunk of a Village.
	 * (Bedrock Edition seems to not have the same check)
	 *
	 * If you are doing Nether structures, you'll probably want to spawn your structure on top of ledges.
	 * Best way to do that is to use getBaseColumn to grab a column of blocks at the structure's x/z position.
	 * Then loop through it and look for land with air above it and set blockpos's Y value to it.
	 * Make sure to set the final boolean in JigsawPlacement.addPieces to false so
	 * that the structure spawns at blockpos's y value instead of placing the structure on the Bedrock roof!
	 *
	 * Also, please for the love of god, do not do dimension checking here.
	 * If you do and another mod's dimension is trying to spawn your structure,
	 * the locate command will make minecraft hang forever and break the game.
	 * Use the biome tags for where to spawn the structure and users can datapack
	 * it to spawn in specific biomes that aren't in the dimension they don't like if they wish.
	 */
	/*
	private static boolean isFeatureChunk(StructureGeneratorFactory.Context<SpectrumUndergroundStructurePoolFeatureConfig> context) {
		// Checks to make sure our structure does not spawn within 10 chunks of an Ocean Monument
		// to demonstrate how this method is good for checking extra conditions for spawning
		return true;
	}

	public static Optional<StructurePiecesGenerator<SpectrumUndergroundStructurePoolFeatureConfig>> createPiecesGenerator(StructureGeneratorFactory.Context<SpectrumUndergroundStructurePoolFeatureConfig> context) {
		// Check if the spot is valid for our structure. This is just as another method for cleanness.
		// Returning an empty optional tells the game to skip this spot as it will not generate the structure.
		if (!isFeatureChunk(context)) {
			return Optional.empty();
		}

		// Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
		BlockPos blockpos = context.chunkPos().getCenterAtY(0);

		// Find the top Y value of the land and then offset our structure to 60 blocks above that.
		// WORLD_SURFACE_WG will stop at top water, so we don't accidentally put our structure into the ocean if it is a super deep ocean.
		int topLandY = context.chunkGenerator().getHeightOnGround(blockpos.getX(), blockpos.getZ(), Heightmap.Type.WORLD_SURFACE_WG, context.world());

		// calculate a random height like specified in the config
		Random random = new Random(context.seed());
		int height = (int) (context.config().minY + context.config().maxY * random.nextFloat());
		HeightLimitView world = context.world();
		if (height < world.getBottomY() + 16 || height > topLandY - 16) {
			return Optional.empty();
		}

		blockpos = blockpos.withY(height);

		StructureGeneratorFactory.Context<SpectrumUndergroundStructurePoolFeatureConfig> c = context;

		// Return the pieces generator that is now set up so that the game runs it when it needs to create the layout of structure pieces.
		return SpectrumStructurePoolBasedGenerator.generate(
				c, // Used for JigsawPlacement to get all the proper behaviors done.
				PoolStructurePiece::new, // Needed in order to create a list of jigsaw pieces when making the structure's layout.
				blockpos, // Position of the structure. Y value is ignored if last parameter is set to true.
				false,  // Special boundary adjustments for villages. It's... hard to explain. Keep this false and make your pieces not be partially intersecting.
				// Either not intersecting or fully contained will make children pieces spawn just fine. It's easier that way.
				false // Place at heightmap (top land). Set this to false for structure to be place at the passed in blockpos's Y value instead.
				// Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.
		);
	}

	 */
}