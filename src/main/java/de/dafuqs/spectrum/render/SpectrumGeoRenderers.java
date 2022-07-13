package de.dafuqs.spectrum.render;

import de.dafuqs.spectrum.registries.SpectrumItems;
import de.dafuqs.spectrum.render.armor.BedrockArmorRenderer;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class SpectrumGeoRenderers {

    public static void register() {
        GeoArmorRenderer.registerArmorRenderer(new BedrockArmorRenderer(), SpectrumItems.BEDROCK_HELMET, SpectrumItems.BEDROCK_CHESTPLATE, SpectrumItems.BEDROCK_LEGGINGS, SpectrumItems.BEDROCK_BOOTS);
    }
}
