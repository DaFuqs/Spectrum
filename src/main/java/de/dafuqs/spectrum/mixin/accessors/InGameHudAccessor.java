package de.dafuqs.spectrum.mixin.accessors;

import net.fabricmc.api.*;
import net.minecraft.client.gui.hud.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public interface InGameHudAccessor {
    @Accessor(value = "scaledWidth")
    int getWidth();

    @Accessor(value = "scaledHeight")
    int getHeight();
}
