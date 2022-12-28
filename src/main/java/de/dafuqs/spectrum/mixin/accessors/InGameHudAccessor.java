package de.dafuqs.spectrum.mixin.accessors;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public interface InGameHudAccessor {
    @Accessor(value = "scaledWidth")
    int getWidth();

    @Accessor(value = "scaledHeight")
    int getHeight();
}
