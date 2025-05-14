package de.dafuqs.spectrum.entity.models;

import com.google.common.collect.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;

@Environment(EnvType.CLIENT)
public class LizardEntityModel<T extends LivingEntity> extends AgeableListModel<T> {
	
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart leftHindLeg;
	private final ModelPart rightHindLeg;
	private final ModelPart leftFrontLeg;
	private final ModelPart rightFrontLeg;
	private final ModelPart tail;
	private final ModelPart lower_tail;
	private final ModelPart jaw;
	
	public LizardEntityModel(ModelPart root) {
		super(true, 8.0F, 3.35F);
		this.body = root.getChild(PartNames.BODY);
		this.head = root.getChild(PartNames.HEAD);
		this.rightHindLeg = root.getChild(PartNames.RIGHT_HIND_LEG);
		this.leftHindLeg = root.getChild(PartNames.LEFT_HIND_LEG);
		this.rightFrontLeg = root.getChild(PartNames.RIGHT_FRONT_LEG);
		this.leftFrontLeg = root.getChild(PartNames.LEFT_FRONT_LEG);
		this.tail = root.getChild(PartNames.TAIL);
		this.lower_tail = tail.getChild("lower_tail");
		this.jaw = head.getChild("jaw");
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		modelPartData.addOrReplaceChild(PartNames.BODY, CubeListBuilder.create()
				.texOffs(23, 18).addBox(-3.5F, -3.0F, -9.0F, 7.0F, 7.0F, 11.0F)
				.texOffs(0, 0).addBox(0.0F, -15.0F, -9.0F, 0.0F, 12.0F, 17.0F)
				.texOffs(48, 13).addBox(-3.0F, -3.0F, 2.0F, 6.0F, 7.0F, 6.0F), PartPose.offset(0.0F, 19.0F, 0.0F));
		
		PartDefinition rightbackleg = modelPartData.addOrReplaceChild(PartNames.RIGHT_HIND_LEG, CubeListBuilder.create()
				.texOffs(0, 65).addBox(0.0F, -1.5F, -2.0F, 3.0F, 4.0F, 4.0F)
				.texOffs(9, 2).addBox(1.0F, 2.5F, -3.0F, 2.0F, 0.0F, 1.0F), PartPose.offset(3.0F, 19.0F + 2.5F, 6.0F));
		
		rightbackleg.addOrReplaceChild("rightbackfrills_r1", CubeListBuilder.create()
				.texOffs(0, 21).addBox(0.9F, -3.5F, -3.9F, 0.0F, 7.0F, 8.0F), PartPose.offsetAndRotation(2.0F, -1.0F, 2.0F, 0.0F, 0.3927F, 0.0F));
		
		PartDefinition leftbackleg = modelPartData.addOrReplaceChild(PartNames.LEFT_HIND_LEG, CubeListBuilder.create()
				.texOffs(0, 0).addBox(-3.0F, -1.5F, -2.0F, 3.0F, 4.0F, 4.0F)
				.texOffs(9, 0).addBox(-3.0F, 2.5F, -3.0F, 2.0F, 0.0F, 1.0F), PartPose.offset(-3.0F, 19.0F + 2.5F, 6.0F));
		
		leftbackleg.addOrReplaceChild("leftbackfrills_r1", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-0.9F, -3.5F, -3.9F, 0.0F, 7.0F, 8.0F), PartPose.offsetAndRotation(-2.0F, -1.0F, 2.0F, 0.0F, -0.3927F, 0.0F));
		
		PartDefinition rightforeleg = modelPartData.addOrReplaceChild(PartNames.RIGHT_FRONT_LEG, CubeListBuilder.create()
				.texOffs(66, 9).addBox(0.0F, -1.5F, -2.0F, 2.0F, 4.0F, 4.0F)
				.texOffs(9, 3).addBox(0.0F, 2.5F, -3.0F, 2.0F, 0.0F, 1.0F), PartPose.offset(3.5F, 19.0F + 2.5F, -5.0F));
		
		rightforeleg.addOrReplaceChild("rightfrills_r1", CubeListBuilder.create()
				.texOffs(31, 60).addBox(-0.75F, -4.0F, -1.0F, 0.0F, 6.0F, 7.0F), PartPose.offsetAndRotation(2.0F, 0.5F, 0.0F, 0.0F, 0.3927F, 0.0F));
		
		PartDefinition leftforeleg = modelPartData.addOrReplaceChild(PartNames.LEFT_FRONT_LEG, CubeListBuilder.create()
				.texOffs(31, 58).addBox(-2.0F, -1.5F, -2.0F, 2.0F, 4.0F, 4.0F)
				.texOffs(9, 1).addBox(-2.0F, 2.5F, -3.0F, 2.0F, 0.0F, 1.0F), PartPose.offset(-3.5F, 19.0F + 2.5F, -5.0F));
		
		leftforeleg.addOrReplaceChild("leftfrills_r1", CubeListBuilder.create()
				.texOffs(36, 37).addBox(0.75F, -4.0F, -1.0F, 0.0F, 6.0F, 7.0F), PartPose.offsetAndRotation(-2.0F, 0.5F, 0.0F, 0.0F, -0.3927F, 0.0F));
		
		PartDefinition neartail = modelPartData.addOrReplaceChild(PartNames.TAIL, CubeListBuilder.create()
				.texOffs(18, 44).addBox(-2.5F, -4.0F, 0.0F, 5.0F, 6.0F, 8.0F)
				.texOffs(44, 48).addBox(0.0F, -15.0F, 0.0F, 0.0F, 11.0F, 8.0F), PartPose.offset(0.0F, 19.0F + 1.5F, 8.0F));
		
		PartDefinition midtail = neartail.addOrReplaceChild("lower_tail", CubeListBuilder.create()
				.texOffs(45, 0).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 5.0F, 8.0F)
				.texOffs(0, 42).addBox(0.0F, -14.0F, 0.0F, 0.0F, 11.0F, 8.0F), PartPose.offset(0.0F, 0.0F, 8.0F));
		
		midtail.addOrReplaceChild("fartail", CubeListBuilder.create()
				.texOffs(56, 28).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 4.0F, 8.0F)
				.texOffs(0, 0).addBox(-7.0F, 1.0F, 0.0F, 14.0F, 0.0F, 17.0F)
				.texOffs(0, 23).addBox(0.0F, -12.0F, 0.0F, 0.0F, 14.0F, 13.0F), PartPose.offset(0.0F, -1.0F, 8.0F));
		
		PartDefinition head = modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create()
				.texOffs(11, 58).addBox(-2.5F, -4.0F, -5.0F, 5.0F, 6.0F, 5.0F)
				.texOffs(44, 44).addBox(-2.0F, -4.0F, -14.0F, 4.0F, 3.0F, 9.0F)
				.texOffs(26, 21).addBox(0.0F, -11.0F, -15.0F, 0.0F, 8.0F, 15.0F), PartPose.offset(0.0F, 19.0F + 2.0F, -9.0F));
		
		head.addOrReplaceChild("rightfrills_r2", CubeListBuilder.create()
				.texOffs(61, 40).addBox(-1.9733F, -9.9307F, 0.0F, 8.0F, 10.0F, 0.0F), PartPose.offsetAndRotation(2.5F, -4.0F, -5.0F, -0.8281F, 0.001F, 1.5679F));
		head.addOrReplaceChild("leftfrills_r2", CubeListBuilder.create()
				.texOffs(45, 68).addBox(-6.0267F, -9.9307F, 0.0F, 8.0F, 10.0F, 0.0F), PartPose.offsetAndRotation(-2.5F, -4.0F, -5.0F, -0.8282F, 0.0F, -1.5615F));
		head.addOrReplaceChild("topfrills_r1", CubeListBuilder.create()
				.texOffs(60, 56).addBox(-4.5F, -11.75F, -0.15F, 9.0F, 12.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -4.0F, -5.0F, -0.8727F, 0.0F, 0.0F));
		
		head.addOrReplaceChild("jaw", CubeListBuilder.create()
				.texOffs(61, 0).addBox(-1.5F, 0.0F, -5.5F, 3.0F, 1.0F, 6.0F), PartPose.offset(0.0F, -1.0F, -5.0F));
		
		return LayerDefinition.create(modelData, 128, 128);
	}
	
	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of(this.head);
	}
	
	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.body, this.rightHindLeg, this.leftHindLeg, this.rightFrontLeg, this.leftFrontLeg, this.tail);
	}
	
	@Override
	public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.head.xRot = headPitch * 0.017453292F;
		this.head.yRot = headYaw * 0.017453292F;
		this.rightHindLeg.xRot = Mth.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
		this.leftHindLeg.xRot = Mth.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
		this.rightFrontLeg.xRot = Mth.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
		this.leftFrontLeg.xRot = Mth.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
		
		this.tail.yRot = this.lerpAngleDegrees(this.tail.yRot, 0.3F * Mth.cos(animationProgress * 0.1F));
		this.lower_tail.yRot = this.lerpAngleDegrees(this.tail.yRot, 0.3F * Mth.cos(animationProgress * 0.1F));
		
		this.jaw.xRot = 0.3F * this.lerpAngleDegrees(this.tail.yRot, 0.05F * Mth.cos(animationProgress * 0.1F));
	}
	
	private float lerpAngleDegrees(float start, float end) {
		return Mth.rotLerp(0.05F, start, end);
	}
	
}