package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;

public class LagoonFishingBobberEntity extends SpectrumFishingBobberEntity {
	
	public LagoonFishingBobberEntity(EntityType<? extends LagoonFishingBobberEntity> entityType, Level world) {
		super(entityType, world);
	}
	
	public LagoonFishingBobberEntity(Player thrower, Level world, int luckOfTheSeaLevel, int waitTimeReductionTicks, int exuberanceLevel, int bigCatchLevel, int serendipityReelLevel, boolean inventoryInsertion, boolean foundry) {
		super(SpectrumEntityTypes.LAGOON_FISHING_BOBBER.get(), thrower, world, luckOfTheSeaLevel, waitTimeReductionTicks, exuberanceLevel, bigCatchLevel, serendipityReelLevel, inventoryInsertion, foundry);
	}
	
	@Override
	public int getLineColor() {
		return 0xb6c6c8FF;
	}
}
