package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(GeodeFeature.class)
public abstract class GeodesGenerateWithGemstoneOresMixin {
	
	@Inject(at = @At("TAIL"), method = "generate(Lnet/minecraft/world/gen/feature/util/FeatureContext;)Z")
	public void generate(FeatureContext<GeodeFeatureConfig> context, CallbackInfoReturnable<Boolean> cir) {
		generateGemstoneOres(context);
	}
	
	/**
	 * After generating a geode place gemstone ores around of it
	 * that way it is easier for players to spot geodes, gives them
	 * a little kickstart and makes geodes more exciting to find in general
	 *
	 * @param context The GeodeFeatures feature config
	 */
	private void generateGemstoneOres(FeatureContext<GeodeFeatureConfig> context) {
		BlockState gemBlock = context.getConfig().layerConfig.innerLayerProvider.getBlockState(context.getRandom(), context.getOrigin());
		if (gemBlock != null) {
			BlockState oreBlockState = getGemstoneOreForGeodeBlock(gemBlock);
			if (oreBlockState != null) { // do not handle other modded geodes
				BlockState blackslagOreBlockState = getGemstoneBlackslagOreForGeodeBlock(gemBlock);
				BlockState deepslateOreBlockState = getGemstoneDeepslateOreForGeodeBlock(gemBlock);
				StructureWorldAccess world = context.getWorld();
				Random random = context.getRandom();
				// having steps for distance with a fixed amount assures
				// that the ore amount gets less with distance from the center
				for (int distance = 5; distance < 14; distance++) {
					for (int i = 0; i < 24; i++) {
						int xOffset = (random.nextInt(distance + 1) * 2 - distance);
						int yOffset = (random.nextInt(distance + 1) * 2 - distance);
						int zOffset = (random.nextInt(distance + 1) * 2 - distance);

						BlockPos pos = context.getOrigin().add(xOffset, yOffset, zOffset);
						BlockState state = world.getBlockState(pos);
						if (state.isIn(SpectrumBlockTags.BLACKSLAG_ORE_REPLACEABLES)) {
							world.setBlockState(pos, blackslagOreBlockState, 3);
						} else if (state.isIn(BlockTags.DEEPSLATE_ORE_REPLACEABLES)) {
							world.setBlockState(pos, deepslateOreBlockState, 3);
						} else if (world.getBlockState(pos).isIn(BlockTags.STONE_ORE_REPLACEABLES)) {
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
	 *
	 * @param blockState The blockstate the geode generates with
	 * @return the matching ore for that block state. Does return null if no matching ore exists. For example if another mod adds additional geodes
	 */
	private BlockState getGemstoneOreForGeodeBlock(BlockState blockState) {
		Block block = blockState.getBlock();
		if (block.equals(Blocks.AMETHYST_BLOCK)) {
			return SpectrumBlocks.AMETHYST_ORE.getDefaultState();
		} else if (block.equals(SpectrumBlocks.CITRINE_BLOCK)) {
			return SpectrumBlocks.CITRINE_ORE.getDefaultState();
		} else if (block.equals(SpectrumBlocks.TOPAZ_BLOCK)) {
			return SpectrumBlocks.TOPAZ_ORE.getDefaultState();
		} else if (block.equals(SpectrumBlocks.ONYX_BLOCK)) {
			return SpectrumBlocks.ONYX_ORE.getDefaultState();
		} else if (block.equals(SpectrumBlocks.MOONSTONE_BLOCK)) {
			return SpectrumBlocks.MOONSTONE_ORE.getDefaultState();
		}
		return null;
	}
	
	/**
	 * Returns a matching ore block for a gemstone block
	 * Aka amethyst_block => amethyst_ore
	 *
	 * @param blockState The blockstate the geode generates with
	 * @return the matching ore for that block state. Does return null if no matching ore exists. For example if another mod adds additional geodes
	 */
	private BlockState getGemstoneDeepslateOreForGeodeBlock(BlockState blockState) {
		Block block = blockState.getBlock();
		if (block.equals(Blocks.AMETHYST_BLOCK)) {
			return SpectrumBlocks.DEEPSLATE_AMETHYST_ORE.getDefaultState();
		} else if (block.equals(SpectrumBlocks.CITRINE_BLOCK)) {
			return SpectrumBlocks.DEEPSLATE_CITRINE_ORE.getDefaultState();
		} else if (block.equals(SpectrumBlocks.TOPAZ_BLOCK)) {
			return SpectrumBlocks.DEEPSLATE_TOPAZ_ORE.getDefaultState();
		} else if (block.equals(SpectrumBlocks.ONYX_BLOCK)) {
			return SpectrumBlocks.DEEPSLATE_ONYX_ORE.getDefaultState();
		} else if (block.equals(SpectrumBlocks.MOONSTONE_BLOCK)) {
			return SpectrumBlocks.DEEPSLATE_MOONSTONE_ORE.getDefaultState();
		}
		return null;
	}

	/**
	 * Returns a matching ore block for a gemstone block
	 * Aka amethyst_block => amethyst_ore
	 *
	 * @param blockState The blockstate the geode generates with
	 * @return the matching ore for that block state. Does return null if no matching ore exists. For example if another mod adds additional geodes
	 */
	private BlockState getGemstoneBlackslagOreForGeodeBlock(BlockState blockState) {
		Block block = blockState.getBlock();
		if (block.equals(Blocks.AMETHYST_BLOCK)) {
			return SpectrumBlocks.BLACKSLAG_AMETHYST_ORE.getDefaultState();
		} else if (block.equals(SpectrumBlocks.CITRINE_BLOCK)) {
			return SpectrumBlocks.BLACKSLAG_CITRINE_ORE.getDefaultState();
		} else if (block.equals(SpectrumBlocks.TOPAZ_BLOCK)) {
			return SpectrumBlocks.BLACKSLAG_TOPAZ_ORE.getDefaultState();
		} else if (block.equals(SpectrumBlocks.ONYX_BLOCK)) {
			return SpectrumBlocks.BLACKSLAG_ONYX_ORE.getDefaultState();
		} else if (block.equals(SpectrumBlocks.MOONSTONE_BLOCK)) {
			return SpectrumBlocks.BLACKSLAG_MOONSTONE_ORE.getDefaultState();
		}
		return null;
	}

}
