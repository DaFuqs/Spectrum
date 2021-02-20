package de.dafuqs.pigment.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;


public class DeeperDownChunkGenerator extends ChunkGenerator {

    private final long seed;
    protected final ChunkGeneratorSettings settings;

    public static final Codec<DeeperDownChunkGenerator> CODEC = RecordCodecBuilder.create(
            (instance) -> instance.group(
                    BiomeSource.CODEC.fieldOf("biome_source").forGetter((surfaceChunkGenerator) -> surfaceChunkGenerator.biomeSource),
                    Codec.LONG.fieldOf("seed").stable().forGetter((surfaceChunkGenerator) -> surfaceChunkGenerator.seed),
                    ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings").forGetter((surfaceChunkGenerator) -> () -> surfaceChunkGenerator.settings))
                .apply(instance, instance.stable(DeeperDownChunkGenerator::new)));

    public DeeperDownChunkGenerator(BiomeSource biomeSource, long l, Supplier<ChunkGeneratorSettings> chunkGeneratorType) {
        this(biomeSource, biomeSource, l, chunkGeneratorType.get());
    }

    private DeeperDownChunkGenerator(BiomeSource biomeSource, BiomeSource biomeSource2, long seed, ChunkGeneratorSettings chunkGeneratorType) {
        super(biomeSource, biomeSource2, new StructuresConfig(Optional.empty(), Collections.emptyMap()), seed); // no structures
        this.seed = seed;
        this.settings = chunkGeneratorType;
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ChunkGenerator withSeed(long seed) {
        return new DeeperDownChunkGenerator(this.biomeSource.withSeed(seed), seed, () -> this.settings);
    }

    // TODO
    @Override
    public void buildSurface(ChunkRegion region, Chunk chunk) {

    }

    @Override
    public CompletableFuture<Chunk> populateNoise(Executor executor, StructureAccessor accessor, Chunk chunk) {
        return null;
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world) {
        return 0;
    }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world) {
        return null;
    }

        /*@Override
    public int getHeight(int x, int z, Heightmap.Type heightmapType) {
        return FLOOR_HEIGHT;
    }

    @Override
    public BlockView getColumnSample(int x, int z) {
        BlockState[] states = new BlockState[256];
        Arrays.fill(states, Blocks.AIR.getDefaultState());
        return new VerticalBlockSample(states);
    }

        @Override
    public void populateNoise(WorldAccess world, StructureAccessor accessor, Chunk chunk) {
        // generate spheres
        placeSpheroids(chunk);
    }


    @Override
    public int getWorldHeight() {
        return StarrySkyCommon.starryWorld.getHeight();
    }

    @Override
    public void populateEntities(ChunkRegion chunkRegion) {
        int xChunk = chunkRegion.getCenterChunkX();
        int zChunk = chunkRegion.getCenterChunkZ();

        List<Spheroid> localSystem = systemGenerator.getSystemAtChunkPos(xChunk, zChunk);
        ChunkRandom chunkRandom = new ChunkRandom();
        chunkRandom.setPopulationSeed(chunkRegion.getSeed(), xChunk, zChunk);

        ChunkPos chunkPos = new ChunkPos(xChunk, zChunk);
        for(Spheroid spheroid : localSystem) {
            spheroid.populateEntities(chunkPos, chunkRegion, chunkRandom);
        }
    }

    @Override
    public int getSeaLevel() {
        return FLOOR_HEIGHT;
    }
*/

}