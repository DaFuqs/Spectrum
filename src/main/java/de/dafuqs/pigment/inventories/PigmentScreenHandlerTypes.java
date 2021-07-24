package de.dafuqs.pigment.inventories;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class PigmentScreenHandlerTypes {

    public static ScreenHandlerType<AltarScreenHandler> ALTAR;
    public static ScreenHandlerType<CraftingTabletScreenHandler> CRAFTING_TABLET;
    public static ScreenHandlerType<RestockingChestScreenHandler> RESTOCKING_CHEST;

    public static void register() {
        ALTAR = ScreenHandlerRegistry.registerSimple(PigmentContainers.ALTAR, AltarScreenHandler::new);
        CRAFTING_TABLET = ScreenHandlerRegistry.registerSimple(PigmentContainers.CRAFTING_TABLET, CraftingTabletScreenHandler::new);
        RESTOCKING_CHEST = ScreenHandlerRegistry.registerSimple(PigmentContainers.RESTOCKING_CHEST, RestockingChestScreenHandler::new);
    }

    public static void registerClient() {
        ScreenRegistry.register(PigmentScreenHandlerTypes.ALTAR, AltarScreen::new);
        ScreenRegistry.register(PigmentScreenHandlerTypes.CRAFTING_TABLET, CraftingTabletScreen::new);
        ScreenRegistry.register(PigmentScreenHandlerTypes.RESTOCKING_CHEST, RestockingChestScreen::new);
    }

}
