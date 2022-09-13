package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class MoltenFishingBobberEntity extends SpectrumFishingBobberEntity {
	
	public MoltenFishingBobberEntity(EntityType type, World world, int luckOfTheSeaLevel, int lureLevel, int exuberanceLevel, boolean foundry) {
		super(type, world, luckOfTheSeaLevel, lureLevel, exuberanceLevel, foundry);
	}
	
	public MoltenFishingBobberEntity(EntityType entityType, World world) {
		super(entityType, world);
	}
	
	public MoltenFishingBobberEntity(PlayerEntity thrower, World world, int luckOfTheSeaLevel, int lureLevel, int exuberanceLevel, boolean foundry) {
		super(SpectrumEntityTypes.MOLTEN_FISHING_BOBBER, thrower, world, luckOfTheSeaLevel, lureLevel, exuberanceLevel, foundry);
	}
	
	@Override
	public void hookedEntityTick(Entity hookedEntity) {
		hookedEntity.setOnFireFor(2);
	}
	
}
