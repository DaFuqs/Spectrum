package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.*;

public record CrystalFormationFeatureFeatureConfig(IntProvider iterationCountProvider,
												   BlockStateProvider blockStateProvider,
												   HolderSet<Block> canStartOnBlocks,
												   HolderSet<Block> canExtendOnBlocks, Boolean canGrowUpwards,
												   Boolean canGrowDownwards) implements FeatureConfiguration {
	
	public static final Codec<CrystalFormationFeatureFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			IntProvider.POSITIVE_CODEC.fieldOf("iterations").forGetter((config) -> config.iterationCountProvider),
			BlockStateProvider.CODEC.fieldOf("state_provider").forGetter((config) -> config.blockStateProvider),
			RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_start_on").forGetter((config) -> config.canStartOnBlocks),
			RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_extend_on").forGetter((config) -> config.canStartOnBlocks),
			Codec.BOOL.fieldOf("can_grow_upwards").forGetter((config) -> config.canGrowUpwards),
			Codec.BOOL.fieldOf("can_grow_downwards").forGetter((config) -> config.canGrowDownwards)
	).apply(instance, CrystalFormationFeatureFeatureConfig::new));

}
