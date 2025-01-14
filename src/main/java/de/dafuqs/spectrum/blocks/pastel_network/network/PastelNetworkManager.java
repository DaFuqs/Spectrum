package de.dafuqs.spectrum.blocks.pastel_network.network;

import net.minecraft.world.*;

import java.util.*;

public interface PastelNetworkManager {
	
	PastelNetwork createNetwork(World world, UUID uuid);
	
    Optional<? extends PastelNetwork> getNetwork(UUID uuid);

}
