package de.dafuqs.spectrum.blocks.pastel_network;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.particle.render.*;

public class Pastel {
	
	private static ClientPastelNetworkManager clientManager;
	private static ServerPastelNetworkManager serverManager;
	
	public static ClientPastelNetworkManager getClientInstance() {
		if (clientManager == null) {
			clientManager = new ClientPastelNetworkManager();
		}
		return clientManager;
	}
	
	public static ServerPastelNetworkManager getServerInstance() {
		if (serverManager == null && SpectrumCommon.minecraftServer != null) {
			serverManager = ServerPastelNetworkManager.get(SpectrumCommon.minecraftServer.overworld());
		}
		return serverManager;
	}
	
	public static PastelNetworkManager<?, ?> getInstance(boolean client) {
		if (client) {
			return getClientInstance();
		} else {
			return getServerInstance();
		}
	}
	
	public static void clearClientInstance() {
		getClientInstance().clearContent();
		EarlyRenderingParticleContainer.clear();
	}
	
	public static void clearServerInstance() {
		serverManager = null;
	}
	
}
