package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.inventories.altar.AltarScreen;
import de.dafuqs.spectrum.inventories.altar.AltarScreenHandler;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class SpectrumScreenHandlerTypes {

    public static ScreenHandlerType<AltarScreenHandler> ALTAR;
    public static ScreenHandlerType<CraftingTabletScreenHandler> CRAFTING_TABLET;
    public static ScreenHandlerType<RestockingChestScreenHandler> RESTOCKING_CHEST;
    public static ScreenHandlerType<BedrockAnvilScreenHandler> BEDROCK_ANVIL;

    public static ScreenHandlerType<GenericSpectrumContainerScreenHandler> GENERIC_9X3;
    public static ScreenHandlerType<GenericSpectrumContainerScreenHandler> GENERIC_9X6;

    public static void register() {
        ALTAR = ScreenHandlerRegistry.registerExtended(SpectrumContainers.ALTAR, AltarScreenHandler::new);
        CRAFTING_TABLET = ScreenHandlerRegistry.registerSimple(SpectrumContainers.CRAFTING_TABLET, CraftingTabletScreenHandler::new);
        RESTOCKING_CHEST = ScreenHandlerRegistry.registerSimple(SpectrumContainers.RESTOCKING_CHEST, RestockingChestScreenHandler::new);
        BEDROCK_ANVIL = ScreenHandlerRegistry.registerSimple(SpectrumContainers.BEDROCK_ANVIL, BedrockAnvilScreenHandler::new);

        GENERIC_9X3 = ScreenHandlerRegistry.registerSimple(SpectrumContainers.GENERIC_9x3, GenericSpectrumContainerScreenHandler::createGeneric9x3_Tier1);
        GENERIC_9X6 = ScreenHandlerRegistry.registerSimple(SpectrumContainers.GENERIC_9x6, GenericSpectrumContainerScreenHandler::createGeneric9x6_Tier1);
    }

    public static void registerClient() {
        ScreenRegistry.register(SpectrumScreenHandlerTypes.ALTAR, AltarScreen::new);
        ScreenRegistry.register(SpectrumScreenHandlerTypes.CRAFTING_TABLET, CraftingTabletScreen::new);
        ScreenRegistry.register(SpectrumScreenHandlerTypes.RESTOCKING_CHEST, RestockingChestScreen::new);
        ScreenRegistry.register(SpectrumScreenHandlerTypes.BEDROCK_ANVIL, BedrockAnvilScreen::new);

        ScreenRegistry.register(SpectrumScreenHandlerTypes.GENERIC_9X3, GenericSpectrumContainerScreen::new);
        ScreenRegistry.register(SpectrumScreenHandlerTypes.GENERIC_9X6, GenericSpectrumContainerScreen::new);
    }

}
