package de.dafuqs.spectrum.api.item;

import net.minecraft.server.network.*;

@FunctionalInterface
public
interface SoundProvider {
	void playSound(ServerPlayerEntity player);
}
