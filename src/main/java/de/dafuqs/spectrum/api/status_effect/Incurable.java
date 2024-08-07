package de.dafuqs.spectrum.api.status_effect;


import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;

public interface Incurable {

    void spectrum$setIncurable(boolean incurable);

    boolean spectrum$isIncurable();

    static boolean isIncurable(StatusEffectInstance instance) {
        var type = instance.getEffectType();
        if (type == SpectrumStatusEffects.ETERNAL_SLUMBER || type == SpectrumStatusEffects.FATAL_SLUMBER)
            return false;

        return ((Incurable) instance).spectrum$isIncurable();
    }
}
