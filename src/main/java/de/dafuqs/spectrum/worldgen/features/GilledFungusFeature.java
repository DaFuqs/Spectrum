package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.*;

public class GilledFungusFeature extends Feature<GilledFungusFeatureConfig> {

    public GilledFungusFeature(Codec<GilledFungusFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(FeatureContext<GilledFungusFeatureConfig> context) {
        StructureWorldAccess structureWorldAccess = context.getWorld();
        BlockPos blockPos = context.getOrigin();
        GilledFungusFeatureConfig hugeFungusFeatureConfig = context.getConfig();
        Block validBaseBlock = hugeFungusFeatureConfig.validBaseBlock.getBlock();
        BlockState baseBlock = structureWorldAccess.getBlockState(blockPos.down());

        if (!baseBlock.isOf(validBaseBlock)) {
            return false;
        }

        Random random = context.getRandom();
        ChunkGenerator chunkGenerator = context.getGenerator();

        int stemHeight = MathHelper.nextInt(random, 4, 9);
        if (random.nextInt(12) == 0) {
            stemHeight *= 2;
        }

        int j = chunkGenerator.getWorldHeight();
        if (blockPos.getY() + stemHeight + 1 >= j) {
            return false;
        }

        structureWorldAccess.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 4);
        this.generateStem(structureWorldAccess, hugeFungusFeatureConfig, blockPos, stemHeight);
        this.generateHat(structureWorldAccess, random, hugeFungusFeatureConfig, blockPos, stemHeight);
        return true;
    }

    private static boolean isReplaceable(WorldAccess world, BlockPos pos, boolean replacePlants) {
        return world.testBlockState(pos, (state) -> {
            Material material = state.getMaterial();
            return state.getMaterial().isReplaceable() || replacePlants && material == Material.PLANT;
        });
    }

    private void generateStem(WorldAccess world, GilledFungusFeatureConfig config, BlockPos pos, int stemHeight) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        BlockState blockState = config.stemState;
        int i = 0;

        for (int j = -i; j <= i; ++j) {
            for (int k = -i; k <= i; ++k) {
                for (int l = 0; l < stemHeight; ++l) {
                    mutable.set(pos, j, l, k);
                    if (isReplaceable(world, mutable, true)) {
                        this.setBlockState(world, mutable, blockState);
                    }
                }
            }
        }
    }

    private void generateHat(WorldAccess world, Random random, GilledFungusFeatureConfig config, BlockPos pos, int hatHeight) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int i = Math.min(random.nextInt(1 + hatHeight / 3) + 5, hatHeight);
        int j = hatHeight - i;

        for (int k = j; k <= hatHeight; ++k) {
            int l = k < hatHeight - random.nextInt(3) ? 2 : 1;
            if (i > 8 && k < j + 4) {
                l = 3;
            }

            for (int m = -l; m <= l; ++m) {
                for (int n = -l; n <= l; ++n) {
                    boolean bl2 = m == -l || m == l;
                    boolean bl3 = n == -l || n == l;
                    boolean bl4 = !bl2 && !bl3 && k != hatHeight;
                    boolean bl5 = bl2 && bl3;
                    boolean bl6 = k < j + 3;
                    mutable.set(pos, m, k, n);
                    if (isReplaceable(world, mutable, false)) {
                        if (bl6) {
                            if (!bl4) {
                                this.placeWithOptionalVines(world, random, mutable, config.capState);
                            }
                        } else if (bl4) {
                            this.placeHatBlock(world, random, config, mutable, 0.1F, 0.2F);
                        } else if (bl5) {
                            this.placeHatBlock(world, random, config, mutable, 0.01F, 0.7F);
                        } else {
                            this.placeHatBlock(world, random, config, mutable, 5.0E-4F, 0.98F);
                        }
                    }
                }
            }
        }

    }

    private void placeHatBlock(WorldAccess world, Random random, GilledFungusFeatureConfig config, BlockPos.Mutable pos, float decorationChance, float generationChance) {
        if (random.nextFloat() < decorationChance) {
            this.setBlockState(world, pos, config.gillsState);
        } else if (random.nextFloat() < generationChance) {
            this.setBlockState(world, pos, config.capState);
        }

    }

    private void placeWithOptionalVines(WorldAccess world, Random random, BlockPos pos, BlockState state) {
        if (world.getBlockState(pos.down()).isOf(state.getBlock())) {
            this.setBlockState(world, pos, state);
        } else if ((double) random.nextFloat() < 0.15) {
            this.setBlockState(world, pos, state);
        }

    }

}
