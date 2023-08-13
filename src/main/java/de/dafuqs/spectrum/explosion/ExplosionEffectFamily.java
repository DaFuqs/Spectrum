package de.dafuqs.spectrum.explosion;

import net.minecraft.util.Identifier;

import java.util.List;

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

    public boolean canApplyTo(List<ExplosionEffectModifier> effectModifierList) {
        int occurrences = 0;
        for (ExplosionEffectModifier explosionEffectModifier : effectModifierList) {
            if (explosionEffectModifier.family == this) {
                occurrences++;
            }
            else if(!isCompatibleWith(explosionEffectModifier.family))
                return false;
        }

        return occurrences < maxConcurrentModifiers;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
