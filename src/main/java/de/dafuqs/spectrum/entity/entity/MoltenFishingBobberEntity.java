package de.dafuqs.spectrum.entity.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class MoltenFishingBobberEntity extends SpectrumFishingBobberEntity {
	
	public MoltenFishingBobberEntity(EntityType type, World world, int luckOfTheSeaLevel, int lureLevel) {
		super(type, world, luckOfTheSeaLevel, lureLevel);
	}
	
	public MoltenFishingBobberEntity(EntityType entityType, World world) {
		super(entityType, world);
	}
	
	public MoltenFishingBobberEntity(PlayerEntity thrower, World world, int luckOfTheSeaLevel, int lureLevel) {
		super(thrower, world, luckOfTheSeaLevel, lureLevel);
	}
	
}
