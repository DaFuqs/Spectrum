package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;

public class BedrockFishingBobberEntity extends SpectrumFishingBobberEntity {

	public BedrockFishingBobberEntity(EntityType<? extends SpectrumFishingBobberEntity> entityType, World world) {
		super(entityType, world);
	}

	public BedrockFishingBobberEntity(PlayerEntity thrower, World world, int luckOfTheSeaLevel, int waitTimeReductionTicks, int exuberanceLevel, int bigCatchLevel, int serendipityReelLevel, boolean inventoryInsertion, boolean foundry) {
		super(SpectrumEntityTypes.BEDROCK_FISHING_BOBBER, thrower, world, luckOfTheSeaLevel, waitTimeReductionTicks, exuberanceLevel, bigCatchLevel, serendipityReelLevel, inventoryInsertion, foundry);
	}
	
	@Override
	public int getLineColor() {
		return 0xFFa10f1d;
	}
}
