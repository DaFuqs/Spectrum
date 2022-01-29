package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.GeodeFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class AirCheckGeodeFeature extends Feature<GeodeFeatureConfig> {
	
	public AirCheckGeodeFeature(Codec<GeodeFeatureConfig> configCodec) {
		super(configCodec);
	}
	
	@Override
	public boolean generate(FeatureContext<GeodeFeatureConfig> context) {
		return false;
	}
	
}
