package de.dafuqs.spectrum.entity.models;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@Environment(EnvType.CLIENT)
public class EggLayingWoolyPigHatEntityModel extends EntityModel<EggLayingWoolyPigEntity> {
	
	private final ModelPart torso;
	private final ModelPart head;
	
	public EggLayingWoolyPigHatEntityModel(ModelPart root) {
		super();
		this.torso = root.getChild("torso");
		this.head = torso.getChild("head");
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition torso = modelPartData.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		
		torso.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(45, 0).addBox(-5.02F, -7.0F, -7.0F, 10.0F, 7.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.0F, -7.0F));
		
		return LayerDefinition.create(modelData, 128, 128);
	}
	
	private float headPitchModifier;
	
	@Override
	public void prepareMobModel(EggLayingWoolyPigEntity entity, float limbAngle, float limbDistance, float tickDelta) {
		super.prepareMobModel(entity, limbAngle, limbDistance, tickDelta);
		this.head.y = -13.0F + entity.getNeckAngle(tickDelta) * 9.0F;
		this.headPitchModifier = entity.getHeadAngle(tickDelta);
	}
	
	@Override
	public void setupAnim(EggLayingWoolyPigEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.xRot = this.headPitchModifier;
		this.head.yRot = netHeadYaw * 0.017453292F;
	}
	
	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		if (young) {
			matrices.scale(0.6f, 0.6f, 0.6f);
			matrices.translate(0, 1, 0);
		}
		torso.render(matrices, vertices, light, overlay, color);
	}
	
}