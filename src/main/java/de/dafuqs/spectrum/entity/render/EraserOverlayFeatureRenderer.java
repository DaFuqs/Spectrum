package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.entity.entity.EraserEntity;
import de.dafuqs.spectrum.entity.models.EraserEntityModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.util.Identifier;

public class EraserOverlayFeatureRenderer extends EyesFeatureRenderer<EraserEntity, EraserEntityModel> {

    public static final RenderLayer LAYER = RenderLayer.getEyes(SpectrumCommon.locate("textures/entity/eraser/eraser_emissive.png"));

    public EraserOverlayFeatureRenderer(FeatureRendererContext<EraserEntity, EraserEntityModel> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public RenderLayer getEyesTexture() {
        return LAYER;
    }
}
