package de.dafuqs.spectrum.explosion.modifier;

import de.dafuqs.spectrum.explosion.*;
import net.minecraft.core.particles.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

public abstract class DamageChangingModifier extends ParticleAddingModifier {
	
	
	public DamageChangingModifier(ExplosionModifierType type, ParticleOptions effect, int color) {
		super(type, effect, color);
	}
	
	@Override
	public Optional<DamageSource> getDamageSource(@Nullable LivingEntity owner) {
		if (owner == null) {
			return Optional.empty();
		}
		return Optional.of(owner.damageSources().generic());
	}
	
}
