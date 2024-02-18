package de.dafuqs.spectrum.api.status_effect;

/**
 * When an entity gets a Status Effect applied that
 * implements this, and it has that effect already,
 * the effects amplifier will be increased by the new amplifier,
 * instead of getting both two StatusEffectInstances applied.
 * The original duration will be preserved, even if the additional
 * duration would be longer.
 */
public interface StackableStatusEffect {

}
