package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.*;

public class GiantGilledFungusFeature extends Feature<GilledFungusFeatureConfig> {

	public GiantGilledFungusFeature(Codec<GilledFungusFeatureConfig> codec) {
		super(codec);
	}
	
	@Override
	public boolean generate(FeatureContext<GilledFungusFeatureConfig> context) {
		StructureWorldAccess world = context.getWorld();
		BlockPos blockPos = context.getOrigin();
		GilledFungusFeatureConfig config = context.getConfig();
		Block validBaseBlock = config.validBase();

        BlockState baseBlock = world.getBlockState(blockPos.down());

        if (!baseBlock.isOf(validBaseBlock)) {
            return false;
        }

        Random random = context.getRandom();
        ChunkGenerator chunkGenerator = context.getGenerator();

        var maxHeight = config.baseStemHeight().get(random);
        var mutable = blockPos.mutableCopy();
        int stemHeight = 0;
        int stemGirth = 1;

        // Find out just how tall we can go
        for (; stemHeight < maxHeight; ++stemHeight) {
            if (!isReplaceable(world, mutable.move(Direction.UP), false)) {
                break;
            }
        }

        if (stemHeight < maxHeight / 1.667F)
            return false;

        stemGirth += (int) Math.floor(stemHeight / 33F);

        if (blockPos.getY() + stemHeight + 1 >= chunkGenerator.getWorldHeight()) {
            return false;
        }

        world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 4);

        this.generateStemRing(blockPos.down(3), config, world, random, stemGirth, stemHeight + 3, true);

        if (stemHeight > 60 || (stemHeight > 30 && random.nextBoolean())) {
            this.generateHat(world, random, config, blockPos, stemHeight, 1F);
            this.generateHat(world, random, config, blockPos, Math.round(stemHeight * 0.65F), 0.7F);
        }
        else {
            this.generateHat(world, random, config, blockPos, stemHeight, 1F);
        }

        for (int i = 0; i < stemHeight / 1.5F; i++) {
            this.generateHat(world, random, config, blockPos.add(random.nextInt(stemGirth * 3), 0, random.nextInt(stemGirth * 3)), random.nextInt(stemHeight / 5) + 1, (random.nextFloat() / (i + 1) * 0.5F) + 0.5F);
        }

        return true;
    }

    private static boolean isReplaceable(WorldAccess world, BlockPos pos, boolean replacePlants) {
        return world.testBlockState(pos, (state) -> state.isReplaceable() || replacePlants);
    }

    private void generateStemRing(BlockPos blockPos, GilledFungusFeatureConfig config, WorldAccess world, Random random, int stemGirth, int stemHeight, boolean recursive) {
        var stemPos = blockPos.mutableCopy();

        for (int x = -stemGirth; x <= stemGirth; x++) {
            for (int z = -stemGirth; z <= stemGirth; z++) {
                stemPos.set(blockPos, x, 0, z);
                if (Math.sqrt(stemPos.getSquaredDistance(blockPos)) > stemGirth)
                    continue;

                this.generateStem(world, config, stemPos, stemHeight);
                if (recursive && !stemPos.equals(blockPos) && random.nextFloat() <= 0.2F) {
                    var height = Math.round((float) stemHeight * (random.nextFloat() * 0.667F));
                    var offsetX = (int) (x * (2 + random.nextFloat()));
                    var offsetZ = (int) (z * (2 + random.nextFloat()));

                    generateStemRing(stemPos.add(offsetX, 0, offsetZ), config, world, random, stemGirth / 2, height, false);
                    this.generateHat(world, random, config, stemPos.add((int) (offsetX * 1.5), 0, (int) (offsetZ * 1.5)), height, 0.333F + random.nextFloat() * 0.667F);
                }

            }
        }
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

    private void generateHat(WorldAccess world, Random random, GilledFungusFeatureConfig config, BlockPos pos, int stemHeight, float sizeMod) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		int hatRadius = Math.round(Math.min(Math.round(random.nextInt(Math.round(1 + stemHeight / 7F)) + (stemHeight / 7F) + 3)  * sizeMod, 17));
		int currentRadius;

		BlockState gillsState = config.gills().getDefaultState();
		BlockState capState = config.cap().getDefaultState();
        var start = hatRadius > 11 ? -3 : -2;
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
                            if (rad <= currentRadius / 1.5F && rad >= currentRadius / 3F)
                                continue;

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
