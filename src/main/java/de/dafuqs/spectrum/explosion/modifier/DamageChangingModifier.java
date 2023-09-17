package de.dafuqs.spectrum.explosion.modifier;

import de.dafuqs.spectrum.explosion.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.particle.*;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class DamageChangingModifier extends ParticleAddingModifier {
	

	public DamageChangingModifier(ExplosionModifierType type, ParticleEffect effect, int color) {
		super(type, effect, color);
	}
	
	@Override
	public Optional<DamageSource> getDamageSource(@Nullable Entity owner) {
		if (owner == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(owner.getDamageSources().generic());
	}
	
}
