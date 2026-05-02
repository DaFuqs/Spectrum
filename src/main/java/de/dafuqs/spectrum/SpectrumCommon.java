package de.dafuqs.spectrum;

import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.attachment_types.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.payloads.*;
import de.dafuqs.spectrum.capabilities.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.events.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.loot.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import net.minecraft.resources.*;
import net.minecraft.server.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.*;
import net.neoforged.fml.common.*;
import net.neoforged.fml.config.*;
import net.neoforged.fml.event.lifecycle.*;
import net.neoforged.neoforge.common.*;
import net.neoforged.neoforge.event.*;
import net.neoforged.neoforge.event.server.*;
import net.neoforged.neoforge.event.tick.*;
import net.neoforged.neoforge.network.event.*;
import net.neoforged.neoforge.network.registration.*;
import org.jetbrains.annotations.*;
import org.slf4j.*;

import java.util.*;
import java.util.function.*;

@Mod(SpectrumCommon.MOD_ID)
public class SpectrumCommon {
	
	public static final String MOD_ID = "spectrum";
	
	public static final Logger LOGGER = LoggerFactory.getLogger("Spectrum");
	
	public static void logInfo(String message) {
		LOGGER.info("{}", message);
	}
	
	public static void logWarning(String message) {
		LOGGER.warn("{}", message);
	}
	
	public static void logError(String message) {
		LOGGER.error("{}", message);
	}
	
	public static ResourceLocation locate(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}
	
	/**
	 * This is the Spectrum analogue of Identifier.of, but instead of defaulting to the namespace 'minecraft', it defaults to 'spectrum'.
	 *
	 * @param id The stringified identifier to parse
	 * @return The parsed identifier
	 */
	public static ResourceLocation ofSpectrumDefaulted(String id) {
		int i = id.indexOf(':');
		String path = id.substring(i + 1);
		String namespace = i > 0 ? id.substring(0, i) : SpectrumCommon.MOD_ID;
		return ResourceLocation.fromNamespaceAndPath(namespace, path.toLowerCase(Locale.ROOT));
	}
	
	// TODO: remove if possible
	@Nullable
	public static MinecraftServer minecraftServer;
	
	public SpectrumCommon(ModContainer container, IEventBus modBus) {
		logInfo("Starting Common Startup");
		
		//Register the config
		container.registerConfig(ModConfig.Type.STARTUP, SpectrumConfig.CONFIG_SPEC);
		
		// Register internals
		modBus.addListener(SpectrumRegistries::registerBuiltInRegistries);
		modBus.addListener(SpectrumRegistries::registerDynamicRegistries);
		InkColors.register();
		InkColorMixes.register();
		SpectrumEntityAttributes.register(modBus);
		
		// Register ALL the stuff
		logInfo("Registering Status Effects...");
		SpectrumMobEffects.register(modBus);
		logInfo("Registering Advancement Criteria...");
		SpectrumAdvancementCriteria.register(modBus);
		logInfo("Registering Particle Types...");
		SpectrumParticleTypes.register(modBus);
		logInfo("Registering Sound Events...");
		SpectrumSoundEvents.register(modBus);
		logInfo("Registering BlockSound Groups...");
		SpectrumSoundTypes.register();
		logInfo("Registering Fluids...");
		NeoForgeMod.enableMilkFluid();
		SpectrumFluids.register(modBus);
		logInfo("Registering Armor Materials...");
		SpectrumArmorMaterials.register(modBus);
		SpectrumDataComponentTypes.register(modBus);
		SpectrumPotions.register(modBus);
		logInfo("Registering Blocks...");
		SpectrumBlocks.register(modBus);
		logInfo("Registering Items...");
		SpectrumItems.register(modBus);
		SpectrumItemGroups.register(modBus);
		logInfo("Registering Block Entities...");
		SpectrumBlockEntities.register(modBus);
		SpectrumPastelPayloadTypes.register(modBus);
		SpectrumPastelPayloads.register(modBus);
		modBus.addListener(SpectrumBlockEntities::addBlockEntityTypeBlocks);
		SpectrumPastelUpgradeSignatures.register(modBus);
		
		// Worldgen
		logInfo("Registering Features...");
		SpectrumFeatures.register(modBus);
		logInfo("Registering Structure Types...");
		SpectrumStructureTypes.register(modBus);
		
		// Dimension effects
		logInfo("Registering Dimension Sound Effects...");
		DimensionReverb.setup();
		
		// Recipes
		logInfo("Registering Recipe Types...");
		SpectrumRecipeScalings.init();
		SpectrumFusionShrineWorldEffects.register(modBus);
		SpectrumRecipeTypes.register(modBus);
		SpectrumRecipeSerializers.register(modBus);
		
		// Loot
		logInfo("Registering Loot Conditions & Functions...");
		SpectrumLootContextParamSets.register();
		SpectrumLootFunctionTypes.register(modBus);
		SpectrumLootConditionTypes.register(modBus);
		
		// GUI
		logInfo("Registering Screen Handler Types...");
		SpectrumScreenHandlerTypes.register(modBus);
		
		logInfo("Registering Enchantment Drops...");
		SpectrumGlobalLootModifierSerializers.register(modBus);
		logInfo("Registering Variant Specific Predicates...");
		SpectrumItemSubPredicateTypes.register(modBus);
		SpectrumEntitySubPredicateTypes.register(modBus);
		
		logInfo("Registering Entities...");
		SpectrumTrackedDataHandlerRegistry.register(modBus);
		SpectrumEntityTypes.register(modBus);
		
		logInfo("Registering Omni Accelerator Projectiles & Behaviors...");
		SpectrumOmniAcceleratorProjectiles.register();
		SpectrumItemProjectileBehaviors.register();
		SpectrumEntityColorProcessors.register();
		
		logInfo("Registering Commands...");
		NeoForge.EVENT_BUS.addListener(SpectrumCommands::register);
		
		logInfo("Registering Networking Packets...");
		modBus.addListener(SpectrumC2SPackets::register);
		modBus.addListener(RegisterPayloadHandlersEvent.class, (event) -> {
				PayloadRegistrar registrar = event.registrar("1");
				SpectrumS2CPackets.register(registrar);
			}
		);

		logInfo("Registering Data Loaders...");
		NeoForge.EVENT_BUS.addListener((Consumer<AddReloadListenerEvent>) event -> {
			event.addListener(NaturesStaffConversionDataLoader.INSTANCE);
			event.addListener(EntityFishingDataLoader.INSTANCE);
			event.addListener(CrystalApothecarySimulationsDataLoader.INSTANCE);
			ColorRegistry.registerColorRegistries(event);
		});
		
		NeoForge.EVENT_BUS.addListener((Consumer<ServerStartingEvent>) event -> {
			SpectrumCommon.logInfo("Fetching server instance...");
			minecraftServer = event.getServer();
		});
		
		NeoForge.EVENT_BUS.addListener((Consumer<ServerStoppedEvent>) event -> {
			Pastel.clearServerInstance();
			minecraftServer = null;
		});
		NeoForge.EVENT_BUS.addListener((Consumer<PlayerTickEvent.Post>) event -> {
			Player player = event.getEntity();
			MiscPlayerDataAttachmentType.get(player).tick();
		});
		modBus.addListener(SpectrumCapabilities::register);
		
		logInfo("Registering Game Events...");
		SpectrumGameEvents.register(modBus);
		SpectrumPositionSources.register(modBus);

		logInfo("Registering Dispenser, Resonance & Present Unwrap Behaviors...");
		modBus.addListener((Consumer<FMLCommonSetupEvent>) event -> event.enqueueWork(() -> {
			SpectrumDispenserBehaviors.register();
			SpectrumPresentUnpackBehaviors.register();
			SpectrumItemGroups.registerSubTabs();
		}));
		
		SpectrumResonanceProcessorTypes.register(modBus);
		
		logInfo("Registering Resource Conditions...");
		SpectrumResourceConditions.register(modBus);
		logInfo("Registering Structure WeightedPool Element Types...");
		SpectrumStructurePoolElementTypes.register(modBus);
		logInfo("Registering Event Listeners...");
		SpectrumEventListeners.register();
		logInfo("Registering Tree Decorator Types...");
		SpectrumTreeDecoratorTypes.register(modBus);
		
		logInfo("Registering Attachments...");
		SpectrumAttachmentTypes.register(modBus);
		
		logInfo("Setting up server side Mod Compat...");
		SpectrumIntegrationPacks.register(modBus);
		
		logInfo("Common startup completed!");
	}
	
	/**
	 * When initializing a block entity, world can still be null
	 * Therefore we use the RecipeManager reference from MinecraftServer
	 * This in turn does not work on clients connected to dedicated servers, though
	 * since SpectrumCommon.minecraftServer is null
	 */
	public static Optional<RecipeManager> getRecipeManager(@Nullable Level world) {
		return world == null ? minecraftServer == null ? Optional.empty() : Optional.of(minecraftServer.getRecipeManager()) : Optional.of(world.getRecipeManager());
	}
	
}
