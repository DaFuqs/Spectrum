package de.dafuqs.pigment.dimension;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import java.util.Random;

public class DeeperDownSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
   public DeeperDownSurfaceBuilder(Codec<TernarySurfaceConfig> codec) {
      super(codec);
   }

   @Override
   public void generate(Random random, Chunk chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int i, long l, TernarySurfaceConfig surfaceConfig) {
      if (noise > 1.0D) {
         SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, seaLevel, i, l, SurfaceBuilder.STONE_CONFIG);
      } else {
         SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, seaLevel, i, l, SurfaceBuilder.GRASS_CONFIG);
      }
   }

}
