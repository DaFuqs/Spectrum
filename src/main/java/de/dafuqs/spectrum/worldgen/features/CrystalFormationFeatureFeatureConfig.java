package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.block.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.*;

public class CrystalFormationFeatureFeatureConfig implements FeatureConfig {

    public static final Codec<CrystalFormationFeatureFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(
                IntProvider.POSITIVE_CODEC.fieldOf("iterations").forGetter((config) -> config.iterationCountProvider),
                BlockStateProvider.TYPE_CODEC.fieldOf("state_provider").forGetter((config) -> config.blockStateProvider),
                RegistryCodecs.entryList(Registry.BLOCK_KEY).fieldOf("can_start_on").forGetter((config) -> config.canStartOnBlocks),
                RegistryCodecs.entryList(Registry.BLOCK_KEY).fieldOf("can_extend_on").forGetter((config) -> config.canStartOnBlocks)
        ).apply(instance, CrystalFormationFeatureFeatureConfig::new);
    });

    public final IntProvider iterationCountProvider;
    public final BlockStateProvider blockStateProvider;
    public final RegistryEntryList<Block> canStartOnBlocks;
    public final RegistryEntryList<Block> canExtendOnBlocks;

    public CrystalFormationFeatureFeatureConfig(IntProvider iterationCountProvider, BlockStateProvider blockStateProvider, RegistryEntryList<Block> canStartOnBlocks, RegistryEntryList<Block> canExtendOnBlocks) {
        this.iterationCountProvider = iterationCountProvider;
        this.blockStateProvider = blockStateProvider;
        this.canStartOnBlocks = canStartOnBlocks;
        this.canExtendOnBlocks = canExtendOnBlocks;
    }

}
