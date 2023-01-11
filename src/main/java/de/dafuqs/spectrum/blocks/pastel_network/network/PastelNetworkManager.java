package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface PastelNetworkManager {

    PastelNetwork createNetwork(World world, UUID uuid);

    PastelNetwork joinNetwork(PastelNodeBlockEntity node, @Nullable UUID uuid);

    void remove(PastelNetwork network);

    void tick();

    @Nullable PastelNetwork getNetwork(UUID uuid);

}
