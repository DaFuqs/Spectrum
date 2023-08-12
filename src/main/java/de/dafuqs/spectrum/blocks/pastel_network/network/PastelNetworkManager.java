package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface PastelNetworkManager {
    
    //PastelNetwork createNetwork(World world, UUID uuid);
    
    PastelNetwork joinNetwork(PastelNodeBlockEntity node, @Nullable UUID uuid);
    
    //void removeEmptyNetwork(PastelNetwork network);
    
    void removeNode(PastelNodeBlockEntity node, NodeRemovalReason reason);
    
    //@Nullable PastelNetwork getNetwork(UUID uuid);
    
}
