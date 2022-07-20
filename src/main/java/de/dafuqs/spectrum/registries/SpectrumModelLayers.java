package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.render.armor.BedrockArmorModel;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class SpectrumModelLayers {
    public static final EntityModelLayer FEET_BEDROCK_LAYER = new EntityModelLayer(SpectrumCommon.locate("bedrock_armor"), "feet");
    public static final EntityModelLayer MAIN_BEDROCK_LAYER = new EntityModelLayer(SpectrumCommon.locate("bedrock_armor"), "main");

    public static void register() {
        EntityModelLayerRegistry.registerModelLayer(FEET_BEDROCK_LAYER,
                () -> TexturedModelData.of(BedrockArmorModel.getModelData(), 128, 128));
        EntityModelLayerRegistry.registerModelLayer(MAIN_BEDROCK_LAYER,
                () -> TexturedModelData.of(BedrockArmorModel.getModelData(), 128, 128));
    }
}
