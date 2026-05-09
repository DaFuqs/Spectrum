package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import javax.annotation.*;

@Mixin(GeodeFeature.class)
public abstract class GeodesGenerateWithGemstoneOresMixin {
	
	@Inject(at = @At("TAIL"), method = "place")
	public void generate(FeaturePlaceContext<GeodeConfiguration> context, CallbackInfoReturnable<Boolean> cir) {
		spectrum$generateGemstoneOres(context);
	}
	
	/**
	 * After generating a geode place gemstone ores around of it
	 * that way it is easier for players to spot geodes, gives them
	 * a little kickstart and makes geodes more exciting to find in general
	 *
	 * @param context The GeodeFeatures feature config
	 */
	@Unique
	private void spectrum$generateGemstoneOres(FeaturePlaceContext<GeodeConfiguration> context) {
		BlockState gemBlock = context.config().geodeBlockSettings.innerLayerProvider.getState(context.random(), context.origin());
		BlockState oreBlockState = spectrum$getGemstoneOreForGeodeBlock(gemBlock);
		if (oreBlockState != null) { // do not handle other modded geodes
			BlockState blackslagOreBlockState = spectrum$getGemstoneBlackslagOreForGeodeBlock(gemBlock);
			BlockState deepslateOreBlockState = spectrum$getGemstoneDeepslateOreForGeodeBlock(gemBlock);
			WorldGenLevel world = context.level();
			RandomSource random = context.random();
			// having steps for distance with a fixed amount assures
			// that the ore amount gets less with distance from the center
			for (int distance = 5; distance < 14; distance++) {
				for (int i = 0; i < 24; i++) {
					int xOffset = (random.nextInt(distance + 1) * 2 - distance);
					int yOffset = (random.nextInt(distance + 1) * 2 - distance);
					int zOffset = (random.nextInt(distance + 1) * 2 - distance);
					
					BlockPos pos = context.origin().offset(xOffset, yOffset, zOffset);
					BlockState state = world.getBlockState(pos);
					if (state.is(SpectrumBlockTags.BLACKSLAG_ORE_REPLACEABLES)) {
						world.setBlock(pos, blackslagOreBlockState, 3);
					} else if (state.is(BlockTags.DEEPSLATE_ORE_REPLACEABLES)) {
						world.setBlock(pos, deepslateOreBlockState, 3);
					} else if (world.getBlockState(pos).is(BlockTags.STONE_ORE_REPLACEABLES)) {
						world.setBlock(pos, oreBlockState, 3);
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
	@Unique
	private @Nullable BlockState spectrum$getGemstoneOreForGeodeBlock(BlockState blockState) {
		Block block = blockState.getBlock();
		if (block.equals(Blocks.AMETHYST_BLOCK)) {
			return SpectrumBlocks.AMETHYST_ORE.get().defaultBlockState();
		} else if (block.equals(SpectrumBlocks.CITRINE_BLOCK.get())) {
			return SpectrumBlocks.CITRINE_ORE.get().defaultBlockState();
		} else if (block.equals(SpectrumBlocks.TOPAZ_BLOCK.get())) {
			return SpectrumBlocks.TOPAZ_ORE.get().defaultBlockState();
		} else if (block.equals(SpectrumBlocks.ONYX_BLOCK.get())) {
			return SpectrumBlocks.ONYX_ORE.get().defaultBlockState();
		} else if (block.equals(SpectrumBlocks.MOONSTONE_BLOCK.get())) {
			return SpectrumBlocks.MOONSTONE_ORE.get().defaultBlockState();
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
	@Unique
	private @Nullable BlockState spectrum$getGemstoneDeepslateOreForGeodeBlock(BlockState blockState) {
		Block block = blockState.getBlock();
		if (block.equals(Blocks.AMETHYST_BLOCK)) {
			return SpectrumBlocks.DEEPSLATE_AMETHYST_ORE.get().defaultBlockState();
		} else if (block.equals(SpectrumBlocks.CITRINE_BLOCK.get())) {
			return SpectrumBlocks.DEEPSLATE_CITRINE_ORE.get().defaultBlockState();
		} else if (block.equals(SpectrumBlocks.TOPAZ_BLOCK.get())) {
			return SpectrumBlocks.DEEPSLATE_TOPAZ_ORE.get().defaultBlockState();
		} else if (block.equals(SpectrumBlocks.ONYX_BLOCK.get())) {
			return SpectrumBlocks.DEEPSLATE_ONYX_ORE.get().defaultBlockState();
		} else if (block.equals(SpectrumBlocks.MOONSTONE_BLOCK.get())) {
			return SpectrumBlocks.DEEPSLATE_MOONSTONE_ORE.get().defaultBlockState();
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
	@Unique
	private @Nullable BlockState spectrum$getGemstoneBlackslagOreForGeodeBlock(BlockState blockState) {
		Block block = blockState.getBlock();
		if (block.equals(Blocks.AMETHYST_BLOCK)) {
			return SpectrumBlocks.BLACKSLAG_AMETHYST_ORE.get().defaultBlockState();
		} else if (block.equals(SpectrumBlocks.CITRINE_BLOCK.get())) {
			return SpectrumBlocks.BLACKSLAG_CITRINE_ORE.get().defaultBlockState();
		} else if (block.equals(SpectrumBlocks.TOPAZ_BLOCK.get())) {
			return SpectrumBlocks.BLACKSLAG_TOPAZ_ORE.get().defaultBlockState();
		} else if (block.equals(SpectrumBlocks.ONYX_BLOCK.get())) {
			return SpectrumBlocks.BLACKSLAG_ONYX_ORE.get().defaultBlockState();
		} else if (block.equals(SpectrumBlocks.MOONSTONE_BLOCK.get())) {
			return SpectrumBlocks.BLACKSLAG_MOONSTONE_ORE.get().defaultBlockState();
		}
		return null;
	}
	
}
