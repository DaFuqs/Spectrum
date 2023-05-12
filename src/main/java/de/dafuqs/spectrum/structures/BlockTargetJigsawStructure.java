package de.dafuqs.spectrum.structures;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.structure.pool.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.heightprovider.*;
import net.minecraft.world.gen.structure.*;

import java.util.*;

/**
 * A Jigsaw Structure that checks for a block predicate at the final position
 * Since JigsawStructure and it's properties are all final, we use our own implementation
 * This different jigsaw structure uses a BlockTargetType instead of a heightmap for its placement
 * Making it easier to place at a specific underground height ranges without generating in air
 */
public class BlockTargetJigsawStructure extends Structure {
	
	public static final Codec<BlockTargetJigsawStructure> CODEC = RecordCodecBuilder.<BlockTargetJigsawStructure>mapCodec((instance) ->
			instance.group(BlockTargetJigsawStructure.configCodecBuilder(instance),
					StructurePool.REGISTRY_CODEC.fieldOf("start_pool").forGetter((structure) -> structure.startPool),
					Identifier.CODEC.optionalFieldOf("start_jigsaw_name").forGetter((structure) -> structure.startJigsawName),
					Codec.intRange(0, 7).fieldOf("size").forGetter((structure) -> structure.size),
					HeightProvider.CODEC.fieldOf("start_height").forGetter((structure) -> structure.startHeight),
					BlockTargetType.CODEC.fieldOf("block_target_type").forGetter((structure) -> structure.blockTargetType),
					Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter((structure) -> structure.maxDistanceFromCenter)
			).apply(instance, BlockTargetJigsawStructure::new)).codec();
	
	protected final RegistryEntry<StructurePool> startPool;
	protected final Optional<Identifier> startJigsawName;
	protected final int size;
	protected final HeightProvider startHeight;
	protected final BlockTargetType blockTargetType;
	protected final int maxDistanceFromCenter;
	
	public BlockTargetJigsawStructure(Structure.Config config, RegistryEntry<StructurePool> startPool, Optional<Identifier> startJigsawName, int size, HeightProvider startHeight, BlockTargetType blockTargetType, int maxDistanceFromCenter) {
		super(config);
		this.startPool = startPool;
		this.startJigsawName = startJigsawName;
		this.size = size;
		this.startHeight = startHeight;
		this.blockTargetType = blockTargetType;
		this.maxDistanceFromCenter = maxDistanceFromCenter;
	}
	
	@Override
	public Optional<Structure.StructurePosition> getStructurePosition(Structure.Context context) {
		ChunkRandom chunkRandom = context.random();
		int x = context.chunkPos().getStartX() + chunkRandom.nextInt(16);
		int z = context.chunkPos().getStartZ() + chunkRandom.nextInt(16);
		HeightContext heightContext = new HeightContext(context.chunkGenerator(), context.world());
		int y = this.startHeight.get(chunkRandom, heightContext);
		VerticalBlockSample verticalBlockSample = context.chunkGenerator().getColumnSample(x, z, context.world(), context.noiseConfig());
		
		BlockState blockState = verticalBlockSample.getState(y);
		if (!this.blockTargetType.test(blockState)) {
			return Optional.empty();
		}
		
		BlockPos blockPos = new BlockPos(x, y, z);
		StructurePools.initDefaultPools(BuiltinRegistries.STRUCTURE_POOL);
		return StructurePoolBasedGenerator.generate(context, this.startPool, this.startJigsawName, this.size, blockPos, false, Optional.empty(), this.maxDistanceFromCenter);
	}
	
	@Override
	public StructureType<BlockTargetJigsawStructure> getType() {
		return SpectrumStructureTypes.BLOCK_PREDICATE_JIGSAW;
	}
	
}