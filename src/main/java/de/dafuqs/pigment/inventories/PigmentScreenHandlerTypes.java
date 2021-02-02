package de.dafuqs.pigment.inventories;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class PigmentScreenHandlerTypes {

    public static ScreenHandlerType<AltarCraftingScreenHandler> ALTAR;

    public static void register() {
        ALTAR = ScreenHandlerRegistry.registerSimple(PigmentContainers.ALTAR, (AltarCraftingScreenHandler::new));
    }

}
