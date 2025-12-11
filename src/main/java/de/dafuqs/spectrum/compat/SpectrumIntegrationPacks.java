package de.dafuqs.spectrum.compat;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.ae2.*;
import de.dafuqs.spectrum.compat.alloy_forgery.*;
import de.dafuqs.spectrum.compat.botania.*;
import de.dafuqs.spectrum.compat.create.*;
import de.dafuqs.spectrum.compat.exclusions_lib.*;
import de.dafuqs.spectrum.compat.farmersdelight.*;
import de.dafuqs.spectrum.compat.gobber.*;
import de.dafuqs.spectrum.compat.malum.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import de.dafuqs.spectrum.compat.neepmeat.*;
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
	
	public static final String CONNECTOR_ID = "connectormod";
	public static final String AE2_ID = "ae2";
	public static final String GOBBER_ID = "gobber2";
	public static final String ALLOY_FORGERY_ID = "alloy_forgery";
	public static final String TRAVELERS_BACKPACK_ID = "travelersbackpack";
	public static final String BOTANIA_ID = "botania";
	public static final String MODONOMICON_ID = "modonomicon";
	public static final String CREATE_ID = "create";
	public static final String FARMERSDELIGHT_ID = "farmersdelight";
	public static final String NEEPMEAT_ID = "neepmeat";
	public static final String MALUM_ID = "malum";
	public static final String EXCLUSIONS_LIB_ID = "exclusions_lib";

	@SuppressWarnings("Convert2MethodRef")
	public static void register() {
		registerIntegrationPack(MODONOMICON_ID, () -> new ModonomiconCompat());
		
		if (!FabricLoader.getInstance().isModLoaded(EXCLUSIONS_LIB_ID)) {
			ExclusionsLibCompat.registerNotPresent();
		}
		
		if (!FabricLoader.getInstance().isModLoaded(CONNECTOR_ID)) {
			// Connector on forge causes a lot of issues since most
			// code bases of forge mods differ quite a lot from their fabric counterparts

			// Farbic Exclusive Mods
			registerIntegrationPack(ALLOY_FORGERY_ID, () -> new AlloyForgeryCompat());
			registerIntegrationPack(NEEPMEAT_ID, () -> new NEEPMeatCompat());
			
			// Connector Problematic ones, see #792 on github
			registerIntegrationPack(MALUM_ID, () -> new MalumCompat()); //NoSuchField error
			registerIntegrationPack(TRAVELERS_BACKPACK_ID, () -> new TravelersBackpackCompat());
			
			registerIntegrationPack(CREATE_ID, () -> new CreateCompat()); // this one needs a bit of work with tags...
				//^ extra note: create on fabric is a bit different jsyk
		}
		
		// These will work across connector, for notaton sake, I will label the version and tested FORGE version here
		//   All were tested for 1.20.1 - FORGE 47.4.10 w/ FFAPI 0.92.6+1.11.14,
		//   Connector 1.0.0-beta.47, and ConnectorExtras-1.11.2
		registerIntegrationPack(FARMERSDELIGHT_ID, () -> new FDCompat()); //working with ver 1.20.1-1.2.9
		registerIntegrationPack(AE2_ID, () -> new AE2Compat()); // working with forge-15.4.10
		registerIntegrationPack(GOBBER_ID, () -> new GobberCompat()); // working with 1.20.1-2.8.9
		registerIntegrationPack(BOTANIA_ID, () -> new BotaniaCompat()); // working with 1.20.1-450-FORGE
		
		for (ModIntegrationPack container : INTEGRATION_PACKS.values()) {
			container.register();
		}
	}
	
	@Environment(EnvType.CLIENT)
	public static void registerClient() {
		for (ModIntegrationPack container : INTEGRATION_PACKS.values()) {
			container.registerClient();
		}
	}
	
	public static boolean isIntegrationPackActive(String modId) {
		return INTEGRATION_PACKS.containsKey(modId);
	}
	
}
