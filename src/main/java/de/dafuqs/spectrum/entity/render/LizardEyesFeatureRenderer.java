package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.entity.*;

@Environment(EnvType.CLIENT)
public class LizardEyesFeatureRenderer<T extends LivingEntity> extends EyesFeatureRenderer {
	
	private static final RenderLayer RENDER_LAYER = RenderLayer.getEyes(SpectrumCommon.locate("textures/entity/lizard/eyes.png"));
	
	public LizardEyesFeatureRenderer(FeatureRendererContext featureRendererContext) {
		super(featureRendererContext);
	}
	
	@Override
	public RenderLayer getEyesTexture() {
		return RENDER_LAYER;
	}
	
}
