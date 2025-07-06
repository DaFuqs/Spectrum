package de.dafuqs.spectrum;

import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.titration_barrel.*;
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
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.*;
import net.minecraft.server.packs.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;
import org.slf4j.*;

import java.util.*;

public class SpectrumCommon implements ModInitializer {
	
	// Miscellaneous registrars
	public static final DeferredRegistrar FUEL_REGISTRAR = new DeferredRegistrar();
	
	@Override
	public void onInitialize() {
		
		// Register internals
		SpectrumRegistries.register();
		InkColors.register();
		InkColorMixes.register();
		SpectrumEntityAttributes.register();
		SpectrumLoadConditions.register();
		
		logInfo("Registering Component Types...");
		SpectrumDataComponentTypes.register();
		
		logInfo("Registering Block / Item Color Registries...");
		ColorRegistry.registerColorRegistries();
		
		// Register ALL the stuff
		logInfo("Registering Status Effects...");
		SpectrumStatusEffects.register();
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
		logInfo("Registering Armor Materials...");
		SpectrumArmorMaterials.register();
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
		SpectrumRecipeScalings.init();
		SpectrumFusionShrineWorldEffects.register();
		SpectrumRecipeTypes.register();
		SpectrumRecipeSerializers.register();
		
		// Loot
		logInfo("Registering Loot Conditions & Functions...");
		SpectrumLootContextTypes.register();
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
		logInfo("Registering Variant Specific Predicates...");
		SpectrumItemSubPredicateTypes.register();
		SpectrumEntitySubPredicateTypes.register();
		
		logInfo("Registering Blocks and Items to Fuel Registry...");
		FUEL_REGISTRAR.flush();
		
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
		
		logInfo("Registering Networking Packets...");
		SpectrumC2SPackets.register();
		SpectrumS2CPackets.register();
		
		logInfo("Registering Data Loaders...");
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(NaturesStaffConversionDataLoader.INSTANCE);
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(EntityFishingDataLoader.INSTANCE);
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(CrystalApothecarySimulationsDataLoader.INSTANCE);
		
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
		
		logInfo("Registering Dispenser, Resonance & Present Unwrap Behaviors...");
		SpectrumDispenserBehaviors.register();
		SpectrumPresentUnpackBehaviors.register();
		SpectrumResonanceProcessorTypes.register();
		
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
		
		//noinspection
		ItemStorage.SIDED.registerForBlockEntity((be, d) -> Storage.empty(), SpectrumBlockEntities.HEARTBOUND_CHEST);
		//noinspection
		ItemStorage.SIDED.registerForBlockEntity((titrationBarrelBlockEntity, direction) -> {
			BlockState state = titrationBarrelBlockEntity.getBlockState();
			TitrationBarrelBlock.BarrelState barrelState = state.getValue(TitrationBarrelBlock.BARREL_STATE);
			if (barrelState == TitrationBarrelBlock.BarrelState.EMPTY || barrelState == TitrationBarrelBlock.BarrelState.FILLED) {
				return ItemStorage.SIDED.find(titrationBarrelBlockEntity.getLevel(), titrationBarrelBlockEntity.getBlockPos(), direction);
			}
			return null;
		}, SpectrumBlockEntities.TITRATION_BARREL);
		//noinspection
		ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.storage, SpectrumBlockEntities.BOTTOMLESS_BUNDLE);
		//noinspection
		FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.fluidStorage, SpectrumBlockEntities.FUSION_SHRINE);
		//noinspection
		FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.getFluidStorage(), SpectrumBlockEntities.TITRATION_BARREL);
		
		// Builtin Resource Packs
		logInfo("Registering Builtin Resource Packs...");
		Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(SpectrumCommon.MOD_ID);
		if (modContainer.isPresent()) {
			ResourceManagerHelper.registerBuiltinResourcePack(locate("spectrum_generation_1"), modContainer.get(), Component.nullToEmpty("Generation 1 Spectrum textures"), ResourcePackActivationType.NORMAL);
			ResourceManagerHelper.registerBuiltinResourcePack(locate("spectrum_programmer_art"), modContainer.get(), Component.nullToEmpty("Spectrum's Programmer Art"), ResourcePackActivationType.NORMAL);
		}
		
		logInfo("Common startup completed!");
	}
	
}
