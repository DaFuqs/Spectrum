package de.dafuqs.spectrum.compat;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.ae2.*;
import de.dafuqs.spectrum.compat.create.*;
import de.dafuqs.spectrum.compat.gobber.*;
import net.fabricmc.loader.api.*;

import java.util.*;

public class SpectrumIntegrationPacks {
	
	protected static final Map<String, ModIntegrationPack> INTEGRATION_PACKS = new HashMap<>();
	
	public abstract static class ModIntegrationPack {
		public abstract void register();
		
		public abstract void registerClient();
	}
	
	protected static void registerIntegrationPack(String modId, ModIntegrationPack container) {
		if (!SpectrumCommon.CONFIG.IntegrationPacksToSkipLoading.contains(modId) && FabricLoader.getInstance().isModLoaded(modId)) {
			INTEGRATION_PACKS.put(modId, container);
		}
	}
	
	public static void register() {
		registerIntegrationPack("ae2", new AE2Compat());
		registerIntegrationPack("gobber2", new GobberCompat());
		registerIntegrationPack("create", new CreateCompat());
		
		for (ModIntegrationPack container : INTEGRATION_PACKS.values()) {
			container.register();
		}
	}
	
	public static void registerClient() {
		for (ModIntegrationPack container : INTEGRATION_PACKS.values()) {
			container.registerClient();
		}
	}
	
	public static boolean isIntegrationPackActive(String modId) {
		return INTEGRATION_PACKS.containsKey(modId);
	}
	
}
