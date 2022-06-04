package de.dafuqs.spectrum.inventories;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumScreenHandlerTypes {
	
	public static ScreenHandlerType<PedestalScreenHandler> PEDESTAL;
	public static ScreenHandlerType<CraftingTabletScreenHandler> CRAFTING_TABLET;
	public static ScreenHandlerType<RestockingChestScreenHandler> RESTOCKING_CHEST;
	public static ScreenHandlerType<BedrockAnvilScreenHandler> BEDROCK_ANVIL;
	public static ScreenHandlerType<ParticleSpawnerScreenHandler> PARTICLE_SPAWNER;
	public static ScreenHandlerType<CompactingChestScreenHandler> COMPACTING_CHEST;
	public static ScreenHandlerType<SuckingChestScreenHandler> SUCKING_CHEST;
	public static ScreenHandlerType<PotionWorkshopScreenHandler> POTION_WORKSHOP;
	public static ScreenHandlerType<ColorPickerScreenHandler> COLOR_PICKER;
	
	public static ScreenHandlerType<GenericSpectrumContainerScreenHandler> GENERIC_TIER1_9X3;
	public static ScreenHandlerType<GenericSpectrumContainerScreenHandler> GENERIC_TIER2_9X3;
	public static ScreenHandlerType<GenericSpectrumContainerScreenHandler> GENERIC_TIER3_9X3;
	
	public static ScreenHandlerType<GenericSpectrumContainerScreenHandler> GENERIC_TIER1_9X6;
	public static ScreenHandlerType<GenericSpectrumContainerScreenHandler> GENERIC_TIER2_9X6;
	public static ScreenHandlerType<GenericSpectrumContainerScreenHandler> GENERIC_TIER3_9X6;
	
	public static ScreenHandlerType<Spectrum3x3ContainerScreenHandler> GENERIC_TIER1_3X3;
	public static ScreenHandlerType<Spectrum3x3ContainerScreenHandler> GENERIC_TIER2_3X3;
	public static ScreenHandlerType<Spectrum3x3ContainerScreenHandler> GENERIC_TIER3_3X3;
	
	public static <T extends ScreenHandler> ScreenHandlerType<T> registerSimple(Identifier id, ScreenHandlerType.Factory<T> factory) {
		ScreenHandlerType<T> type = new ScreenHandlerType<>(factory);
		return Registry.register(Registry.SCREEN_HANDLER, id, type);
	}
	
	public static <T extends ScreenHandler> ScreenHandlerType<T> registerExtended(Identifier id, ExtendedScreenHandlerType.ExtendedFactory<T> factory) {
		ScreenHandlerType<T> type = new ExtendedScreenHandlerType<>(factory);
		return Registry.register(Registry.SCREEN_HANDLER, id, type);
	}
	
	public static void register() {
		PEDESTAL = registerExtended(SpectrumContainers.PEDESTAL, PedestalScreenHandler::new);
		PARTICLE_SPAWNER = registerExtended(SpectrumContainers.PARTICLE_SPAWNER, ParticleSpawnerScreenHandler::new);
		COMPACTING_CHEST = registerExtended(SpectrumContainers.COMPACTING_CHEST, CompactingChestScreenHandler::new);
		SUCKING_CHEST = registerExtended(SpectrumContainers.SUCKING_CHEST, SuckingChestScreenHandler::new);
		COLOR_PICKER = registerExtended(SpectrumContainers.COLOR_PICKER, ColorPickerScreenHandler::new);
		
		CRAFTING_TABLET = registerSimple(SpectrumContainers.CRAFTING_TABLET, CraftingTabletScreenHandler::new);
		RESTOCKING_CHEST = registerSimple(SpectrumContainers.RESTOCKING_CHEST, RestockingChestScreenHandler::new);
		BEDROCK_ANVIL = registerSimple(SpectrumContainers.BEDROCK_ANVIL, BedrockAnvilScreenHandler::new);
		POTION_WORKSHOP = registerSimple(SpectrumContainers.POTION_WORKSHOP, PotionWorkshopScreenHandler::new);
		
		GENERIC_TIER1_9X3 = registerSimple(SpectrumContainers.GENERIC_TIER1_9x3, GenericSpectrumContainerScreenHandler::createGeneric9x3_Tier1);
		GENERIC_TIER2_9X3 = registerSimple(SpectrumContainers.GENERIC_TIER2_9x3, GenericSpectrumContainerScreenHandler::createGeneric9x3_Tier2);
		GENERIC_TIER3_9X3 = registerSimple(SpectrumContainers.GENERIC_TIER3_9x3, GenericSpectrumContainerScreenHandler::createGeneric9x3_Tier3);
		
		GENERIC_TIER1_9X6 = registerSimple(SpectrumContainers.GENERIC_TIER1_9x6, GenericSpectrumContainerScreenHandler::createGeneric9x6_Tier1);
		GENERIC_TIER2_9X6 = registerSimple(SpectrumContainers.GENERIC_TIER2_9x6, GenericSpectrumContainerScreenHandler::createGeneric9x6_Tier2);
		GENERIC_TIER3_9X6 = registerSimple(SpectrumContainers.GENERIC_TIER3_9x6, GenericSpectrumContainerScreenHandler::createGeneric9x6_Tier3);

		GENERIC_TIER1_3X3 = registerSimple(SpectrumContainers.GENERIC_TIER1_3X3, Spectrum3x3ContainerScreenHandler::createTier1);
		GENERIC_TIER2_3X3 = registerSimple(SpectrumContainers.GENERIC_TIER2_3X3, Spectrum3x3ContainerScreenHandler::createTier2);
		GENERIC_TIER3_3X3 = registerSimple(SpectrumContainers.GENERIC_TIER3_3X3, Spectrum3x3ContainerScreenHandler::createTier3);
	}

	public static void registerClient() {
		HandledScreens.register(SpectrumScreenHandlerTypes.PEDESTAL, PedestalScreen::new);
		HandledScreens.register(SpectrumScreenHandlerTypes.CRAFTING_TABLET, CraftingTabletScreen::new);
		HandledScreens.register(SpectrumScreenHandlerTypes.RESTOCKING_CHEST, RestockingChestScreen::new);
		HandledScreens.register(SpectrumScreenHandlerTypes.BEDROCK_ANVIL, BedrockAnvilScreen::new);
		HandledScreens.register(SpectrumScreenHandlerTypes.PARTICLE_SPAWNER, ParticleSpawnerScreen::new);
		HandledScreens.register(SpectrumScreenHandlerTypes.COMPACTING_CHEST, CompactingChestScreen::new);
		HandledScreens.register(SpectrumScreenHandlerTypes.SUCKING_CHEST, SuckingChestScreen::new);
		HandledScreens.register(SpectrumScreenHandlerTypes.POTION_WORKSHOP, PotionWorkshopScreen::new);
		HandledScreens.register(SpectrumScreenHandlerTypes.COLOR_PICKER, ColorPickerScreen::new);
		
		HandledScreens.register(SpectrumScreenHandlerTypes.GENERIC_TIER1_9X3, SpectrumGenericContainerScreen::new);
		HandledScreens.register(SpectrumScreenHandlerTypes.GENERIC_TIER2_9X3, SpectrumGenericContainerScreen::new);
		HandledScreens.register(SpectrumScreenHandlerTypes.GENERIC_TIER3_9X3, SpectrumGenericContainerScreen::new);
		HandledScreens.register(SpectrumScreenHandlerTypes.GENERIC_TIER1_9X6, SpectrumGenericContainerScreen::new);
		HandledScreens.register(SpectrumScreenHandlerTypes.GENERIC_TIER2_9X6, SpectrumGenericContainerScreen::new);
		HandledScreens.register(SpectrumScreenHandlerTypes.GENERIC_TIER3_9X6, SpectrumGenericContainerScreen::new);
		HandledScreens.register(SpectrumScreenHandlerTypes.GENERIC_TIER1_3X3, Spectrum3x3ContainerScreen::new);
		HandledScreens.register(SpectrumScreenHandlerTypes.GENERIC_TIER2_3X3, Spectrum3x3ContainerScreen::new);
		HandledScreens.register(SpectrumScreenHandlerTypes.GENERIC_TIER3_3X3, Spectrum3x3ContainerScreen::new);
	}
	
}
