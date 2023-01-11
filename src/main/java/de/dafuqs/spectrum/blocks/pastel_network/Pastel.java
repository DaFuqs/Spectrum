package de.dafuqs.spectrum.blocks.pastel_network;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import net.fabricmc.api.*;

public class Pastel {

    @Environment(EnvType.CLIENT)
    private static PastelNetworkManager clientManager;
    private static ServerPastelNetworkManager serverManager;

    @Environment(EnvType.CLIENT)
    public static PastelNetworkManager getClientInstance() {
        if (clientManager == null) {
            clientManager = new ClientPastelNetworkManager();
        }
        return clientManager;
    }

    public static ServerPastelNetworkManager getServerInstance() {
        if (serverManager == null) {
            serverManager = ServerPastelNetworkManager.get(SpectrumCommon.minecraftServer.getOverworld());
        }
        return serverManager;
    }

    public static PastelNetworkManager getInstance(boolean client) {
        if (client) {
            return getClientInstance();
        } else {
            return getServerInstance();
        }
    }

    public static void clearClientInstance() {
        clientManager = null;
    }

    public static void clearServerInstance() {
        serverManager = null;
    }

}
