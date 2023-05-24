package de.dafuqs.spectrum.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.block.*;
import net.minecraft.registry.entry.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.registry.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.*;

public record CrystalFormationFeatureFeatureConfig(IntProvider iterationCountProvider,
                                                   BlockStateProvider blockStateProvider,
                                                   RegistryEntryList<Block> canStartOnBlocks,
                                                   RegistryEntryList<Block> canExtendOnBlocks, boolean canGrowUpwards,
                                                   boolean canGrowDownwards) implements FeatureConfig {
    
    public static final Codec<CrystalFormationFeatureFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            IntProvider.POSITIVE_CODEC.fieldOf("iterations").forGetter((config) -> config.iterationCountProvider),
            BlockStateProvider.TYPE_CODEC.fieldOf("state_provider").forGetter((config) -> config.blockStateProvider),
            RegistryCodecs.entryList(RegistryKeys.BLOCK).fieldOf("can_start_on").forGetter((config) -> config.canStartOnBlocks),
            RegistryCodecs.entryList(RegistryKeys.BLOCK).fieldOf("can_extend_on").forGetter((config) -> config.canStartOnBlocks),
            Codec.BOOL.fieldOf("can_grow_upwards").forGetter((config) -> config.canGrowUpwards),
            Codec.BOOL.fieldOf("can_grow_downwards").forGetter((config) -> config.canGrowDownwards)
    ).apply(instance, CrystalFormationFeatureFeatureConfig::new));

}
