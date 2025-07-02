package de.dafuqs.spectrum.deeper_down;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.util.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.levelgen.*;

public final class DDOreVeinSampler {
	
	private static final int BLACKSLAG_LAYER_START_Y = -192;
	
	private static final float field_36620 = 0.4F;
	private static final int field_36621 = 20;
	private static final double field_36622 = 0.2D;
	private static final float VEIN_LIMIT = 0.7F;
	private static final float field_36624 = 0.1F;
	private static final float field_36625 = 0.3F;
	private static final float field_36626 = 0.6F;
	private static final float RAW_ORE_BLOCK_CHANCE = 0.08F;
	private static final float ORE_OR_STONE_THRESHOLD = -0.15F;
	
	private DDOreVeinSampler() {
	}
	
	public static NoiseChunk.BlockStateFiller create(DensityFunction veinToggle, DensityFunction veinRidged, DensityFunction veinGap, PositionalRandomFactory randomDeriver) {
		return (pos) -> {
			double veinTypeSample = veinToggle.compute(pos);
			DDOreVeinSampler.VeinType veinType = VeinType.getVeinTypeForSample(veinTypeSample);
			double absVeinTypeSample = Math.abs(veinTypeSample);
			
			int i = pos.blockY();
			int j = veinType.maxY - i;
			int k = i - veinType.minY;
			if (k >= 0 && j >= 0) {
				int l = Math.min(j, k);
				double f = Mth.clampedMap(l, 0.0D, field_36621, field_36622, 0.0D);
				if (absVeinTypeSample + f < 0.05) {
					return null;
				} else {
					RandomSource random = randomDeriver.at(pos.blockX(), i, pos.blockZ());
					if (random.nextFloat() > VEIN_LIMIT) {
						return null;
					} else if (veinRidged.compute(pos) >= 0.0D) {
						return null;
					} else {
						double g = Mth.clampedMap(absVeinTypeSample, field_36620, field_36626, field_36624, field_36625);
						if ((double) random.nextFloat() < g && veinGap.compute(pos) > ORE_OR_STONE_THRESHOLD) {
							return random.nextFloat() < RAW_ORE_BLOCK_CHANCE ? veinType.rawOreBlock : (pos.blockY() < BLACKSLAG_LAYER_START_Y ? veinType.blackslagOre : veinType.deepslateOre);
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
		IRON(Blocks.IRON_ORE.defaultBlockState(), Blocks.IRON_ORE.defaultBlockState(), Blocks.RAW_IRON_BLOCK.defaultBlockState(), Blocks.STONE.defaultBlockState(), -256, -80),
		GOLD(Blocks.DEEPSLATE_GOLD_ORE.defaultBlockState(), SpectrumBlocks.BLACKSLAG_GOLD_ORE.defaultBlockState(), Blocks.RAW_GOLD_BLOCK.defaultBlockState(), Blocks.DIORITE.defaultBlockState(), -260, -128),
		DIAMOND(Blocks.DEEPSLATE_DIAMOND_ORE.defaultBlockState(), SpectrumBlocks.BLACKSLAG_DIAMOND_ORE.defaultBlockState(), Blocks.COAL_BLOCK.defaultBlockState(), Blocks.TUFF.defaultBlockState(), -316, -192),
		REDSTONE(Blocks.DEEPSLATE_REDSTONE_ORE.defaultBlockState(), SpectrumBlocks.BLACKSLAG_REDSTONE_ORE.defaultBlockState(), Blocks.REDSTONE_BLOCK.defaultBlockState(), Blocks.GRANITE.defaultBlockState(), -220, -80),
		LAPIS(Blocks.DEEPSLATE_LAPIS_ORE.defaultBlockState(), SpectrumBlocks.BLACKSLAG_LAPIS_ORE.defaultBlockState(), Blocks.LAPIS_BLOCK.defaultBlockState(), Blocks.ANDESITE.defaultBlockState(), -260, -128),
		EMERALD(Blocks.DEEPSLATE_EMERALD_ORE.defaultBlockState(), SpectrumBlocks.BLACKSLAG_EMERALD_ORE.defaultBlockState(), SpectrumBlocks.BLACKSLAG.defaultBlockState(), SpectrumBlocks.BASAL_MARBLE.defaultBlockState(), -316, -128);
		
		final BlockState deepslateOre;
		final BlockState blackslagOre;
		final BlockState rawOreBlock;
		final BlockState stone;
		final int minY;
		final int maxY;
		
		VeinType(BlockState deepslateOre, BlockState blackslagOre, BlockState rawOreBlock, BlockState stone, int minY, int maxY) {
			this.deepslateOre = deepslateOre;
			this.blackslagOre = blackslagOre;
			this.rawOreBlock = rawOreBlock;
			this.stone = stone;
			this.minY = minY;
			this.maxY = maxY;
		}
		
		public static VeinType getVeinTypeForSample(double veinTypeSample) {
			return veinTypeSample > 0.3 ? VeinType.REDSTONE :
					veinTypeSample > 0.15 ? VeinType.EMERALD :
							veinTypeSample > 0.0D ? DDOreVeinSampler.VeinType.GOLD :
									veinTypeSample < -0.45 ? DDOreVeinSampler.VeinType.DIAMOND :
											veinTypeSample < -0.3 ? DDOreVeinSampler.VeinType.LAPIS :
													DDOreVeinSampler.VeinType.IRON;
		}
	}

}