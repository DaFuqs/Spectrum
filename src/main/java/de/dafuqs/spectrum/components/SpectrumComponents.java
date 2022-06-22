package de.dafuqs.spectrum.components;

import de.dafuqs.spectrum.SpectrumCommon;
import net.immortaldevs.sar.api.SarRegistries;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumComponents {

    public static final GraceBearerComponent GRACE_BEARER = new GraceBearerComponent();

    public static void register() {
        Registry.register(SarRegistries.COMPONENT, new Identifier(SpectrumCommon.MOD_ID, "grace_bearer"), GRACE_BEARER);
    }
}
