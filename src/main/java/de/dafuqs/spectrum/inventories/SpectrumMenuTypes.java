package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.block.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.world.flag.*;
import net.minecraft.world.inventory.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.extensions.*;
import net.neoforged.neoforge.network.*;
import net.neoforged.neoforge.registries.*;

public class SpectrumMenuTypes {
	
	private static final DeferredRegister<MenuType<?>> REGISTRAR = DeferredRegister.create(Registries.MENU, SpectrumCommon.MOD_ID);
	
	public static MenuType<PaintbrushScreenHandler> PAINTBRUSH = registerSimple(SpectrumCommon.locate("paintbrush"), PaintbrushScreenHandler::new);
	public static MenuType<WorkstaffScreenHandler> WORKSTAFF = registerSimple(SpectrumCommon.locate("workstaff"), WorkstaffScreenHandler::new);
	public static MenuType<PedestalScreenHandler> PEDESTAL = registerExtended(SpectrumCommon.locate("pedestal"), PedestalScreenHandler::new);
	public static MenuType<CraftingTabletScreenHandler> CRAFTING_TABLET = registerSimple(SpectrumCommon.locate("crafting_tablet"), CraftingTabletScreenHandler::new);
	public static MenuType<FabricationChestScreenHandler> FABRICATION_CHEST = registerSimple(SpectrumCommon.locate("fabrication_chest"), FabricationChestScreenHandler::new);
	public static MenuType<BedrockAnvilScreenHandler> BEDROCK_ANVIL = registerSimple(SpectrumCommon.locate("bedrock_anvil"), BedrockAnvilScreenHandler::new);
	public static MenuType<ParticleSpawnerScreenHandler> PARTICLE_SPAWNER = registerExtended(SpectrumCommon.locate("particle_spawner"), ParticleSpawnerScreenHandler::new);
	public static MenuType<CompactingChestScreenHandler> COMPACTING_CHEST = registerExtended(SpectrumCommon.locate("compacting_chest"), CompactingChestScreenHandler::new);
	public static MenuType<BlackHoleChestScreenHandler> BLACK_HOLE_CHEST = registerExtended(SpectrumCommon.locate("black_hole_chest"), BlackHoleChestScreenHandler::new);
	public static MenuType<PotionWorkshopScreenHandler> POTION_WORKSHOP = registerSimple(SpectrumCommon.locate("potion_workshop"), PotionWorkshopScreenHandler::new);
	public static MenuType<FilteringScreenHandler> FILTERING = registerExtended(SpectrumCommon.locate("filtering"), FilteringScreenHandler::new);
	public static MenuType<BagOfHoldingScreenHandler> BAG_OF_HOLDING = registerSimple(SpectrumCommon.locate("bag_of_holding"), BagOfHoldingScreenHandler::new);
	
	// Ink
	public static MenuType<InkStorageScreenHandler> INK_STORAGE = registerExtended(SpectrumCommon.locate("ink_storage"), InkStorageScreenHandler::new);
	public static MenuType<ColorPickerScreenHandler> COLOR_PICKER = registerExtended(SpectrumCommon.locate("color_picker"), ColorPickerScreenHandler::new);
	public static MenuType<TintingStationScreenHandler> TINTING_STATION = registerExtended(SpectrumCommon.locate("tinting_station"), TintingStationScreenHandler::new);
	public static MenuType<CinderhearthScreenHandler> CINDERHEARTH = registerExtended(SpectrumCommon.locate("cinderhearth"), CinderhearthScreenHandler::new);
	
	// Generic
	public static MenuType<GenericSpectrumContainerScreenHandler> GENERIC_TIER1_9X3 = registerSimple(SpectrumCommon.locate("generic_tier1_9x3"), GenericSpectrumContainerScreenHandler::createGeneric9x3_Tier1);
	public static MenuType<GenericSpectrumContainerScreenHandler> GENERIC_TIER2_9X3 = registerSimple(SpectrumCommon.locate("generic_tier2_9x3"), GenericSpectrumContainerScreenHandler::createGeneric9x3_Tier2);
	public static MenuType<GenericSpectrumContainerScreenHandler> GENERIC_TIER3_9X3 = registerSimple(SpectrumCommon.locate("generic_tier3_9x3"), GenericSpectrumContainerScreenHandler::createGeneric9x3_Tier3);
	public static MenuType<GenericSpectrumContainerScreenHandler> GENERIC_TIER1_9X6 = registerSimple(SpectrumCommon.locate("generic_tier1_9x6"), GenericSpectrumContainerScreenHandler::createGeneric9x6_Tier1);
	public static MenuType<GenericSpectrumContainerScreenHandler> GENERIC_TIER2_9X6 = registerSimple(SpectrumCommon.locate("generic_tier2_9x6"), GenericSpectrumContainerScreenHandler::createGeneric9x6_Tier2);
	public static MenuType<GenericSpectrumContainerScreenHandler> GENERIC_TIER3_9X6 = registerSimple(SpectrumCommon.locate("generic_tier3_9x6"), GenericSpectrumContainerScreenHandler::createGeneric9x6_Tier3);
	public static MenuType<Spectrum3x3ContainerScreenHandler> GENERIC_TIER1_3X3 = registerSimple(SpectrumCommon.locate("generic_tier1_3x3"), Spectrum3x3ContainerScreenHandler::createTier1);
	public static MenuType<Spectrum3x3ContainerScreenHandler> GENERIC_TIER2_3X3 = registerSimple(SpectrumCommon.locate("generic_tier2_3x3"), Spectrum3x3ContainerScreenHandler::createTier2);
	public static MenuType<Spectrum3x3ContainerScreenHandler> GENERIC_TIER3_3X3 = registerSimple(SpectrumCommon.locate("generic_tier3_3x3"), Spectrum3x3ContainerScreenHandler::createTier3);
	
	public static <T extends AbstractContainerMenu> MenuType<T> registerSimple(ResourceLocation id, MenuType.MenuSupplier<T> factory) {
		MenuType<T> type = new MenuType<>(factory, FeatureFlags.VANILLA_SET);
		REGISTRAR.register(id.getPath(), () -> type);
		return type;
	}
	
	public static <T extends AbstractContainerMenu> MenuType<T> registerExtended(ResourceLocation id, IContainerFactory<T> factory) {
		MenuType<T> type = IMenuTypeExtension.create(factory);
		REGISTRAR.register(id.getPath(), () -> type);
		return type;
	}
	
	public static void register(IEventBus eventBus) {
		REGISTRAR.register(eventBus);
	}
	
	public static void registerClient(RegisterMenuScreensEvent event) {
		event.register(SpectrumMenuTypes.PAINTBRUSH, PaintbrushScreen::new);
		event.register(SpectrumMenuTypes.WORKSTAFF, WorkstaffScreen::new);
		event.register(SpectrumMenuTypes.PEDESTAL, PedestalScreen::new);
		event.register(SpectrumMenuTypes.CRAFTING_TABLET, CraftingTabletScreen::new);
		event.register(SpectrumMenuTypes.FABRICATION_CHEST, FabricationChestScreen::new);
		event.register(SpectrumMenuTypes.BEDROCK_ANVIL, BedrockAnvilScreen::new);
		event.register(SpectrumMenuTypes.PARTICLE_SPAWNER, ParticleSpawnerScreen::new);
		event.register(SpectrumMenuTypes.COMPACTING_CHEST, CompactingChestScreen::new);
		event.register(SpectrumMenuTypes.BLACK_HOLE_CHEST, BlackHoleChestScreen::new);
		event.register(SpectrumMenuTypes.POTION_WORKSHOP, PotionWorkshopScreen::new);
		event.register(SpectrumMenuTypes.FILTERING, FilteringScreen::new);
		event.register(SpectrumMenuTypes.BAG_OF_HOLDING, ContainerScreen::new);
		
		// Ink
		MenuScreens.ScreenConstructor<InkStorageScreenHandler, InkStorageScreen<InkStorageScreenHandler>> inkStorageScreenConstructor = InkStorageScreen::new; // tricking generics with this simple trick
		event.register(SpectrumMenuTypes.INK_STORAGE, inkStorageScreenConstructor);
		event.register(SpectrumMenuTypes.COLOR_PICKER, ColorPickerScreen::new);
		event.register(SpectrumMenuTypes.TINTING_STATION, TintingStationScreen::new);
		event.register(SpectrumMenuTypes.CINDERHEARTH, CinderhearthScreen::new);
		
		// Generic
		event.register(SpectrumMenuTypes.GENERIC_TIER1_9X3, SpectrumGenericContainerScreen::new);
		event.register(SpectrumMenuTypes.GENERIC_TIER2_9X3, SpectrumGenericContainerScreen::new);
		event.register(SpectrumMenuTypes.GENERIC_TIER3_9X3, SpectrumGenericContainerScreen::new);
		event.register(SpectrumMenuTypes.GENERIC_TIER1_9X6, SpectrumGenericContainerScreen::new);
		event.register(SpectrumMenuTypes.GENERIC_TIER2_9X6, SpectrumGenericContainerScreen::new);
		event.register(SpectrumMenuTypes.GENERIC_TIER3_9X6, SpectrumGenericContainerScreen::new);
		event.register(SpectrumMenuTypes.GENERIC_TIER1_3X3, Spectrum3x3ContainerScreen::new);
		event.register(SpectrumMenuTypes.GENERIC_TIER2_3X3, Spectrum3x3ContainerScreen::new);
		event.register(SpectrumMenuTypes.GENERIC_TIER3_3X3, Spectrum3x3ContainerScreen::new);
	}
	
}
