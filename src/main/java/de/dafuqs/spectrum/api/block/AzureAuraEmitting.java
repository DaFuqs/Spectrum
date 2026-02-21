package de.dafuqs.spectrum.api.block;

/**
 * Blocks with this interface increase nearby azure auras.
 * They regularity need to call
 * `BlockAuraSoundInstance.addToExistingInstanceOrCreateNewOne(world, pos);`
 * clientside, like via `animateTick`, to add them to an azure aura
 */
public interface AzureAuraEmitting {

}
