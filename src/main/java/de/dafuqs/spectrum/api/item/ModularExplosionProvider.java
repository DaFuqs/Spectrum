package de.dafuqs.spectrum.api.item;

/**
 * Something an ExplosionModifierSet can be attached to
 */
public interface ModularExplosionProvider {
	
	double getBaseExplosionBlastRadius();
	
	float getBaseExplosionDamage();
	
	int getMaxExplosionModifiers();
	
}
