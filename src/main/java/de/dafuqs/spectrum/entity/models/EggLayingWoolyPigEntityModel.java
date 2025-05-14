package de.dafuqs.spectrum.entity.models;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.*;

@Environment(EnvType.CLIENT)
public class EggLayingWoolyPigEntityModel extends EntityModel<EggLayingWoolyPigEntity> {
	
	protected final ModelPart torso;
	protected final ModelPart head;
	protected final ModelPart left_foreleg;
	protected final ModelPart right_foreleg;
	protected final ModelPart left_backleg;
	protected final ModelPart right_backleg;
	
	public EggLayingWoolyPigEntityModel(ModelPart root) {
		super();
		this.torso = root.getChild("torso");
		this.head = torso.getChild(PartNames.HEAD);
		this.left_foreleg = torso.getChild("left_foreleg");
		this.right_foreleg = torso.getChild("right_foreleg");
		this.left_backleg = torso.getChild("left_backleg");
		this.right_backleg = torso.getChild("right_backleg");
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition torso = modelPartData.addOrReplaceChild("torso", CubeListBuilder.create()
				.texOffs(42, 47).addBox(-5.0F, -14.0F, -7.0F, 10.0F, 8.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		
		PartDefinition head = torso.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(56, 24).addBox(-4.0F, -6.0F, -6.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 44).addBox(-2.5F, -2.0F, -7.0F, 5.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 73).addBox(2.5F, -8.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(4, 73).addBox(-3.5F, -8.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.0F, -7.0F));
		
		head.addOrReplaceChild("ear_r1", CubeListBuilder.create()
				.texOffs(0, 0).addBox(7.0964F, -13.0939F, -10.5F, 3.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 6.0F, 0.0F, 0.0F, -0.3927F));
		
		head.addOrReplaceChild("ear_r2", CubeListBuilder.create()
				.texOffs(0, 32).addBox(-10.0964F, -13.0939F, -10.5F, 3.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 6.0F, 0.0F, 0.0F, 0.3927F));
		
		torso.addOrReplaceChild("left_foreleg", CubeListBuilder.create()
				.texOffs(54, 69).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -6.0F, -4.0F));
		
		torso.addOrReplaceChild("right_foreleg", CubeListBuilder.create()
				.texOffs(38, 69).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -6.0F, -4.0F));
		
		torso.addOrReplaceChild("left_backleg", CubeListBuilder.create()
				.texOffs(0, 61).addBox(-3.0F, -2.0F, -3.0F, 5.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(61, 40).addBox(-3.0F, 4.0F, 0.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -8.0F, 5.0F));
		
		torso.addOrReplaceChild("right_backleg", CubeListBuilder.create()
				.texOffs(39, 34).addBox(-2.0F, -2.0F, -3.0F, 5.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 12).addBox(0.0F, 4.0F, 0.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -8.0F, 5.0F));
		
		torso.addOrReplaceChild("tail", CubeListBuilder.create()
				.texOffs(22, 61).addBox(-2.5F, -15.0F, 7.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -1.0F));
		
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
	public void setupAnim(EggLayingWoolyPigEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.head.xRot = this.headPitchModifier;
		this.head.yRot = headYaw * 0.017453292F;
		this.right_backleg.xRot = Mth.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
		this.left_backleg.xRot = Mth.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
		this.right_foreleg.xRot = Mth.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
		this.left_foreleg.xRot = Mth.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
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