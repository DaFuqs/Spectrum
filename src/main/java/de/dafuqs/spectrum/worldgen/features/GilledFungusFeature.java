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
	
	@Override
	public boolean generate(FeatureContext<GilledFungusFeatureConfig> context) {
		StructureWorldAccess structureWorldAccess = context.getWorld();
		BlockPos blockPos = context.getOrigin();
		GilledFungusFeatureConfig hugeFungusFeatureConfig = context.getConfig();
		Block validBaseBlock = hugeFungusFeatureConfig.validBase();
		BlockState baseBlock = structureWorldAccess.getBlockState(blockPos.down());
		
		if (!baseBlock.isOf(validBaseBlock)) {
			return false;
		}

        Random random = context.getRandom();
        ChunkGenerator chunkGenerator = context.getGenerator();

        int stemHeight = hugeFungusFeatureConfig.baseStemHeight().get(random);
        if (random.nextInt(12) == 0) {
            stemHeight *= 2;
        }
        if (blockPos.getY() + stemHeight + 1 >= chunkGenerator.getWorldHeight()) {
            return false;
        }

        structureWorldAccess.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 4);
        this.generateStem(structureWorldAccess, hugeFungusFeatureConfig, blockPos, stemHeight);
        this.generateHat(structureWorldAccess, random, hugeFungusFeatureConfig, blockPos, stemHeight);
        return true;
    }

    private static boolean isReplaceable(WorldAccess world, BlockPos pos, boolean replacePlants) {
        return world.testBlockState(pos, (state) -> state.isReplaceable() || replacePlants);
    }

    private void generateStem(WorldAccess world, GilledFungusFeatureConfig config, BlockPos pos, int stemHeight) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		BlockState blockState = config.stem().getDefaultState();
		int i = 0;
        for (int x = -i; x <= i; ++x) {
            for (int z = -i; z <= i; ++z) {
                for (int y = 0; y < stemHeight; ++y) {
                    mutable.set(pos, x, y, z);
                    if (isReplaceable(world, mutable, true)) {
                        this.setBlockState(world, mutable, blockState);
                    }
                }
            }
        }
    }

    private void generateHat(WorldAccess world, Random random, GilledFungusFeatureConfig config, BlockPos pos, int stemHeight) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int hatRadius = Math.min(random.nextInt(1 + stemHeight / 5) + 4, 9);
        int currentRadius;

        BlockState gillsState = config.gills().getDefaultState();
        BlockState capState = config.cap().getDefaultState();
        var start = hatRadius > 4 ? -2 : -1;
        var firstLoop = true;

        for (int y = start; y < Math.max(Math.round(hatRadius / 3F), 2); ++y) {
            var underHang = y < 0;
            boolean isLowestLevel = y == 0;

            currentRadius = underHang ? hatRadius : (int) Math.round(hatRadius / Math.pow(1.175, Math.max(y - 1, 0))) - (isLowestLevel ? 0 : 1);

            for (int x = -currentRadius; x <= currentRadius; ++x) {
                for (int z = -currentRadius; z <= currentRadius; ++z) {

                    boolean isCorner = Math.abs(x) == currentRadius && Math.abs(z) == currentRadius;
                    if (isCorner) {
                        continue;
                    }

                    mutable.set(pos, x, stemHeight + y, z);
                    if (isReplaceable(world, mutable, false)) {
                        var rad = Math.sqrt(mutable.getSquaredDistanceFromCenter(pos.getX(), mutable.getY(), pos.getZ()));

                        if (underHang) {
                            if(!(random.nextInt(3) == 0 && firstLoop) && rad <= currentRadius && rad > currentRadius - 1) {
                                this.setBlockState(world, mutable, capState);
                            }
                        }
                        else if (isLowestLevel) {
                            if (rad <= currentRadius - 1) {
                                this.setBlockState(world, mutable, gillsState.with(PillarBlock.AXIS, Math.abs(x) < Math.abs(z) ? Direction.Axis.X : Direction.Axis.Z));
                            } else if(rad <= currentRadius) {
                                this.setBlockState(world, mutable, capState);

                            }
                        }
                        else if (rad <= currentRadius) {
                            this.setBlockState(world, mutable, capState);

                        }
                    }
                }
            }

            firstLoop = false;
        }
    }

}
