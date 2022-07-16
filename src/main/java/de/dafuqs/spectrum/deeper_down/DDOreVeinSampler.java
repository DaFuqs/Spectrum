package de.dafuqs.spectrum.deeper_down;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.random.AbstractRandom;
import net.minecraft.world.gen.random.RandomDeriver;

public final class DDOreVeinSampler {
    private static final float field_36620 = 0.4F;
    private static final int field_36621 = 20;
    private static final double field_36622 = 0.2D;
    private static final float field_36623 = 0.7F;
    private static final float field_36624 = 0.1F;
    private static final float field_36625 = 0.3F;
    private static final float field_36626 = 0.6F;
    private static final float RAW_ORE_BLOCK_CHANCE = 0.05F;
    private static final float field_36628 = -0.3F;

    private DDOreVeinSampler() {
    }

    public static ChunkNoiseSampler.BlockStateSampler create(DensityFunction veinToggle, DensityFunction veinRidged, DensityFunction veinGap, RandomDeriver randomDeriver) {
        return (pos) -> {
            double veinTypeSample = veinToggle.sample(pos);
            DDOreVeinSampler.VeinType veinType = VeinType.getVeinTypeForSample(veinTypeSample);
            double absVeinTypeSample = Math.abs(veinTypeSample);
    
            int i = pos.blockY();
            int j = veinType.maxY - i;
            int k = i - veinType.minY;
            if (k >= 0 && j >= 0) {
                int l = Math.min(j, k);
                double f = MathHelper.clampedLerpFromProgress(l, 0.0D, field_36621, field_36622, 0.0D);
                if (absVeinTypeSample + f < 0.4) {
                    return null;
                } else {
                    AbstractRandom abstractRandom = randomDeriver.createRandom(pos.blockX(), i, pos.blockZ());
                    if (abstractRandom.nextFloat() > field_36623) {
                        return null;
                    } else if (veinRidged.sample(pos) >= 0.0D) {
                        return null;
                    } else {
                        double g = MathHelper.clampedLerpFromProgress(absVeinTypeSample, field_36620, field_36626, field_36624, field_36625);
                        if ((double)abstractRandom.nextFloat() < g && veinGap.sample(pos) > field_36628) {
                            return abstractRandom.nextFloat() < RAW_ORE_BLOCK_CHANCE ? veinType.rawOreBlock : veinType.ore;
                        } else {
                            return veinType.stone;
                        }
                    }
                }
            } else {
                return null;
            }
        };
    }

    protected enum VeinType {
        COPPER(Blocks.COPPER_ORE.getDefaultState(), Blocks.RAW_COPPER_BLOCK.getDefaultState(), Blocks.GRANITE.getDefaultState(), -128, -56),
        IRON(Blocks.DEEPSLATE_IRON_ORE.getDefaultState(), Blocks.RAW_IRON_BLOCK.getDefaultState(), Blocks.TUFF.getDefaultState(), -256, -128),
        GOLD(Blocks.DEEPSLATE_GOLD_ORE.getDefaultState(), Blocks.RAW_GOLD_BLOCK.getDefaultState(), Blocks.DIORITE.getDefaultState(), -256, -128),
        DIAMOND(Blocks.DEEPSLATE_DIAMOND_ORE.getDefaultState(), Blocks.RAW_IRON_BLOCK.getDefaultState(), Blocks.TUFF.getDefaultState(), -256, -128),
        REDSTONE(Blocks.DEEPSLATE_REDSTONE_ORE.getDefaultState(), Blocks.DEEPSLATE_REDSTONE_ORE.getDefaultState(), Blocks.GRANITE.getDefaultState(), -256, -128),
        LAPIS(Blocks.DEEPSLATE_LAPIS_ORE.getDefaultState(), Blocks.RAW_IRON_BLOCK.getDefaultState(), Blocks.TUFF.getDefaultState(), -256, -128),
        COAL(Blocks.DEEPSLATE_COAL_ORE.getDefaultState(), Blocks.COAL_BLOCK.getDefaultState(), Blocks.TUFF.getDefaultState(), -256, -128),
        EMERALD(Blocks.DEEPSLATE_EMERALD_ORE.getDefaultState(), Blocks.DEEPSLATE_EMERALD_ORE.getDefaultState(), Blocks.DIORITE.getDefaultState(), -256, -128);

        final BlockState ore;
        final BlockState rawOreBlock;
        final BlockState stone;
        private final int minY;
        private final int maxY;

        VeinType(BlockState ore, BlockState rawOreBlock, BlockState stone, int minY, int maxY) {
            this.ore = ore;
            this.rawOreBlock = rawOreBlock;
            this.stone = stone;
            this.minY = minY;
            this.maxY = maxY;
        }
        
        public static VeinType getVeinTypeForSample(double veinTypeSample) {
            return veinTypeSample > 0.0D ? DDOreVeinSampler.VeinType.COPPER : DDOreVeinSampler.VeinType.IRON; // TODO: expand
        }
        
    }
}