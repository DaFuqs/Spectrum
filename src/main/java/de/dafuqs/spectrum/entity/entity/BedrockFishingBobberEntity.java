package de.dafuqs.spectrum.entity.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class BedrockFishingBobberEntity extends SpectrumFishingBobberEntity {
	
	public BedrockFishingBobberEntity(EntityType type, World world, int luckOfTheSeaLevel, int lureLevel) {
		super(type, world, luckOfTheSeaLevel, lureLevel);
	}
	
	public BedrockFishingBobberEntity(EntityType entityType, World world) {
		super(entityType, world);
	}
	
	public BedrockFishingBobberEntity(PlayerEntity thrower, World world, int luckOfTheSeaLevel, int lureLevel) {
		super(thrower, world, luckOfTheSeaLevel, lureLevel);
	}
	
	
	
}
