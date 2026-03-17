package de.dafuqs.spectrum.compat;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.alloy_forgery.*;
import de.dafuqs.spectrum.compat.ears.*;
import de.dafuqs.spectrum.compat.exclusions_lib.*;
import de.dafuqs.spectrum.compat.idwtialsimmoedm.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import de.dafuqs.spectrum.compat.neepmeat.*;
import de.dafuqs.spectrum.compat.starry_skies.*;
import de.dafuqs.spectrum.compat.travelersbackpack.*;
import net.fabricmc.api.*;
import net.fabricmc.loader.api.*;

import java.util.*;
import java.util.function.*;

public class SpectrumIntegrationPacks {
	
	protected static final Map<String, ModIntegrationPack> INTEGRATION_PACKS = new HashMap<>();
	
	
	public abstract static class ModIntegrationPack {
		public abstract void register();
		
		public abstract void registerClient();
	}
	
	protected static void registerIntegrationPack(String modId, Supplier<ModIntegrationPack> container) {
		if (!SpectrumCommon.CONFIG.IntegrationPacksToSkipLoading.contains(modId) && FabricLoader.getInstance().isModLoaded(modId)) {
			INTEGRATION_PACKS.put(modId, container.get());
		}
	}
	
	public static final String AE2_ID = "ae2";
	public static final String GOBBER_ID = "gobber2";
	public static final String ALLOY_FORGERY_ID = "alloy_forgery";
	public static final String TRAVELERS_BACKPACK_ID = "travelersbackpack";
	public static final String BOTANIA_ID = "botania";
	public static final String MODONOMICON_ID = "modonomicon";
	public static final String CREATE_ID = "create";
	public static final String NEEPMEAT_ID = "neepmeat";
	public static final String EXCLUSIONS_LIB_ID = "exclusions_lib";
	public static final String STARRY_SKIES_ID = "starry_skies";
	
	// Client Only
	public static final String EARS_ID = "ears";
	public static final String IDWTIALSIMMOEDM_ID = "idwtialsimmoedm";

	@SuppressWarnings("Convert2MethodRef")
	public static void register() {
		registerIntegrationPack(MODONOMICON_ID, () -> new ModonomiconCompat());
		
		if (!FabricLoader.getInstance().isModLoaded(EXCLUSIONS_LIB_ID)) {
			ExclusionsLibCompat.registerNotPresent();
		}
		
		// registerIntegrationPack(AE2_ID, () -> new AE2Compat()); // stuck in 1.20.1
		// registerIntegrationPack(GOBBER_ID, () -> new GobberCompat()); // stuck in 1.20.1
		registerIntegrationPack(ALLOY_FORGERY_ID, () -> new AlloyForgeryCompat());
		registerIntegrationPack(TRAVELERS_BACKPACK_ID, () -> new TravelersBackpackCompat());
		// registerIntegrationPack(BOTANIA_ID, () -> new BotaniaCompat()); // stuck in 1.20.1
		registerIntegrationPack(NEEPMEAT_ID, () -> new NEEPMeatCompat());
		// registerIntegrationPack(CREATE_ID, () -> new CreateCompat()); // stuck in 1.20.1
		registerIntegrationPack(STARRY_SKIES_ID, () -> new StarrySkiesCompat());
		
		for (ModIntegrationPack container : INTEGRATION_PACKS.values()) {
			container.register();
		}
	}
	
	@Environment(EnvType.CLIENT)
	@SuppressWarnings("Convert2MethodRef")
	public static void registerClient() {
		registerIntegrationPack(EARS_ID, () -> new EarsCompat());
		registerIntegrationPack(IDWTIALSIMMOEDM_ID, () -> new IdwtialsimmoedmCompat());
		
		for (ModIntegrationPack container : INTEGRATION_PACKS.values()) {
			container.registerClient();
		}
	}
	
	public static boolean isIntegrationPackActive(String modId) {
		return INTEGRATION_PACKS.containsKey(modId);
	}
	
}
