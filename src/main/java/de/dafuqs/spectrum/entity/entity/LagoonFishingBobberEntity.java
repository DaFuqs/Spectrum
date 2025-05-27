package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;

public class LagoonFishingBobberEntity extends SpectrumFishingBobberEntity {

	public LagoonFishingBobberEntity(EntityType<? extends LagoonFishingBobberEntity> entityType, World world) {
		super(entityType, world);
	}

	public LagoonFishingBobberEntity(PlayerEntity thrower, World world, int luckOfTheSeaLevel, int waitTimeReductionTicks, int exuberanceLevel, int bigCatchLevel, int serendipityReelLevel, boolean inventoryInsertion, boolean foundry) {
		super(SpectrumEntityTypes.LAGOON_FISHING_BOBBER, thrower, world, luckOfTheSeaLevel, waitTimeReductionTicks, exuberanceLevel, bigCatchLevel, serendipityReelLevel, inventoryInsertion, foundry);
	}
	
	@Override
	public int getLineColor() {
		return 0xb6c6c8FF;
	}
}
