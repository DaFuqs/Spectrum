package de.dafuqs.spectrum.explosion;

public enum ExplosionArchetype {
	COSMETIC(false, false),
	DESTROY_BLOCKS(true, false),
	DAMAGE_ENTITIES(false, true),
	ALL(true, true);
	
	final boolean affectsBlocks;
	final boolean affectsEntities;
	
	ExplosionArchetype(boolean affectsBlocks, boolean affectsEntities) {
		this.affectsBlocks = affectsBlocks;
		this.affectsEntities = affectsEntities;
	}
	
}
