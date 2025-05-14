package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;

public class BidentEntity extends BidentBaseEntity {
	
	public BidentEntity(Level world) {
		this(SpectrumEntityTypes.BIDENT, world);
	}
	
	public BidentEntity(EntityType<? extends ThrownTrident> entityType, Level world) {
		super(entityType, world);
	}
	
	@Override
	protected void onHitBlock(BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);
	}
    
}
