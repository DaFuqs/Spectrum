package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.blocks.idols.*;
import de.dafuqs.spectrum.entity.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;

public class MoltenFishingBobberEntity extends SpectrumFishingBobberEntity {
	
	public MoltenFishingBobberEntity(EntityType<? extends SpectrumFishingBobberEntity> entityType, Level world) {
		super(entityType, world);
	}
	
	@Override
	public void tick() {
		Level world = this.level();
		super.tick();
		if (!world.isClientSide() && tickCount % 20 == 0 && onGround()) {
			FirestarterIdolBlock.causeFire((ServerLevel) this.level(), blockPosition(), Direction.DOWN);
		}
	}
	
	public MoltenFishingBobberEntity(Player thrower, Level world, int luckBonus, int waitTimeReductionTicks, int exuberanceLevel, int bigCatchLevel, int serendipityReelLevel, boolean inventoryInsertion) {
		super(SpectrumEntityTypes.MOLTEN_FISHING_BOBBER, thrower, world, luckBonus, waitTimeReductionTicks, exuberanceLevel, bigCatchLevel, serendipityReelLevel, inventoryInsertion, true);
	}
	
	@Override
	public void hookedEntityTick(Entity hookedEntity) {
		hookedEntity.igniteForSeconds(2);
	}
	
	@Override
	public int getLineColor() {
		return 0xFFff8e24;
	}
}
