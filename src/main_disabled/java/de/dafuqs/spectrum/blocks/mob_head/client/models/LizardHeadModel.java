package de.dafuqs.spectrum.blocks.mob_head.client.models;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;

@Environment(EnvType.CLIENT)
public class LizardHeadModel extends SpectrumSkullModel {
	
	public static final ResourceLocation HEAD_TEXTURE = SpectrumCommon.locate("textures/entity/lizard/lizard_head.png");
	
	protected final int color;
	protected final ModelPart frills;
	
	public LizardHeadModel(ModelPart root, ModelPart frills, int color) {
		super(root);
		this.color = color;
		this.frills = frills;
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition head = modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create()
				.texOffs(11, 58).addBox(-2.5F, -6.0F, 1.0F, 5.0F, 6.0F, 5.0F)
				.texOffs(44, 44).addBox(-2.0F, -6.0F, -8.0F, 4.0F, 3.0F, 9.0F)
				.texOffs(26, 21).addBox(0.0F, -13.0F, -9.0F, 0.0F, 8.0F, 15.0F), PartPose.ZERO);
		
		head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(61, 0).addBox(-1.5F, 0.0F, -5.5F, 3.0F, 1.0F, 6.0F), PartPose.offset(0.0F, -3.0F, 1.0F));
		
		return LayerDefinition.create(modelData, 128, 128);
	}
	
	public static LayerDefinition getTexturedModelDataFrills() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition head = modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create(), PartPose.ZERO);
		
		head.addOrReplaceChild("rightfrills_r1", CubeListBuilder.create().texOffs(61, 40).addBox(-1.9733F, -9.9307F, 0.0F, 8.0F, 10.0F, 0.0F), PartPose.offsetAndRotation(2.5F, -6.0F, 1.0F, -0.8281F, 0.001F, 1.5679F));
		head.addOrReplaceChild("leftfrills_r1", CubeListBuilder.create().texOffs(45, 68).addBox(-6.0267F, -9.9307F, 0.0F, 8.0F, 10.0F, 0.0F), PartPose.offsetAndRotation(-2.5F, -6.0F, 1.0F, -0.8282F, 0.0F, -1.5615F));
		head.addOrReplaceChild("topfrills_r1", CubeListBuilder.create().texOffs(60, 56).addBox(-4.5F, -11.75F, -0.15F, 9.0F, 12.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -6.0F, 1.0F, -0.8727F, 0.0F, 0.0F));
		head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(61, 0).addBox(-1.5F, 0.0F, -5.5F, 3.0F, 1.0F, 6.0F), PartPose.offset(0.0F, -3.0F, 1.0F));
		
		return LayerDefinition.create(modelData, 128, 128);
	}
	
	@Override
	public void setupAnim(float animationProgress, float yaw, float pitch) {
		super.setupAnim(animationProgress, yaw, pitch);
		this.frills.yRot = yaw * ROTATION_VEC;
		this.frills.xRot = pitch * ROTATION_VEC;
	}
	
	@Override
	public void render(PoseStack matrices, VertexConsumer vertices, MultiBufferSource vertexConsumerProvider, int light, int overlay, int argb) {
		float scale = getScale();
		matrices.scale(scale, scale, scale);
		
		this.renderToBuffer(matrices, vertices, light, overlay, argb);
		
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderType.entityCutoutNoCullZOffset(HEAD_TEXTURE));
		int alpha = FastColor.ARGB32.alpha(argb);
		this.frills.render(matrices, vertexConsumer, light, overlay, FastColor.ARGB32.color(alpha, color));
	}
	
}