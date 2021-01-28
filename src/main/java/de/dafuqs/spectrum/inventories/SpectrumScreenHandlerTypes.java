package de.dafuqs.spectrum.inventories;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class SpectrumScreenHandlerTypes {

    public static ScreenHandlerType<AltarCraftingScreenHandler> ALTAR;

    public static void register() {
        ALTAR = ScreenHandlerRegistry.registerSimple(SpectrumContainers.ALTAR, (AltarCraftingScreenHandler::new));
    }

}
