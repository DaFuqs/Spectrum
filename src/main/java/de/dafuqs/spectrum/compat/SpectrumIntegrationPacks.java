package de.dafuqs.spectrum.compat;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.ae2.*;
import de.dafuqs.spectrum.compat.alloy_forgery.*;
import de.dafuqs.spectrum.compat.botania.*;
import de.dafuqs.spectrum.compat.gobber.*;
import de.dafuqs.spectrum.compat.travelersbackpack.*;
import net.fabricmc.loader.api.*;

import java.util.*;
import java.util.function.*;

public class SpectrumIntegrationPacks {
	
	protected static final Map<String, ModIntegrationPack> INTEGRATION_PACKS = new HashMap<>();
	
	
	public abstract static class ModIntegrationPack {
		public abstract void register();
		
		public abstract void registerClient();
		
		public void registerMultiblocks() {
		}
	}
	
	protected static void registerIntegrationPack(String modId, Supplier<ModIntegrationPack> container) {
		if (!SpectrumCommon.CONFIG.IntegrationPacksToSkipLoading.contains(modId) && FabricLoader.getInstance().isModLoaded(modId)) {
			INTEGRATION_PACKS.put(modId, container.get());
		}
	}
	
	public static final String AE2_ID = "ae2";
	public static final String GOBBER_ID = "gobber2";
	public static final String CREATE_ID = "create";
	public static final String MYTHIC_METALS_ID = "mythicmetals";
	public static final String ALLOY_FORGERY_ID = "alloy_forgery";
	public static final String TRAVELERS_BACKPACK_ID = "travelersbackpack";
	public static final String BOTANIA_ID = "botania";
	
	public static void register() {
		registerIntegrationPack(AE2_ID, () -> new AE2Compat());
		registerIntegrationPack(GOBBER_ID, () -> new GobberCompat());
		//registerIntegrationPack(CREATE_ID, () -> new CreateCompat());
		//registerIntegrationPack(MYTHIC_METALS_ID, () -> new MythicMetalsCompat());
		registerIntegrationPack(ALLOY_FORGERY_ID, () -> new AlloyForgeryCompat());
		registerIntegrationPack(TRAVELERS_BACKPACK_ID, () -> new TravelersBackpackCompat());
		registerIntegrationPack(BOTANIA_ID, () -> new BotaniaCompat());
		
		for (ModIntegrationPack container : INTEGRATION_PACKS.values()) {
			container.register();
		}
	}
	
	public static void registerClient() {
		for (ModIntegrationPack container : INTEGRATION_PACKS.values()) {
			container.registerClient();
		}
	}
	
	public static void registerMultiblocks() {
		for (ModIntegrationPack container : INTEGRATION_PACKS.values()) {
			container.registerMultiblocks();
		}
	}
	
	public static boolean isIntegrationPackActive(String modId) {
		return INTEGRATION_PACKS.containsKey(modId);
	}
	
}
