package de.dafuqs.spectrum.inventories;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class SpectrumScreenHandlerTypes {


	public static ScreenHandlerType<PedestalScreenHandler> PEDESTAL;
	public static ScreenHandlerType<CraftingTabletScreenHandler> CRAFTING_TABLET;
	public static ScreenHandlerType<RestockingChestScreenHandler> RESTOCKING_CHEST;
	public static ScreenHandlerType<BedrockAnvilScreenHandler> BEDROCK_ANVIL;
	public static ScreenHandlerType<ParticleSpawnerScreenHandler> PARTICLE_SPAWNER;
	public static ScreenHandlerType<CompactingChestScreenHandler> COMPACTING_CHEST;
	public static ScreenHandlerType<SuckingChestScreenHandler> SUCKING_CHEST;

	public static ScreenHandlerType<GenericSpectrumContainerScreenHandler> GENERIC_TIER1_9X3;
	public static ScreenHandlerType<GenericSpectrumContainerScreenHandler> GENERIC_TIER1_9X6;
	public static ScreenHandlerType<Spectrum3x3ContainerScreenHandler> GENERIC_TIER1_3X3;
	public static ScreenHandlerType<Spectrum3x3ContainerScreenHandler> GENERIC_TIER2_3X3;
	public static ScreenHandlerType<Spectrum3x3ContainerScreenHandler> GENERIC_TIER3_3X3;

	public static void register() {
		PEDESTAL = ScreenHandlerRegistry.registerExtended(SpectrumContainers.PEDESTAL, PedestalScreenHandler::new);
		CRAFTING_TABLET = ScreenHandlerRegistry.registerSimple(SpectrumContainers.CRAFTING_TABLET, CraftingTabletScreenHandler::new);
		RESTOCKING_CHEST = ScreenHandlerRegistry.registerSimple(SpectrumContainers.RESTOCKING_CHEST, RestockingChestScreenHandler::new);
		BEDROCK_ANVIL = ScreenHandlerRegistry.registerSimple(SpectrumContainers.BEDROCK_ANVIL, BedrockAnvilScreenHandler::new);
		PARTICLE_SPAWNER = ScreenHandlerRegistry.registerExtended(SpectrumContainers.PARTICLE_SPAWNER, ParticleSpawnerScreenHandler::new);
		COMPACTING_CHEST = ScreenHandlerRegistry.registerExtended(SpectrumContainers.COMPACTING_CHEST, CompactingChestScreenHandler::new);
		SUCKING_CHEST = ScreenHandlerRegistry.registerExtended(SpectrumContainers.SUCKING_CHEST, SuckingChestScreenHandler::new);

		GENERIC_TIER1_9X3 = ScreenHandlerRegistry.registerSimple(SpectrumContainers.GENERIC_TIER1_9x3, GenericSpectrumContainerScreenHandler::createGeneric9x3_Tier1);
		GENERIC_TIER1_9X6 = ScreenHandlerRegistry.registerSimple(SpectrumContainers.GENERIC_TIER1_9x6, GenericSpectrumContainerScreenHandler::createGeneric9x6_Tier1);

		GENERIC_TIER1_3X3 = ScreenHandlerRegistry.registerSimple(SpectrumContainers.GENERIC_TIER1_3X3, Spectrum3x3ContainerScreenHandler::createTier1);
		GENERIC_TIER2_3X3 = ScreenHandlerRegistry.registerSimple(SpectrumContainers.GENERIC_TIER2_3X3, Spectrum3x3ContainerScreenHandler::createTier2);
		GENERIC_TIER3_3X3 = ScreenHandlerRegistry.registerSimple(SpectrumContainers.GENERIC_TIER3_3X3, Spectrum3x3ContainerScreenHandler::createTier3);
	}

	public static void registerClient() {
		ScreenRegistry.register(SpectrumScreenHandlerTypes.PEDESTAL, PedestalScreen::new);
		ScreenRegistry.register(SpectrumScreenHandlerTypes.CRAFTING_TABLET, CraftingTabletScreen::new);
		ScreenRegistry.register(SpectrumScreenHandlerTypes.RESTOCKING_CHEST, RestockingChestScreen::new);
		ScreenRegistry.register(SpectrumScreenHandlerTypes.BEDROCK_ANVIL, BedrockAnvilScreen::new);
		ScreenRegistry.register(SpectrumScreenHandlerTypes.PARTICLE_SPAWNER, ParticleSpawnerScreen::new);
		ScreenRegistry.register(SpectrumScreenHandlerTypes.COMPACTING_CHEST, CompactingChestScreen::new);
		ScreenRegistry.register(SpectrumScreenHandlerTypes.SUCKING_CHEST, SuckingChestScreen::new);

		ScreenRegistry.register(SpectrumScreenHandlerTypes.GENERIC_TIER1_9X3, SpectrumGenericContainerScreen::new);
		ScreenRegistry.register(SpectrumScreenHandlerTypes.GENERIC_TIER1_9X6, SpectrumGenericContainerScreen::new);
		ScreenRegistry.register(SpectrumScreenHandlerTypes.GENERIC_TIER1_3X3, Spectrum3x3ContainerScreen::new);
		ScreenRegistry.register(SpectrumScreenHandlerTypes.GENERIC_TIER2_3X3, Spectrum3x3ContainerScreen::new);
		ScreenRegistry.register(SpectrumScreenHandlerTypes.GENERIC_TIER3_3X3, Spectrum3x3ContainerScreen::new);
	}

}
