package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface PastelNetworkManager {
    
    //PastelNetwork createNetwork(World world, UUID uuid);
    
    PastelNetwork JoinOrCreateNetwork(PastelNodeBlockEntity node, @Nullable UUID uuid);

    void connectNodes(PastelNodeBlockEntity node, PastelNodeBlockEntity parent);
    
    //void removeEmptyNetwork(PastelNetwork network);
    
    void removeNode(PastelNodeBlockEntity node, NodeRemovalReason reason);
    
    //@Nullable PastelNetwork getNetwork(UUID uuid);
    
}
