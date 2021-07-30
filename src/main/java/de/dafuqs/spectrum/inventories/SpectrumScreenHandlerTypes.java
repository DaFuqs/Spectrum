package de.dafuqs.spectrum.inventories;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class SpectrumScreenHandlerTypes {

    public static ScreenHandlerType<AltarScreenHandler> ALTAR;
    public static ScreenHandlerType<CraftingTabletScreenHandler> CRAFTING_TABLET;
    public static ScreenHandlerType<RestockingChestScreenHandler> RESTOCKING_CHEST;

    public static void register() {
        ALTAR = ScreenHandlerRegistry.registerSimple(SpectrumContainers.ALTAR, AltarScreenHandler::new);
        CRAFTING_TABLET = ScreenHandlerRegistry.registerSimple(SpectrumContainers.CRAFTING_TABLET, CraftingTabletScreenHandler::new);
        RESTOCKING_CHEST = ScreenHandlerRegistry.registerSimple(SpectrumContainers.RESTOCKING_CHEST, RestockingChestScreenHandler::new);
    }

    public static void registerClient() {
        ScreenRegistry.register(SpectrumScreenHandlerTypes.ALTAR, AltarScreen::new);
        ScreenRegistry.register(SpectrumScreenHandlerTypes.CRAFTING_TABLET, CraftingTabletScreen::new);
        ScreenRegistry.register(SpectrumScreenHandlerTypes.RESTOCKING_CHEST, RestockingChestScreen::new);
    }

}
