package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;
import java.util.function.*;

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
		
		Optional<Registry<GeodeOreDefinition>> geodeOresRegistry = context.level().registryAccess().registry(SpectrumRegistryKeys.GEODE_ORES);
		if (geodeOresRegistry.isPresent()) {
			Optional<GeodeOreDefinition> geodeOreDefinition = geodeOresRegistry.get()
					.stream()
					.filter(definition -> definition.blocks().test(gemBlock))
					.findFirst();
			
			if (geodeOreDefinition.isPresent()) {
				GeodeOreDefinition definition = geodeOreDefinition.get();
				WorldGenLevel world = context.level();
				RandomSource random = context.random();
				
				// having steps for distance with a fixed amount assures
				// that the ore amount gets less with distance from the center
				for (int distance = definition.minDistance(); distance < definition.maxDistance(); distance++) {
					for (int i = 0; i < definition.tries(); i++) {
						int xOffset = (random.nextInt(distance + 1) * 2 - distance);
						int yOffset = (random.nextInt(distance + 1) * 2 - distance);
						int zOffset = (random.nextInt(distance + 1) * 2 - distance);
						
						BlockPos pos = context.origin().offset(xOffset, yOffset, zOffset);
						BlockState state = world.getBlockState(pos);
						
						for (OreConfiguration.TargetBlockState target : definition.oreConfiguration()) {
							if (target.target.test(state, random)) {
								world.setBlock(pos, target.state, 3);
								break;
							}
						}
					}
				}
			}
		}
	}
	
}
