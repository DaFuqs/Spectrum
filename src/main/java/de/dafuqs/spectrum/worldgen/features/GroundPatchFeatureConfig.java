package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.block.*;
import net.minecraft.tag.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.*;

public record GroundPatchFeatureConfig(TagKey<Block> replaceable, BlockStateProvider groundState,
                                       RegistryEntry<PlacedFeature> vegetationFeature, VerticalSurfaceType surface,
                                       IntProvider depth, float extraBottomBlockChance, int verticalRange,
                                       float vegetationChance, IntProvider horizontalRadius,
                                       float extraEdgeColumnChance) implements FeatureConfig {
    
    public static final Codec<GroundPatchFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            TagKey.codec(Registry.BLOCK_KEY).fieldOf("replaceable").forGetter((config) -> config.replaceable),
            BlockStateProvider.TYPE_CODEC.fieldOf("ground_state").forGetter((config) -> config.groundState),
            PlacedFeature.REGISTRY_CODEC.fieldOf("vegetation_feature").forGetter((config) -> config.vegetationFeature),
            VerticalSurfaceType.CODEC.fieldOf("surface").forGetter((config) -> config.surface),
            IntProvider.createValidatingCodec(1, 128).fieldOf("depth").forGetter((config) -> config.depth),
            Codec.floatRange(0.0F, 1.0F).fieldOf("extra_bottom_block_chance").forGetter((config) -> config.extraBottomBlockChance),
            Codec.intRange(1, 256).fieldOf("vertical_range").forGetter((config) -> config.verticalRange),
            Codec.floatRange(0.0F, 1.0F).fieldOf("vegetation_chance").forGetter((config) -> config.vegetationChance),
            IntProvider.VALUE_CODEC.fieldOf("xz_radius").forGetter((config) -> config.horizontalRadius),
            Codec.floatRange(0.0F, 1.0F).fieldOf("extra_edge_column_chance").forGetter((config) -> config.extraEdgeColumnChance)
    ).apply(instance, GroundPatchFeatureConfig::new));
    
}
