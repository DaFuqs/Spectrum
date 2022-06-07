package de.dafuqs.spectrum.worldgen.structure_features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

public class SpectrumUndergroundStructurePoolFeatureConfig extends StructurePoolFeatureConfig {
	
	public static final Codec<SpectrumUndergroundStructurePoolFeatureConfig> CODEC = RecordCodecBuilder.create(
			(instance) -> instance.group(StructurePool.REGISTRY_CODEC
							.fieldOf("start_pool").forGetter(SpectrumUndergroundStructurePoolFeatureConfig::getStartPool),
					Codec.intRange(0, 7).fieldOf("size").forGetter(SpectrumUndergroundStructurePoolFeatureConfig::getSize),
					Codec.INT.fieldOf("min_y").forGetter(SpectrumUndergroundStructurePoolFeatureConfig::getMinY),
					Codec.INT.fieldOf("max_y").forGetter(SpectrumUndergroundStructurePoolFeatureConfig::getMaxY)
			).apply(instance, SpectrumUndergroundStructurePoolFeatureConfig::new)
	);
	public int minY;
	public int maxY;
	
	public SpectrumUndergroundStructurePoolFeatureConfig(RegistryEntry<StructurePool> startPool, int size, int minY, int maxY) {
		super(startPool, size);
		this.minY = minY;
		this.maxY = maxY;
	}
	
	public int getMinY() {
		return minY;
	}
	
	public int getMaxY() {
		return maxY;
	}
	
}
