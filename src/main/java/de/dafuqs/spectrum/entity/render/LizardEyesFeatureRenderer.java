package de.dafuqs.spectrum.entity.render;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.models.*;
import net.fabricmc.api.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.world.entity.*;

@Environment(EnvType.CLIENT)
public class LizardEyesFeatureRenderer<T extends LivingEntity> extends EyesLayer<T, LizardEntityModel<T>> {
	
	private static final RenderType TEXTURE = RenderType.eyes(SpectrumCommon.locate("textures/entity/lizard/lizard_eyes.png"));
	
	public LizardEyesFeatureRenderer(RenderLayerParent<T, LizardEntityModel<T>> featureRendererContext) {
		super(featureRendererContext);
	}
	
	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if ((entity.getId() - entity.level().getGameTime() % 120) != 0) {
			super.render(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
		}
	}

	@Override
	public RenderType renderType() {
		return TEXTURE;
	}
	
}
