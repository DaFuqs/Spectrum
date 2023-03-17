package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.Codec;
import de.dafuqs.spectrum.blocks.jade_vines.JadeiteFlowerBlock;
import de.dafuqs.spectrum.blocks.jade_vines.JadeiteLotusStemBlock;
import de.dafuqs.spectrum.blocks.jade_vines.NephriteBlossomLeavesBlock;
import de.dafuqs.spectrum.blocks.jade_vines.NephriteBlossomStemBlock;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.ArrayList;
import java.util.List;

public class JadeiteLotusFeature extends Feature<JadeiteLotusFeatureConfig> {

    public JadeiteLotusFeature(Codec<JadeiteLotusFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<JadeiteLotusFeatureConfig> context) {
        var world = context.getWorld();
        var origin = context.getOrigin();
        var random = context.getRandom();
        var chunkGen = context.getGenerator();
        var inverted = context.getConfig().inverted();

        var floor = world.getBlockState(inverted ? origin.down() : origin.up());

        if (!(floor.isIn(BlockTags.DIRT) || floor.isIn(SpectrumBlockTags.BASE_STONE_DEEPER_DOWN)))
            return false;

        var stemHeight = Math.round(MathHelper.nextGaussian(random, 8, 8F) + 5);

        if ((inverted && stemHeight + origin.getY() > chunkGen.getWorldHeight()) || !isReplaceable(world, origin, true))
            return false;

        if (!inverted && origin.getY() - stemHeight < chunkGen.getMinimumY())
            return false;

        generateStem(world, origin, stemHeight, inverted);

        return true;
    }

    private void generateStem(WorldAccess world, BlockPos origin, int stemHeight, boolean inverted) {
        var stemPointer = origin.mutableCopy();
        var topStem = false;

        for (int height = 0; height < stemHeight; height++) {

            if (height == 0) {
                this.setBlockState(world, stemPointer, SpectrumBlocks.JADEITE_LOTUS_STEM.getDefaultState().with(JadeiteLotusStemBlock.INVERTED, inverted));
                topStem = true;
            }
            else if(height == stemHeight - 1) {
                this.setBlockState(world, stemPointer, SpectrumBlocks.JADEITE_LOTUS_FLOWER.getDefaultState().with(JadeiteFlowerBlock.FACING, inverted ? Direction.UP : Direction.DOWN));
            }
            else if (isReplaceable(world, stemPointer, true)) {
                this.setBlockState(world, stemPointer, JadeiteLotusStemBlock.getStemVariant(topStem, inverted));
                topStem = !topStem;
            }
            stemPointer.move(0, inverted ? 1 : -1, 0);
        }
    }

    private static boolean isReplaceable(WorldAccess world, BlockPos pos, boolean replacePlants) {
        return world.testBlockState(pos, (state) -> {
            Material material = state.getMaterial();
            return state.getMaterial().isReplaceable() || replacePlants && material == Material.PLANT;
        });
    }
}
