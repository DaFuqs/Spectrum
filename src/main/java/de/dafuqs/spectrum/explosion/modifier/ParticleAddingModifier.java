package de.dafuqs.spectrum.explosion.modifier;

import de.dafuqs.spectrum.explosion.*;
import net.minecraft.core.particles.*;

import java.util.*;

public class ParticleAddingModifier extends ExplosionModifier {
	
	private final ParticleOptions particleEffect;
	
	public ParticleAddingModifier(ExplosionModifierType type, ParticleOptions particleEffect, int displayColor) {
		super(type, displayColor);
		this.particleEffect = particleEffect;
	}
	
	@Override
	public Optional<ParticleOptions> getParticleEffects() {
		return Optional.of(particleEffect);
	}
	
}
