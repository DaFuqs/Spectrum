package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.level.*;

public class BidentEntity extends BidentBaseEntity {
	
	public BidentEntity(Level world) {
		this(SpectrumEntityTypes.BIDENT.get(), world);
	}
	
	public BidentEntity(EntityType<? extends ThrownTrident> entityType, Level world) {
		super(entityType, world);
	}
	
}
