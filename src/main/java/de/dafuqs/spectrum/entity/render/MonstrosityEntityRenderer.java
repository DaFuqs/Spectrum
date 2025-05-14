package de.dafuqs.spectrum.entity.render;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.entity.models.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.*;

@Environment(EnvType.CLIENT)
public class MonstrosityEntityRenderer extends MobRenderer<MonstrosityEntity, MonstrosityEntityModel> {
	
	public static final ResourceLocation TEXTURE = SpectrumCommon.locate("textures/entity/monstrosity.png");
	
	public MonstrosityEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new MonstrosityEntityModel(context.bakeLayer(SpectrumModelLayers.MONSTROSITY)), 1.8F);
	}
	
	@Override
	public void render(MonstrosityEntity entity, float yaw, float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumerProvider, int light) {
		super.render(entity, yaw, tickDelta, poseStack, vertexConsumerProvider, light);
	}
	
	@Override
	public ResourceLocation getTextureLocation(MonstrosityEntity entity) {
		return TEXTURE;
	}
	
}
