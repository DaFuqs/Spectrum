package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.entity.models.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.*;

public class EraserOverlayFeatureRenderer extends EyesLayer<EraserEntity, EraserEntityModel> {
	
	public static final RenderType LAYER = RenderType.eyes(SpectrumCommon.locate("textures/entity/eraser/eraser_emissive.png"));
	
	public EraserOverlayFeatureRenderer(RenderLayerParent<EraserEntity, EraserEntityModel> featureRendererContext) {
		super(featureRendererContext);
	}
	
	@Override
	public RenderType renderType() {
		return LAYER;
	}
}
