package de.dafuqs.spectrum.explosion.modifier;

import de.dafuqs.spectrum.explosion.*;

public class KillZoneModifier extends ExplosionModifier {
	
	private final float killZoneRadius;
	private final float killZoneDamageModifier;
	
	public KillZoneModifier(ExplosionModifierType type, float killZoneRadius, float killZoneDamageModifier, int color) {
		super(type, color);
		this.killZoneRadius = killZoneRadius;
		this.killZoneDamageModifier = killZoneDamageModifier;
	}
	
	@Override
	public float getKillZoneRadius() {
		return killZoneRadius;
	}
	
	@Override
	public float getKillZoneDamageModifier() {
		return killZoneDamageModifier;
	}
	
}
