package de.dafuqs.spectrum.blocks.mob_head.client.models;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;


public class TropicalFishHeadModel extends SpectrumSkullModel {
	
	private static final ResourceLocation PATTERN_TEXTURE = ResourceLocation.parse("textures/entity/fish/tropical_a_pattern_1.png");
	protected final ModelPart pattern;
	
	public TropicalFishHeadModel(ModelPart root, ModelPart pattern) {
		super(root);
		this.pattern = pattern.getChild(PartNames.HEAD);
	}
	
	public static MeshDefinition getModelData(CubeDeformation dilation) {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition head = modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -3.0F, -3.0F, 2.0F, 3.0F, 6.0F, dilation), PartPose.ZERO);
		head.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(22, -6).addBox(0.0F, -3.0F, 3.0F, 0.0F, 3.0F, 6.0F, dilation), PartPose.ZERO);
		head.addOrReplaceChild("right_fin", CubeListBuilder.create().texOffs(2, 16).addBox(-2.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, dilation), PartPose.offsetAndRotation(-1.0F, -1.0F, 0.0F, 0.0F, ((float) Math.PI / 4F), 0.0F));
		head.addOrReplaceChild("left_fin", CubeListBuilder.create().texOffs(2, 12).addBox(0.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, dilation), PartPose.offsetAndRotation(1.0F, -1.0F, 0.0F, 0.0F, (-(float) Math.PI / 4F), 0.0F));
		head.addOrReplaceChild("top_fin", CubeListBuilder.create().texOffs(10, -5).addBox(0.0F, -6.0F, -3.0F, 0.0F, 3.0F, 6.0F, dilation), PartPose.ZERO);
		
		return modelData;
	}
	
	public static LayerDefinition getTexturedModelData() {
		return LayerDefinition.create(getModelData(CubeDeformation.NONE), 32, 32);
	}
	
	public static LayerDefinition getTexturedModelDataPattern() {
		return LayerDefinition.create(getModelData(new CubeDeformation(0.008F)), 32, 32);
	}
	
	@Override
	public void setupAnim(float animationProgress, float yaw, float pitch) {
		super.setupAnim(animationProgress, yaw, pitch);
		this.pattern.yRot = yaw * ROTATION_VEC;
		this.pattern.xRot = pitch * ROTATION_VEC;
	}
	
	@Override
	public void render(PoseStack matrices, VertexConsumer vertices, MultiBufferSource vertexConsumerProvider, int light, int overlay, int argb) {
		float scale = getScale();
		matrices.scale(scale, scale, scale);
		
		int alpha = FastColor.ARGB32.alpha(argb);
		int red = FastColor.ARGB32.color(alpha, DyeColor.RED.getTextureDiffuseColor());
		this.renderToBuffer(matrices, vertices, light, overlay, red);
		
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderType.entityCutoutNoCullZOffset(PATTERN_TEXTURE));
		int blue = FastColor.ARGB32.color(alpha, DyeColor.BLUE.getTextureDiffuseColor());
		this.pattern.render(matrices, vertexConsumer, light, overlay, blue);
	}
	
}
