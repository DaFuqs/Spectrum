package de.dafuqs.spectrum.explosion;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.util.*;

public record ExplosionModifierType(int maxModifiersForType) {
	
	public boolean isCompatibleWith(ExplosionModifierType type) {
		return true;
	}
	
	public boolean acceptsArchetype(ExplosionArchetype archetype) {
		return true;
	}
	
	public Identifier getId() {
		return SpectrumRegistries.EXPLOSION_MODIFIER_TYPES.getId(this);
	}
	
}
