package de.dafuqs.spectrum.explosion.modifier;

import de.dafuqs.spectrum.explosion.*;

public class MoreBoomModifier extends ExplosionModifier {
	
	public MoreBoomModifier(ExplosionModifierType type, int displayColor) {
		super(type, displayColor);
	}
	
	/**
	 * This number exists exclusively so that the total amount of damage dealt by a maxed out conflux is 6k flat<p>
	 * 2500 * (1.3389 * 3) = 6000.45
	 * <p> <p>
	 * Autism gaming
	 */
	@Override
	public float getDamageModifier() {
		return 1.6778F;
	}
	
	@Override
	public float getBlastRadiusModifier() {
		return 1.5F;
	}
	
}
