package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class LagoonFishingBobberEntity extends SpectrumFishingBobberEntity {
	
	public LagoonFishingBobberEntity(EntityType type, World world, int luckOfTheSeaLevel, int lureLevel, int exuberanceLevel, int bigCatchLevel, boolean inventoryInsertion, boolean foundry) {
		super(type, world, luckOfTheSeaLevel, lureLevel, exuberanceLevel, bigCatchLevel, inventoryInsertion, foundry);
	}
	
	public LagoonFishingBobberEntity(EntityType entityType, World world) {
		super(entityType, world);
	}
	
	public LagoonFishingBobberEntity(PlayerEntity thrower, World world, int luckOfTheSeaLevel, int lureLevel, int exuberanceLevel, int bigCatchLevel, boolean inventoryInsertion, boolean foundry) {
		super(SpectrumEntityTypes.LAGOON_FISHING_BOBBER, thrower, world, luckOfTheSeaLevel, lureLevel, exuberanceLevel, bigCatchLevel, inventoryInsertion, foundry);
	}
	
}
