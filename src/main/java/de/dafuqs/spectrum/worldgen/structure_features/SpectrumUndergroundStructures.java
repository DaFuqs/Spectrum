package de.dafuqs.spectrum.worldgen.structure_features;

import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.PostPlacementProcessor;
import net.minecraft.structure.StructureGeneratorFactory;
import net.minecraft.structure.StructurePiecesGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Optional;
import java.util.Random;

// thank you, @TelepathicGrunt
// source: https://github.com/TelepathicGrunt/StructureTutorialMod/blob/1.18.2-Fabric-Jigsaw/src/main/java/com/telepathicgrunt/structure_tutorial/structures/SkyStructures.java
public class SpectrumUndergroundStructures extends StructureFeature<SpectrumUndergroundStructurePoolFeatureConfig> {
	
	public SpectrumUndergroundStructures() {
		super(SpectrumUndergroundStructurePoolFeatureConfig.CODEC, SpectrumUndergroundStructures::createPiecesGenerator, PostPlacementProcessor.EMPTY);
	}
	
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
}