package de.dafuqs.spectrum.entity.models;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@Environment(EnvType.CLIENT)
public class MonstrosityEntityModel extends EntityModel<MonstrosityEntity> {
	
	private final ModelPart torso;
	
	public MonstrosityEntityModel(ModelPart root) {
		this.torso = root.getChild("torso");
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		
		PartDefinition modelPartData = modelData.getRoot();
		modelPartData.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -12.0F, -10.0F, 20.0F, 12.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		
		return LayerDefinition.create(modelData, 128, 128);
	}
	
	@Override
	public void setupAnim(MonstrosityEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	
	}
	
	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		torso.render(matrices, vertices, light, overlay, color);
	}
	
}