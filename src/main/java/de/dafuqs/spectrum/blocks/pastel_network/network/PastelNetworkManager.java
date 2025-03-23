package de.dafuqs.spectrum.blocks.pastel_network.network;

import net.minecraft.world.*;

import java.util.*;

public interface PastelNetworkManager<W extends World, N extends PastelNetwork<W>> {
	
	N createNetwork(W world, UUID uuid);
	
	Optional<? extends N> getNetwork(UUID uuid);
	
	void removeNetwork(UUID uuid);
	
}
