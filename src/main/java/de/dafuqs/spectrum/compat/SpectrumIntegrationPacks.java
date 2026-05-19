package de.dafuqs.spectrum.compat;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.ae2.*;
import de.dafuqs.spectrum.compat.alloy_forgery.*;
import de.dafuqs.spectrum.compat.create.*;
import de.dafuqs.spectrum.compat.exclusions_lib.*;
import de.dafuqs.spectrum.compat.gobber.*;
import de.dafuqs.spectrum.compat.malum.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import de.dafuqs.spectrum.compat.travelersbackpack.*;
import de.dafuqs.spectrum.config.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.event.lifecycle.*;
import net.neoforged.fml.loading.*;

import java.util.*;
import java.util.function.*;

public class SpectrumIntegrationPacks {
	
	protected static final Map<String, ModIntegrationPack> INTEGRATION_PACKS = new HashMap<>();
	
	public abstract static class ModIntegrationPack {
		public abstract void register(IEventBus modBus);
		
		public abstract void registerClient(FMLClientSetupEvent event);
	}
	
	protected static void registerIntegrationPack(String modId, Supplier<ModIntegrationPack> container) {
		if (!SpectrumConfig.CONFIG.IntegrationPacksToSkipLoading.get().contains(modId) && SpectrumIntegrationPacks.isModLoaded(modId)) {
			INTEGRATION_PACKS.put(modId, container.get());
		}
	}
	
	public static final String AE2_ID = "ae2";
	public static final String GOBBER_ID = "gobber2";
	public static final String ALLOY_FORGERY_ID = "alloy_forgery";
	public static final String TRAVELERS_BACKPACK_ID = "travelersbackpack";
	//public static final String BOTANIA_ID = "botania";
	public static final String MODONOMICON_ID = "modonomicon";
	public static final String CREATE_ID = "create";
	public static final String MALUM_ID = "malum";
	public static final String EXCLUSIONS_LIB_ID = "exclusions_lib";
	public static final String IMMERSIVE_PORTALS_ID = "immersive_portals_core";
	//public static final String STARRY_SKIES_ID = "starry_skies";
	
	// Client Only
	public static final String EARS_ID = "ears";
	public static final String IDWTIALSIMMOEDM_ID = "idwtialsimmoedm";

	@SuppressWarnings("Convert2MethodRef")
	public static void register(IEventBus modBus) {
		registerIntegrationPack(MODONOMICON_ID, () -> new ModonomiconCompat());
		
		if (!SpectrumIntegrationPacks.isModLoaded(EXCLUSIONS_LIB_ID)) {
			ExclusionsLibCompat.registerNotPresent(modBus);
		}
		
		registerIntegrationPack(AE2_ID, () -> new AE2Compat());
		registerIntegrationPack(GOBBER_ID, () -> new GobberCompat());
		registerIntegrationPack(ALLOY_FORGERY_ID, () -> new AlloyForgeryCompat());
		registerIntegrationPack(TRAVELERS_BACKPACK_ID, () -> new TravelersBackpackCompat());
		//registerIntegrationPack(BOTANIA_ID, () -> new BotaniaCompat());
		registerIntegrationPack(MALUM_ID, () -> new MalumCompat());
		registerIntegrationPack(CREATE_ID, () -> new CreateCompat());
		//registerIntegrationPack(STARRY_SKIES_ID, () -> new StarrySkiesCompat());
		
		for (ModIntegrationPack container : INTEGRATION_PACKS.values()) {
			container.register(modBus);
		}
	}
	
	public static void registerClient(FMLClientSetupEvent event) {
		for (ModIntegrationPack container : INTEGRATION_PACKS.values()) {
			container.registerClient(event);
		}
	}
	
	public static boolean isIntegrationPackActive(String modId) {
		return INTEGRATION_PACKS.containsKey(modId);
	}
	
	public static boolean isModLoaded(String modId) {
		return LoadingModList.get().getModFileById(modId) != null;
	}
	
}
