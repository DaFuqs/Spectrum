package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import net.minecraft.entity.Entity;
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
		super(SpectrumEntityTypes.MOLTEN_FISHING_BOBBER, thrower, world, luckOfTheSeaLevel, lureLevel);
	}
	
	@Override
	public void hookedEntityTick(Entity hookedEntity) {
		hookedEntity.setOnFireFor(2);
	}
	
	@Override
	public boolean doesRenderOnFire() {
		return true;
		// no, always on is more fun
		/*if(this.world == null) {
			return true;
		}
		FluidState fluidState = this.world.getFluidState(this.getBlockPos());
		return fluidState.isEmpty() || fluidState.isIn(FluidTags.LAVA);*/
	}
	
}
