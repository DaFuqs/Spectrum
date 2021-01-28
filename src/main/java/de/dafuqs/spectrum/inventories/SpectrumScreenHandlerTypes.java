package de.dafuqs.spectrum.inventories;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class SpectrumScreenHandlerTypes {

    public static ScreenHandlerType<AltarScreenHandler> ALTAR_SCREEN_HANDLER;

    public static void register() {
        ALTAR_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(SpectrumContainers.ALTAR, ((syncId, inventory) -> new AltarScreenHandler(syncId, inventory)));
    }

}
