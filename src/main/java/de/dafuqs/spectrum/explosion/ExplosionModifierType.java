package de.dafuqs.spectrum.explosion;

import net.minecraft.util.*;

/**
 * @param id
 * @param maxModifiersForType
 */
public record ExplosionModifierType(Identifier id, int maxModifiersForType) {
	
	public boolean isCompatibleWith(ExplosionModifierType type) {
		return true;
	}
	
	public boolean acceptsArchetype(ExplosionArchetype archetype) {
		return true;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
}
