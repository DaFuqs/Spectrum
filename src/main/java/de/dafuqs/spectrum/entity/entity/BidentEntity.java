package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.spells.*;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.util.hit.*;
import net.minecraft.world.*;

public class BidentEntity extends BidentBaseEntity {
	
	public BidentEntity(World world) {
		this(SpectrumEntityTypes.BIDENT, world);
	}
	
	public BidentEntity(EntityType<? extends TridentEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
	}
    
}
