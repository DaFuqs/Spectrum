package de.dafuqs.spectrum.render.armor;

import de.dafuqs.spectrum.items.armor.BedrockArmorItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class BedrockArmorRenderer extends GeoArmorRenderer<BedrockArmorItem> {
    public BedrockArmorRenderer() {
        super(new BedrockArmorModel());
    }
}
