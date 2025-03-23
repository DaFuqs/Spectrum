package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.blocks.idols.*;
import de.dafuqs.spectrum.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class MoltenFishingBobberEntity extends SpectrumFishingBobberEntity {

	public MoltenFishingBobberEntity(EntityType<? extends SpectrumFishingBobberEntity> type, World world, int luckOfTheSeaLevel, int lureLevel, int exuberanceLevel, int bigCatchLevel, int serendipityReelLevel, boolean inventoryInsertion) {
		super(type, world, luckOfTheSeaLevel, lureLevel, exuberanceLevel, bigCatchLevel, serendipityReelLevel, inventoryInsertion, true);
	}
	
	public MoltenFishingBobberEntity(EntityType<? extends SpectrumFishingBobberEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public void tick() {
		World world = this.getWorld();
		super.tick();
		if (!world.isClient && age % 20 == 0 && isOnGround()) {
			FirestarterIdolBlock.causeFire((ServerWorld) this.getWorld(), getBlockPos(), Direction.DOWN);
		}
	}

	public MoltenFishingBobberEntity(PlayerEntity thrower, World world, int luckOfTheSeaLevel, int lureLevel, int exuberanceLevel, int bigCatchLevel, int serendipityReelLevel, boolean inventoryInsertion) {
		super(SpectrumEntityTypes.MOLTEN_FISHING_BOBBER, thrower, world, luckOfTheSeaLevel, lureLevel, exuberanceLevel, bigCatchLevel, serendipityReelLevel, inventoryInsertion, true);
	}
	
	@Override
	public void hookedEntityTick(Entity hookedEntity) {
		hookedEntity.setOnFireFor(2);
	}
	
}
