package de.dafuqs.spectrum.blocks.altar;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class SpectrumScreenHandlers {

    public static ScreenHandlerType<AltarScreenHandler> ALTAR;

    public static void register() {
        ALTAR = ScreenHandlerRegistry.registerSimple(SpectrumContainers.ALTAR, ((syncId, inventory) -> new AltarScreenHandler(syncId, inventory)));
    }

}
