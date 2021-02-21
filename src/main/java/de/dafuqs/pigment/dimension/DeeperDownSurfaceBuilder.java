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

   public void generate(Random random, Chunk chunk, Biome biome, int i, int j, int k, double d, BlockState blockState, BlockState blockState2, int l, long m, TernarySurfaceConfig ternarySurfaceConfig) {
      if (d > 1.0D) {
         SurfaceBuilder.DEFAULT.generate(random, chunk, biome, i, j, k, d, blockState, blockState2, l, m, SurfaceBuilder.STONE_CONFIG);
      } else {
         SurfaceBuilder.DEFAULT.generate(random, chunk, biome, i, j, k, d, blockState, blockState2, l, m, SurfaceBuilder.GRASS_CONFIG);
      }

   }
}
