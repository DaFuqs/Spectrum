package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.blocks.mob_blocks.FirestarterMobBlock;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class MoltenFishingBobberEntity extends SpectrumFishingBobberEntity {
	
	public MoltenFishingBobberEntity(EntityType type, World world, int luckOfTheSeaLevel, int lureLevel, int exuberanceLevel, int bigCatchLevel, boolean inventoryInsertion) {
		super(type, world, luckOfTheSeaLevel, lureLevel, exuberanceLevel, bigCatchLevel, inventoryInsertion, true);
	}
	
	public MoltenFishingBobberEntity(EntityType entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public void tick() {
		super.tick();
		if(!world.isClient && age % 20 == 0 && isOnGround()) {
			FirestarterMobBlock.causeFire((ServerWorld) world, getBlockPos(), Direction.DOWN);
		}
	}
	
	public MoltenFishingBobberEntity(PlayerEntity thrower, World world, int luckOfTheSeaLevel, int lureLevel, int exuberanceLevel, int bigCatchLevel, boolean inventoryInsertion) {
		super(SpectrumEntityTypes.MOLTEN_FISHING_BOBBER, thrower, world, luckOfTheSeaLevel, lureLevel, exuberanceLevel, bigCatchLevel, inventoryInsertion, true);
	}
	
	@Override
	public void hookedEntityTick(Entity hookedEntity) {
		hookedEntity.setOnFireFor(2);
	}
	
}
