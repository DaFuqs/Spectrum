package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.configurations.*;

public class AirCheckDiskFeature extends OreFeature {
	
	public AirCheckDiskFeature(Codec<OreConfiguration> codec) {
        super(codec);
    }
	
	@Override
	public boolean place(FeaturePlaceContext<OreConfiguration> context) {
		BlockPos blockPos = context.origin();
		WorldGenLevel structureWorldAccess = context.level();
		
		if (structureWorldAccess.getBlockState(blockPos).isAir()) {
			return false;
		}
		
		return super.place(context);
	}

}
