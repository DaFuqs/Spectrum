package de.dafuqs.spectrum.entity.models;

import com.google.common.collect.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;

@Environment(EnvType.CLIENT)
public class LizardEntityModel<T extends LivingEntity> extends AnimalModel<T> {
	
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
		this.body = root.getChild(EntityModelPartNames.BODY);
		this.head = root.getChild(EntityModelPartNames.HEAD);
		this.rightHindLeg = root.getChild(EntityModelPartNames.RIGHT_HIND_LEG);
		this.leftHindLeg = root.getChild(EntityModelPartNames.LEFT_HIND_LEG);
		this.rightFrontLeg = root.getChild(EntityModelPartNames.RIGHT_FRONT_LEG);
		this.leftFrontLeg = root.getChild(EntityModelPartNames.LEFT_FRONT_LEG);
		this.tail = root.getChild(EntityModelPartNames.TAIL);
		this.lower_tail = tail.getChild("lower_tail");
		this.jaw = head.getChild("jaw");
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData body = modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create()
				.uv(23, 18).cuboid(-3.5F, -3.0F, -9.0F, 7.0F, 7.0F, 11.0F)
				.uv(0, 0).cuboid(0.0F, -15.0F, -9.0F, 0.0F, 12.0F, 17.0F)
				.uv(48, 13).cuboid(-3.0F, -3.0F, 2.0F, 6.0F, 7.0F, 6.0F), ModelTransform.pivot(0.0F, 19.0F, 0.0F));
		
		ModelPartData rightbackleg = modelPartData.addChild(EntityModelPartNames.RIGHT_HIND_LEG, ModelPartBuilder.create()
				.uv(0, 65).cuboid(0.0F, -1.5F, -2.0F, 3.0F, 4.0F, 4.0F)
				.uv(9, 2).cuboid(1.0F, 2.5F, -3.0F, 2.0F, 0.0F, 1.0F), ModelTransform.pivot(3.0F, 19.0F + 2.5F, 6.0F));
		
		rightbackleg.addChild("rightbackfrills_r1", ModelPartBuilder.create()
				.uv(0, 21).cuboid(0.9F, -3.5F, -3.9F, 0.0F, 7.0F, 8.0F), ModelTransform.of(2.0F, -1.0F, 2.0F, 0.0F, 0.3927F, 0.0F));
		
		ModelPartData leftbackleg = modelPartData.addChild(EntityModelPartNames.LEFT_HIND_LEG, ModelPartBuilder.create()
				.uv(0, 0).cuboid(-3.0F, -1.5F, -2.0F, 3.0F, 4.0F, 4.0F)
				.uv(9, 0).cuboid(-3.0F, 2.5F, -3.0F, 2.0F, 0.0F, 1.0F), ModelTransform.pivot(-3.0F, 19.0F + 2.5F, 6.0F));
		
		leftbackleg.addChild("leftbackfrills_r1", ModelPartBuilder.create()
				.uv(0, 0).cuboid(-0.9F, -3.5F, -3.9F, 0.0F, 7.0F, 8.0F), ModelTransform.of(-2.0F, -1.0F, 2.0F, 0.0F, -0.3927F, 0.0F));
		
		ModelPartData rightforeleg = modelPartData.addChild(EntityModelPartNames.RIGHT_FRONT_LEG, ModelPartBuilder.create()
				.uv(66, 9).cuboid(0.0F, -1.5F, -2.0F, 2.0F, 4.0F, 4.0F)
				.uv(9, 3).cuboid(0.0F, 2.5F, -3.0F, 2.0F, 0.0F, 1.0F), ModelTransform.pivot(3.5F, 19.0F + 2.5F, -5.0F));
		
		rightforeleg.addChild("rightfrills_r1", ModelPartBuilder.create()
				.uv(31, 60).cuboid(-0.75F, -4.0F, -1.0F, 0.0F, 6.0F, 7.0F), ModelTransform.of(2.0F, 0.5F, 0.0F, 0.0F, 0.3927F, 0.0F));
		
		ModelPartData leftforeleg = modelPartData.addChild(EntityModelPartNames.LEFT_FRONT_LEG, ModelPartBuilder.create()
				.uv(31, 58).cuboid(-2.0F, -1.5F, -2.0F, 2.0F, 4.0F, 4.0F)
				.uv(9, 1).cuboid(-2.0F, 2.5F, -3.0F, 2.0F, 0.0F, 1.0F), ModelTransform.pivot(-3.5F, 19.0F + 2.5F, -5.0F));
		
		leftforeleg.addChild("leftfrills_r1", ModelPartBuilder.create()
				.uv(36, 37).cuboid(0.75F, -4.0F, -1.0F, 0.0F, 6.0F, 7.0F), ModelTransform.of(-2.0F, 0.5F, 0.0F, 0.0F, -0.3927F, 0.0F));
		
		ModelPartData neartail = modelPartData.addChild(EntityModelPartNames.TAIL, ModelPartBuilder.create()
				.uv(18, 44).cuboid(-2.5F, -4.0F, 0.0F, 5.0F, 6.0F, 8.0F)
				.uv(44, 48).cuboid(0.0F, -15.0F, 0.0F, 0.0F, 11.0F, 8.0F), ModelTransform.pivot(0.0F, 19.0F + 1.5F, 8.0F));
		
		ModelPartData midtail = neartail.addChild("lower_tail", ModelPartBuilder.create()
				.uv(45, 0).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 5.0F, 8.0F)
				.uv(0, 42).cuboid(0.0F, -14.0F, 0.0F, 0.0F, 11.0F, 8.0F), ModelTransform.pivot(0.0F, 0.0F, 8.0F));
		
		midtail.addChild("fartail", ModelPartBuilder.create()
				.uv(56, 28).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 4.0F, 8.0F)
				.uv(0, 0).cuboid(-7.0F, 1.0F, 0.0F, 14.0F, 0.0F, 17.0F)
				.uv(0, 23).cuboid(0.0F, -12.0F, 0.0F, 0.0F, 14.0F, 13.0F), ModelTransform.pivot(0.0F, -1.0F, 8.0F));
		
		ModelPartData head = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
				.uv(11, 58).cuboid(-2.5F, -4.0F, -5.0F, 5.0F, 6.0F, 5.0F)
				.uv(44, 44).cuboid(-2.0F, -4.0F, -14.0F, 4.0F, 3.0F, 9.0F)
				.uv(26, 21).cuboid(0.0F, -11.0F, -15.0F, 0.0F, 8.0F, 15.0F), ModelTransform.pivot(0.0F, 19.0F + 2.0F, -9.0F));
		
		head.addChild("rightfrills_r2", ModelPartBuilder.create()
				.uv(61, 40).cuboid(-1.9733F, -9.9307F, 0.0F, 8.0F, 10.0F, 0.0F), ModelTransform.of(2.5F, -4.0F, -5.0F, -0.8281F, 0.001F, 1.5679F));
		head.addChild("leftfrills_r2", ModelPartBuilder.create()
				.uv(45, 68).cuboid(-6.0267F, -9.9307F, 0.0F, 8.0F, 10.0F, 0.0F), ModelTransform.of(-2.5F, -4.0F, -5.0F, -0.8282F, 0.0F, -1.5615F));
		head.addChild("topfrills_r1", ModelPartBuilder.create()
				.uv(60, 56).cuboid(-4.5F, -11.75F, -0.15F, 9.0F, 12.0F, 0.0F), ModelTransform.of(0.0F, -4.0F, -5.0F, -0.8727F, 0.0F, 0.0F));
		
		head.addChild("jaw", ModelPartBuilder.create()
				.uv(61, 0).cuboid(-1.5F, 0.0F, -5.5F, 3.0F, 1.0F, 6.0F), ModelTransform.pivot(0.0F, -1.0F, -5.0F));
		
		return TexturedModelData.of(modelData, 128, 128);
	}
	
	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of(this.head);
	}
	
	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(this.body, this.rightHindLeg, this.leftHindLeg, this.rightFrontLeg, this.leftFrontLeg, this.tail);
	}
	
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.head.pitch = headPitch * 0.017453292F;
		this.head.yaw = headYaw * 0.017453292F;
		this.rightHindLeg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
		this.leftHindLeg.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
		this.rightFrontLeg.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
		this.leftFrontLeg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
		
		this.tail.yaw = this.lerpAngleDegrees(this.tail.yaw, 0.3F * MathHelper.cos(animationProgress * 0.1F));
		this.lower_tail.yaw = this.lerpAngleDegrees(this.tail.yaw, 0.3F * MathHelper.cos(animationProgress * 0.1F));
		
		this.jaw.pitch = 0.3F * this.lerpAngleDegrees(this.tail.yaw, 0.05F * MathHelper.cos(animationProgress * 0.1F));
	}
	
	private float lerpAngleDegrees(float start, float end) {
		return MathHelper.lerpAngleDegrees(0.05F, start, end);
	}
	
}