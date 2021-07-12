package de.dafuqs.pigment.mixin;

import de.dafuqs.pigment.Support;
import de.dafuqs.pigment.registries.PigmentBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.GeodeFeature;
import net.minecraft.world.gen.feature.GeodeFeatureConfig;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Random;

@Mixin(GeodeFeature.class)
public class GeodesGenerateWithGemstoneOresMixin {

    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/world/gen/feature/GeodeFeature;generate(Lnet/minecraft/world/gen/feature/util/FeatureContext;)Z")
    public void generate(FeatureContext<GeodeFeatureConfig> context, CallbackInfoReturnable<Boolean> cir) {
        generateGemstoneOres(context);
    }

    /**
     * After generating a geode place gemstone ores around of it
     * that way it's easier for players to spot geodes, gives them
     * a little kickstart and makes geodes more exciting to find in general
     * @param context The GeodeFeatures feature config
     */
    private void generateGemstoneOres(FeatureContext<GeodeFeatureConfig> context) {
        BlockState gemBlock = context.getConfig().layerConfig.innerLayerProvider.getBlockState(context.getRandom(), context.getOrigin());
        if(gemBlock != null) {
            BlockState oreBlockState = getGemstoneOreForGeodeBlock(gemBlock);
            if (oreBlockState != null) {
                BlockState deepslateOreBlockState = getGemstoneDeepslateOreForGeodeBlock(gemBlock);
                StructureWorldAccess world = context.getWorld();
                Random random = context.getRandom();
                // having steps for distance with a fixed amount assures
                // that the ore amount gets less with distance from the center
                for(int distance = 6; distance < 16; distance++) {
                    for (int i = 0; i < 10; i++) {
                        int xOffset = (random.nextInt(distance + 1) * 2 - distance);
                        int yOffset = (random.nextInt(distance + 1) * 2 - distance);
                        int zOffset = (random.nextInt(distance + 1) * 2 - distance);

                        BlockPos pos = context.getOrigin().add(xOffset, yOffset, zOffset);
                        BlockState state = world.getBlockState(pos);
                        if(state.isOf(Blocks.DEEPSLATE)) {
                            world.setBlockState(pos, deepslateOreBlockState, 3);
                        } else if (Support.hasTag(world.getBlockState(pos), BlockTags.BASE_STONE_OVERWORLD)) {
                            world.setBlockState(pos, oreBlockState, 3);
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns a matching ore block for a gemstone block
     * Aka amethyst_block => amethyst_ore
     * @param blockState The blockstate the geode generates with
     * @return the matching ore for that block state. Does return null if no matching ore exists. For example if another mod adds additional geodes
     */
    private BlockState getGemstoneOreForGeodeBlock(BlockState blockState) {
        Block block = blockState.getBlock();
        if(block.equals(Blocks.AMETHYST_BLOCK)) {
            return PigmentBlocks.AMETHYST_ORE.getDefaultState();
        } else if(block.equals(PigmentBlocks.CITRINE_BLOCK)) {
            return PigmentBlocks.CITRINE_ORE.getDefaultState();
        } else if(block.equals(PigmentBlocks.TOPAZ_BLOCK)) {
            return PigmentBlocks.TOPAZ_ORE.getDefaultState();
        } else if(block.equals(PigmentBlocks.ONYX_BLOCK)) {
            return PigmentBlocks.ONYX_ORE.getDefaultState();
        } else if(block.equals(PigmentBlocks.MOONSTONE_BLOCK)) {
            return PigmentBlocks.MOONSTONE_ORE.getDefaultState();
        }
        return null;
    }

    /**
     * Returns a matching ore block for a gemstone block
     * Aka amethyst_block => amethyst_ore
     * @param blockState The blockstate the geode generates with
     * @return the matching ore for that block state. Does return null if no matching ore exists. For example if another mod adds additional geodes
     */
    private BlockState getGemstoneDeepslateOreForGeodeBlock(BlockState blockState) {
        Block block = blockState.getBlock();
        if(block.equals(Blocks.AMETHYST_BLOCK)) {
            return PigmentBlocks.DEEPSLATE_AMETHYST_ORE.getDefaultState();
        } else if(block.equals(PigmentBlocks.CITRINE_BLOCK)) {
            return PigmentBlocks.DEEPSLATE_CITRINE_ORE.getDefaultState();
        } else if(block.equals(PigmentBlocks.TOPAZ_BLOCK)) {
            return PigmentBlocks.DEEPSLATE_TOPAZ_ORE.getDefaultState();
        } else if(block.equals(PigmentBlocks.ONYX_BLOCK)) {
            return PigmentBlocks.DEEPSLATE_ONYX_ORE.getDefaultState();
        } else if(block.equals(PigmentBlocks.MOONSTONE_BLOCK)) {
            return PigmentBlocks.DEEPSLATE_MOONSTONE_ORE.getDefaultState();
        }
        return null;
    }

}
