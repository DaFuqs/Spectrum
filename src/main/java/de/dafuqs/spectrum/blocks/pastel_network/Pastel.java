package de.dafuqs.spectrum.blocks.pastel_network;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.particle.render.*;
import net.neoforged.api.distmarker.*;

public class Pastel {
	
	@OnlyIn(Dist.CLIENT)
	private static ClientPastelNetworkManager clientManager;
	private static ServerPastelNetworkManager serverManager;
	
	@OnlyIn(Dist.CLIENT)
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
	
	@OnlyIn(Dist.CLIENT)
	public static void clearClientInstance() {
		getClientInstance().clearContent();
		EarlyRenderingParticleContainer.clear();
	}
	
	public static void clearServerInstance() {
		serverManager = null;
	}
	
}
