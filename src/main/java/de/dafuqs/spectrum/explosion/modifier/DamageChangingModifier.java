package de.dafuqs.spectrum.explosion.modifier;

import de.dafuqs.spectrum.explosion.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.particle.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class DamageChangingModifier extends ParticleAddingModifier {
	
	private final DamageSource damageSource;
	
	public DamageChangingModifier(ExplosionModifierType type, DamageSource damageSource, ParticleEffect effect, int color) {
		super(type, effect, color);
		this.damageSource = damageSource;
	}
	
	@Override
	public Optional<DamageSource> getDamageSource(@Nullable Entity owner) {
		return Optional.of(damageSource);
	}
	
}
