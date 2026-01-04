package de.dafuqs.spectrum.blocks.mob_head.client.models;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.*;


public class SlimeHeadModel extends SpectrumSkullModel {
	
	protected final ModelPart translucent;
	
	public SlimeHeadModel(ModelPart root, ModelPart translucent) {
		super(root);
		this.translucent = translucent;
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition head = modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, -7.0F, -3.0F, 6.0F, 6.0F, 6.0F), PartPose.ZERO);
		head.addOrReplaceChild("right_eye", CubeListBuilder.create().texOffs(32, 0).addBox(-3.25F, -6.0F, -3.5F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
		head.addOrReplaceChild("left_eye", CubeListBuilder.create().texOffs(32, 4).addBox(1.25F, -6.0F, -3.5F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
		head.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(32, 8).addBox(0.0F, -3.0F, -3.5F, 1.0F, 1.0F, 1.0F), PartPose.ZERO);
		
		return LayerDefinition.create(modelData, 64, 32);
	}
	
	public static LayerDefinition getTexturedModelTranslucent() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
		return LayerDefinition.create(modelData, 64, 32);
	}
	
	@Override
	public void setupAnim(float animationProgress, float yaw, float pitch) {
		super.setupAnim(animationProgress, yaw, pitch);
		this.translucent.yRot = yaw * ROTATION_VEC;
		this.translucent.xRot = pitch * ROTATION_VEC;
	}
	
	@Override
	public void render(PoseStack matrices, VertexConsumer vertices, MultiBufferSource vertexConsumerProvider, int light, int overlay, int argb) {
		super.render(matrices, vertices, vertexConsumerProvider, light, overlay, argb);
		
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderType.entityTranslucent(ResourceLocation.parse("textures/entity/slime/slime.png")));
		this.translucent.render(matrices, vertexConsumer, light, overlay, argb);
	}
	
}
