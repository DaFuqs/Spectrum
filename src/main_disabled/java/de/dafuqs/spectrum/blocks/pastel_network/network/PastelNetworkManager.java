package de.dafuqs.spectrum.blocks.pastel_network.network;

import net.minecraft.world.level.*;

import java.util.*;

public interface PastelNetworkManager<W extends Level, N extends PastelNetwork<W>> {
	
	N createNetwork(W world, UUID uuid, int color);
	
	Optional<? extends N> getNetwork(UUID uuid);
	
	void removeNetwork(UUID uuid);
	
}
