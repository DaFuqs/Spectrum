package de.dafuqs.spectrum.explosion;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.resources.*;

public class ExplosionModifierType {
	
	protected ExplosionArchetype applicableArchetype;
	protected int maxModifiersForType;
	
	public ExplosionModifierType(ExplosionArchetype applicableArchetype, int maxModifiersForType) {
		this.applicableArchetype = applicableArchetype;
		this.maxModifiersForType = maxModifiersForType;
	}
	
}
