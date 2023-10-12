package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.models.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.util.math.*;
import net.minecraft.entity.*;

@Environment(EnvType.CLIENT)
public class LizardEyesFeatureRenderer<T extends LivingEntity> extends EyesFeatureRenderer<T, LizardEntityModel<T>> {
	
	private static final RenderLayer TEXTURE = RenderLayer.getEyes(SpectrumCommon.locate("textures/entity/lizard/lizard_eyes.png"));
	
	public LizardEyesFeatureRenderer(FeatureRendererContext<T, LizardEntityModel<T>> featureRendererContext) {
		super(featureRendererContext);
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if ((entity.getId() - entity.world.getTime() % 120) != 0) {
			super.render(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
		}
	}
	
	@Override
	public RenderLayer getEyesTexture() {
		return TEXTURE;
	}
	
}
