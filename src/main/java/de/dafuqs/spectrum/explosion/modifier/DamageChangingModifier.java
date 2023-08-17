package de.dafuqs.spectrum.explosion.modifier;

import de.dafuqs.spectrum.explosion.*;
import net.minecraft.entity.damage.*;
import net.minecraft.particle.*;
import net.minecraft.util.*;

import java.util.*;

public class DamageChangingModifier extends ParticleAddingModifier {
    
    private final DamageSource damageSource;
    
    public DamageChangingModifier(Identifier id, ExplosionModifierType type, DamageSource damageSource, ParticleEffect effect, int color) {
        super(id, type, effect, color);
        this.damageSource = damageSource;
    }
    
    @Override
    public Optional<DamageSource> getDamageSource() {
        return Optional.of(damageSource);
    }
    
}
