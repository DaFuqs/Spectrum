package de.dafuqs.spectrum.api.item;

import net.minecraft.server.network.ServerPlayerEntity;

@FunctionalInterface
public
interface SoundProvider {
    void playSound(ServerPlayerEntity player);
}
