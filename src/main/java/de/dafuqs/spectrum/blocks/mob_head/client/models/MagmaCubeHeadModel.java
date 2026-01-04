package de.dafuqs.spectrum.blocks.mob_head.client.models;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.*;


public class MagmaCubeHeadModel extends SpectrumSkullModel {
	
	public MagmaCubeHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create()
				.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(24, 10).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.01F))
				.texOffs(24, 19).addBox(-4.0F, -5.0F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.01F)), PartPose.ZERO);
		
		return LayerDefinition.create(modelData, 64, 32);
	}
	
	@Override
	public void render(PoseStack matrices, VertexConsumer vertices, MultiBufferSource vertexConsumerProvider, int light, int overlay, int argb) {
		super.render(matrices, vertices, vertexConsumerProvider, light, overlay, argb);
	}
	
}
