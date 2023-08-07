package de.dafuqs.spectrum.explosion;

import net.minecraft.util.Identifier;

public class ExplosionEffectFamily {

    public final Identifier id;
    public final int maxConcurrentModifiers;

    public ExplosionEffectFamily(Identifier id, int maxConcurrentModifiers) {
        this.id = id;
        this.maxConcurrentModifiers = maxConcurrentModifiers;
    }

    public boolean isCompatibleWith(ExplosionEffectFamily family) {
        return true;
    }
    
    public boolean acceptsArchetype(Archetype archetype) {
        return true;
    }
}
