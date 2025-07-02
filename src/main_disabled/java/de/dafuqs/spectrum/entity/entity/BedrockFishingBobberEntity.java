package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;

public class BedrockFishingBobberEntity extends SpectrumFishingBobberEntity {
	
	public BedrockFishingBobberEntity(EntityType<? extends SpectrumFishingBobberEntity> entityType, Level world) {
		super(entityType, world);
	}
	
	public BedrockFishingBobberEntity(Player thrower, Level world, int luckOfTheSeaLevel, int waitTimeReductionTicks, int exuberanceLevel, int bigCatchLevel, int serendipityReelLevel, boolean inventoryInsertion, boolean foundry) {
		super(SpectrumEntityTypes.BEDROCK_FISHING_BOBBER, thrower, world, luckOfTheSeaLevel, waitTimeReductionTicks, exuberanceLevel, bigCatchLevel, serendipityReelLevel, inventoryInsertion, foundry);
	}
	
	@Override
	public int getLineColor() {
		return 0xFFa10f1d;
	}
}
