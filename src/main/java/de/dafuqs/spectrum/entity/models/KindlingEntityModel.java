package de.dafuqs.spectrum.entity.models;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.entity.animation.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

// Made with Blockbench 4.6.4
public class KindlingEntityModel extends HierarchicalModel<KindlingEntity> {
	
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart rightwing;
	private final ModelPart rightwingjoint;
	private final ModelPart leftwing;
	private final ModelPart leftwingjoint;
	private final ModelPart rightforeleg;
	private final ModelPart leftforeleg;
	private final ModelPart rightbackleg;
	private final ModelPart leftbackleg;
	private final ModelPart tail;
	private final ModelPart midtail;
	private final ModelPart fartail;
	
	public KindlingEntityModel(ModelPart root) {
		body = root.getChild(PartNames.BODY);
		head = body.getChild(PartNames.HEAD);
		rightwing = body.getChild("rightwing");
		leftwing = body.getChild("leftwing");
		rightwingjoint = rightwing.getChild("wingjoint");
		leftwingjoint = leftwing.getChild("wingjoint2");
		
		rightforeleg = body.getChild("rightforeleg");
		leftforeleg = body.getChild("leftforeleg");
		rightbackleg = body.getChild("rightbackleg");
		leftbackleg = body.getChild("leftbackleg");
		tail = body.getChild("tail");
		midtail = tail.getChild("midtail");
		fartail = midtail.getChild("fartail");
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition torso = modelPartData.addOrReplaceChild(PartNames.BODY, CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -3.5F, -7.25F, 7.0F, 7.0F, 13.0F, new CubeDeformation(0.0F))
				.texOffs(0, 21).addBox(-4.5F, -5.0F, -8.75F, 9.0F, 9.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.5F, -0.25F));
		
		PartDefinition head = torso.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(0, 48).addBox(-3.5F, -7.5F, -6.75F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(30, 76).addBox(-4.0F, -8.0F, -7.25F, 8.0F, 9.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(29, 21).addBox(-1.5F, -2.51F, -8.75F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -6.5F));
		
		head.addOrReplaceChild("middlehorn", CubeListBuilder.create().texOffs(54, 0).addBox(0.0F, -10.5F, -4.0F, 0.0F, 12.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -6.75F, 0.3927F, 0.0F, 0.0F));
		
		PartDefinition rightHorns = head.addOrReplaceChild("righthorns", CubeListBuilder.create(), PartPose.offset(0.5F, 6.5F, 6.75F));
		rightHorns.addOrReplaceChild("lowerrighthorn_r1", CubeListBuilder.create().texOffs(46, 0).addBox(-3.0F, 2.5F, -0.5F, 7.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, -13.5F, -9.0F, 0.0F, -0.4363F, 0.0F));
		rightHorns.addOrReplaceChild("upperrighthorn_r1", CubeListBuilder.create().texOffs(64, 48).addBox(-3.5F, -4.5F, -0.25F, 9.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, -12.5F, -9.0F, -0.0203F, -0.4359F, -0.4318F));
		
		PartDefinition leftHorns = head.addOrReplaceChild("lefthorns", CubeListBuilder.create(), PartPose.offset(-0.5F, 6.5F, 6.75F));
		leftHorns.addOrReplaceChild("lowerrighthorn_r2", CubeListBuilder.create().texOffs(0, 41).addBox(-4.0F, 2.5F, -0.5F, 7.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5F, -13.5F, -9.0F, 0.0F, 0.4363F, 0.0F));
		leftHorns.addOrReplaceChild("upperrighthorn_r2", CubeListBuilder.create().texOffs(63, 0).addBox(-5.5F, -4.5F, -0.25F, 9.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5F, -12.5F, -9.0F, -0.0203F, 0.4359F, 0.4318F));
		
		PartDefinition rightWing = torso.addOrReplaceChild("rightwing", CubeListBuilder.create().texOffs(93, 22).addBox(0.0F, -6.0F, -1.5F, 0.0F, 6.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -3.5F, -3.75F, 0.0F, 0.0F, 0.3054F));
		
		rightWing.addOrReplaceChild("wingjoint", CubeListBuilder.create().texOffs(84, -9).addBox(0.0F, -20.0F, -2.5F, 0.0F, 20.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, -1.0F));
		
		PartDefinition leftWing = torso.addOrReplaceChild("leftwing", CubeListBuilder.create().texOffs(93, 22).mirror().addBox(0.0F, -6.0F, -1.5F, 0.0F, 6.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, -3.5F, -3.75F, 0.0F, 0.0F, -0.3054F));
		
		leftWing.addOrReplaceChild("wingjoint2", CubeListBuilder.create().texOffs(84, -9).mirror().addBox(0.0F, -20.0F, -2.5F, 0.0F, 20.0F, 20.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -6.0F, -1.0F));
		
		torso.addOrReplaceChild("rightforeleg", CubeListBuilder.create().texOffs(67, 16).addBox(0.0F, -2.5F, -3.5F, 3.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(69, 28).addBox(3.0F, -2.5F, 1.5F, 0.0F, 7.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(25, 71).addBox(0.0F, 3.5F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 1.0F, -5.0F));
		
		torso.addOrReplaceChild("leftforeleg", CubeListBuilder.create().texOffs(44, 64).addBox(-3.0F, -2.5F, -3.5F, 3.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(71, 8).addBox(-3.0F, 3.5F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-3.0F, -2.5F, 1.5F, 0.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 1.0F, -5.0F));
		
		torso.addOrReplaceChild("rightbackleg", CubeListBuilder.create().texOffs(23, 58).addBox(-2.0F, -2.0F, -3.25F, 4.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(14, 70).addBox(2.0F, -2.0F, 2.75F, 0.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(72, 69).addBox(-2.0F, 4.0F, -0.25F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 1.5F, 4.5F));
		
		torso.addOrReplaceChild("leftbackleg", CubeListBuilder.create().texOffs(54, 35).addBox(-4.0F, -2.0F, -3.25F, 4.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(71, 56).addBox(-3.0F, 4.0F, -0.25F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(61, 69).addBox(-4.0F, -2.0F, 2.75F, 0.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 1.5F, 4.5F));
		
		PartDefinition tail = torso.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(28, 0).addBox(-2.5F, -2.48F, 0.0F, 5.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 5.75F));
		PartDefinition midTail = tail.addOrReplaceChild("midtail", CubeListBuilder.create().texOffs(57, 57).addBox(-1.5F, -1.97F, 0.0F, 3.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 7.0F));
		midTail.addOrReplaceChild("fartail", CubeListBuilder.create().texOffs(39, 43).addBox(0.0F, -6.5F, 3.0F, 0.0F, 8.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(0, 64).addBox(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 7.0F));
		
		return LayerDefinition.create(modelData, 128, 128);
	}
	
	@Override
	public ModelPart root() {
		return this.body;
	}
	
	@Override
	public void setupAnim(KindlingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		
		this.setHeadAngle(netHeadYaw, headPitch);
		
		this.animate(entity.standingAnimationState, KindlingAnimations.STANDING, ageInTicks);
		this.animate(entity.walkingAnimationState, KindlingAnimations.WALKING, ageInTicks);
		this.animate(entity.standingAngryAnimationState, KindlingAnimations.STANDING_ANGRY, ageInTicks);
		this.animate(entity.walkingAngryAnimationState, KindlingAnimations.WALKING_ANGRY, ageInTicks);
		this.animate(entity.glidingAnimationState, KindlingAnimations.GLIDING, ageInTicks);
	}
	
	private void setHeadAngle(float yaw, float pitch) {
		this.head.xRot = pitch * 0.017453292F;
		this.head.yRot = yaw * 0.017453292F;
	}
	
	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		if (young) {
			matrices.scale(0.7f, 0.7f, 0.7f);
			matrices.translate(0, 0.55, 0);
		}
		body.render(matrices, vertices, light, overlay, color);
	}
	
}