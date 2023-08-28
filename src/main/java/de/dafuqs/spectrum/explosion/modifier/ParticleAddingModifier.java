package de.dafuqs.spectrum.explosion.modifier;

import de.dafuqs.spectrum.explosion.*;
import net.minecraft.particle.*;

import java.util.*;

public class ParticleAddingModifier extends ExplosionModifier {
	
	private final ParticleEffect particleEffect;
	
	public ParticleAddingModifier(ExplosionModifierType type, ParticleEffect particleEffect, int displayColor) {
		super(type, displayColor);
		this.particleEffect = particleEffect;
	}
	
	@Override
	public Optional<ParticleEffect> getParticleEffects() {
		return Optional.of(particleEffect);
	}
	
}
