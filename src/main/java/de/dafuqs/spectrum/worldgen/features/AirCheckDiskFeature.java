package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class AirCheckDiskFeature extends OreFeature {
	
	public AirCheckDiskFeature(Codec<OreFeatureConfig> codec) {
		super(codec);
	}
	
	public boolean generate(FeatureContext<OreFeatureConfig> context) {
		BlockPos blockPos = context.getOrigin();
		StructureWorldAccess structureWorldAccess = context.getWorld();
		
		if (structureWorldAccess.getBlockState(blockPos).isAir()) {
			return false;
		}
		
		return super.generate(context);
	}
	
}
