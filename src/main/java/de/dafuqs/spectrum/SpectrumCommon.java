package de.dafuqs.spectrum;

import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.compat.reverb.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.events.*;
import de.dafuqs.spectrum.explosion.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.loot.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import me.shedaniel.autoconfig.*;
import me.shedaniel.autoconfig.serializer.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.resource.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.fabricmc.loader.api.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.tag.*;
import net.minecraft.resource.*;
import net.minecraft.server.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import org.slf4j.*;

import java.util.*;

public class SpectrumCommon implements ModInitializer {
	
	public static final String MOD_ID = "spectrum";
	
	public static final Logger LOGGER = LoggerFactory.getLogger("Spectrum");
	public static final Map<Identifier, TagKey<Item>> CACHED_ITEM_TAG_MAP = new HashMap<>();
	public static SpectrumConfig CONFIG;

	public static void logInfo(String message) {
		LOGGER.info("[Spectrum] " + message);
	}
	
	public static void logWarning(String message) {
		LOGGER.warn("[Spectrum] " + message);
	}
	
	public static void logError(String message) {
		LOGGER.error("[Spectrum] " + message);
	}
	
	public static Identifier locate(String name) {
		return new Identifier(MOD_ID, name);
	}
	
	// Will be null when playing on a dedicated server!
	@Nullable
	public static MinecraftServer minecraftServer;
	
	static {
		//Set up config
		logInfo("Loading config file...");
		AutoConfig.register(SpectrumConfig.class, JanksonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(SpectrumConfig.class).getConfig();
		logInfo("Finished loading config file.");
	}
	
	@Override
	public void onInitialize() {
		logInfo("Starting Common Startup");
		
		// Register internals
		SpectrumRegistries.register();
		InkColors.register();
		InkColorMixes.register();
		SpectrumEntityAttributes.register();
		
		logInfo("Registering Banner Patterns...");
		SpectrumBannerPatterns.register();
		
		logInfo("Registering Block / Item Color Registries...");
		ColorRegistry.registerColorRegistries();
		
		// Register ALL the stuff
		logInfo("Registering Status Effects...");
		SpectrumStatusEffects.register();
		SpectrumStatusEffectTags.register();
		logInfo("Registering Advancement Criteria...");
		SpectrumAdvancementCriteria.register();
		logInfo("Registering Particle Types...");
		SpectrumParticleTypes.register();
		logInfo("Registering Sound Events...");
		SpectrumSoundEvents.register();
		logInfo("Registering BlockSound Groups...");
		SpectrumBlockSoundGroups.register();
		logInfo("Registering Fluids...");
		SpectrumFluids.register();
		logInfo("Registering Enchantments...");
		SpectrumEnchantments.register();
		logInfo("Registering Blocks...");
		SpectrumBlocks.register();
		logInfo("Registering Items...");
		SpectrumPotions.register();
		SpectrumItems.register();
		SpectrumItemGroups.register();
		logInfo("Registering Block Entities...");
		SpectrumBlockEntities.register();

		// Pastel
		logInfo("Registering Pastel Upgrades...");
		SpectrumPastelUpgrades.register();
		logInfo("Registering Stamp Categories...");
		SpectrumStampDataCategories.register();
		
		// Worldgen
		logInfo("Registering Features...");
		SpectrumFeatures.register();
		logInfo("Registering Biome Modifications...");
		SpectrumPlacedFeatures.addBiomeModifications();
		logInfo("Registering Structure Types...");
		SpectrumStructureTypes.register();
		
		// Dimension
		logInfo("Registering Dimension...");
		SpectrumDimensions.register();
		
		// Dimension effects
		logInfo("Registering Dimension Sound Effects...");
		DimensionReverb.setup();
		
		// Recipes
		logInfo("Registering Recipe Types...");
		SpectrumFusionShrineWorldEffects.register();
		SpectrumRecipeTypes.registerSerializer();
		
		// Loot
		logInfo("Registering Loot Conditions & Functions...");
		SpectrumLootConditionTypes.register();
		SpectrumLootFunctionTypes.register();
		
		logInfo("Setting up server side Mod Compat...");
		SpectrumIntegrationPacks.register();
		
		// GUI
		logInfo("Registering Screen Handler Types...");
		SpectrumScreenHandlerTypes.register();
		
		logInfo("Registering Default Item Stack Damage Immunities...");
		SpectrumItemDamageImmunities.registerDefaultItemStackImmunities();
		logInfo("Registering Enchantment Drops...");
		SpectrumLootPoolModifiers.setup();
		logInfo("Registering Type Specific Predicates...");
		SpectrumTypeSpecificPredicates.register();
		
		logInfo("Registering Items to Fuel Registry...");
		SpectrumItems.registerFuelRegistry();
		
		logInfo("Registering Entities...");
		SpectrumTrackedDataHandlerRegistry.register();
		SpectrumEntityTypes.register();
		
		logInfo("Registering Omni Accelerator Projectiles & Behaviors...");
		SpectrumOmniAcceleratorProjectiles.register();
		SpectrumItemProjectileBehaviors.register();
		
		SpectrumEntityColorProcessors.register();
		SpectrumItemProviders.register();
		
		logInfo("Registering Commands...");
		SpectrumCommands.register();
		
		logInfo("Registering Client To ServerPackage Receivers...");
		SpectrumC2SPacketReceiver.registerC2SReceivers();
		
		logInfo("Registering Data Loaders...");
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(NaturesStaffConversionDataLoader.INSTANCE);
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(EntityFishingDataLoader.INSTANCE);
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(CrystalApothecarySimulationsDataLoader.INSTANCE);
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(ResonanceDropsDataLoader.INSTANCE);
		
		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			SpectrumCommon.logInfo("Fetching server instance...");
			minecraftServer = server;
		});
		
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			Pastel.clearServerInstance();
			minecraftServer = null;
		});
		
		logInfo("Adding to Fabric's Registries...");
		SpectrumFlammableBlocks.register();
		SpectrumStrippableBlocks.register();
		SpectrumWaxableBlocks.register();
		SpectrumTillableBlocks.register();
		SpectrumCompostableBlocks.register();
		
		logInfo("Registering Game Events...");
		SpectrumGameEvents.register();
		SpectrumPositionSources.register();
		
		logInfo("Registering Explosion Effects & Providers...");
		ExplosionModifiers.register();
		ExplosionModifierProviders.register();
		logInfo("Registering Special Recipes...");
		SpectrumCustomRecipeSerializers.registerRecipeSerializers();
		
		logInfo("Registering Dispenser, Resonance & Present Unwrap Behaviors...");
		SpectrumDispenserBehaviors.register();
		SpectrumPresentUnpackBehaviors.register();
		SpectrumResonanceProcessors.register();
		
		logInfo("Registering Resource Conditions...");
		SpectrumResourceConditions.register();
		logInfo("Registering Structure WeightedPool Element Types...");
		SpectrumStructurePoolElementTypes.register();
		logInfo("Registering Event Listeners...");
		SpectrumEventListeners.register();
		logInfo("Registering Path Node Types...");
		SpectrumPathNodeTypes.register();
		logInfo("Registering Tree Decorator Types...");
		SpectrumTreeDecoratorTypes.register();
		
		//noinspection UnstableApiUsage
		ItemStorage.SIDED.registerForBlockEntity((be, d) -> Storage.empty(), SpectrumBlockEntities.HEARTBOUND_CHEST);
		//noinspection UnstableApiUsage
		ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.storage, SpectrumBlockEntities.BOTTOMLESS_BUNDLE);
		//noinspection UnstableApiUsage
		FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidStorage, SpectrumBlockEntities.FUSION_SHRINE);
		//noinspection UnstableApiUsage
		FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.getFluidStorage(), SpectrumBlockEntities.TITRATION_BARREL);
		
		// Builtin Resource Packs
		logInfo("Registering Builtin Resource Packs...");
		Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(SpectrumCommon.MOD_ID);
		if (modContainer.isPresent()) {
			ResourceManagerHelper.registerBuiltinResourcePack(locate("spectrum_style_amethyst"), modContainer.get(), Text.of("Spectrum Style Amethyst"), ResourcePackActivationType.NORMAL);
			ResourceManagerHelper.registerBuiltinResourcePack(locate("spectrum_generation_1"), modContainer.get(), Text.of("Generation 1 Spectrum textures"), ResourcePackActivationType.NORMAL);
			ResourceManagerHelper.registerBuiltinResourcePack(locate("spectrum_programmer_art"), modContainer.get(), Text.of("Spectrum's Programmer Art"), ResourcePackActivationType.NORMAL);
		}
		
		logInfo("Common startup completed!");
	}
	
	/**
	 * When initializing a block entity, world can still be null
	 * Therefore we use the RecipeManager reference from MinecraftServer
	 * This in turn does not work on clients connected to dedicated servers, though
	 * since SpectrumCommon.minecraftServer is null
	 */
	public static Optional<RecipeManager> getRecipeManager(@Nullable World world) {
		return world == null ? minecraftServer == null ? Optional.empty() : Optional.of(minecraftServer.getRecipeManager()) : Optional.of(world.getRecipeManager());
	}
	
}
