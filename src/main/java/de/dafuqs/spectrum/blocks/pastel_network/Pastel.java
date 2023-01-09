package de.dafuqs.spectrum.blocks.pastel_network;

import de.dafuqs.spectrum.blocks.pastel_network.transfer.*;
import net.fabricmc.api.*;

public class Pastel {

    @Environment(EnvType.CLIENT)
    private static PastelNetworkManager clientManager;
    private static ServerPastelNetworkManager serverManager;

    @Environment(EnvType.CLIENT)
    public static PastelNetworkManager getClientInstance() {
        if (clientManager == null) {
            clientManager = new PastelNetworkManager();
        }
        return clientManager;
    }

    public static ServerPastelNetworkManager getServerInstance() {
        if (serverManager == null) {
            serverManager = new ServerPastelNetworkManager();
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
