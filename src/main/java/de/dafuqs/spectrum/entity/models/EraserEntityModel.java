package de.dafuqs.spectrum.entity.models;

import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.util.math.*;

public class EraserEntityModel extends SinglePartEntityModel<EraserEntity> {
	
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart rightHindLeg;
	private final ModelPart leftHindLeg;
	private final ModelPart rightMiddleLeg;
	private final ModelPart leftMiddleLeg;
	private final ModelPart rightFrontLeg;
	private final ModelPart leftFrontLeg;
	
	public EraserEntityModel(ModelPart root) {
		this.root = root;
		
		ModelPart body = root.getChild(EntityModelPartNames.BODY);
		this.head = body.getChild(EntityModelPartNames.HEAD);
		ModelPart legs = body.getChild("legs");
		this.rightHindLeg = legs.getChild(EntityModelPartNames.RIGHT_HIND_LEG);
		this.leftHindLeg = legs.getChild(EntityModelPartNames.LEFT_HIND_LEG);
		this.rightMiddleLeg = legs.getChild(EntityModelPartNames.RIGHT_FRONT_LEG);
		this.leftMiddleLeg = legs.getChild(EntityModelPartNames.LEFT_FRONT_LEG);
		this.rightFrontLeg = legs.getChild("rightstrikeleg");
		this.leftFrontLeg = legs.getChild("leftstrikeleg");
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		
		ModelPartData head = body.addChild("head", ModelPartBuilder.create()
				.uv(0, 0).cuboid(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F), ModelTransform.pivot(0.0F, -2.25F, 0.0F));
		head.addChild("nettles", ModelPartBuilder.create()
				.uv(0, 4).cuboid(-1.5F, -4.0F, 0.0F, 3.0F, 5.0F, 0.0F), ModelTransform.of(0.0F, -0.9F, -1.0F, -0.4363F, 0.0F, 0.0F));
		
		ModelPartData legs = body.addChild("legs", ModelPartBuilder.create()
				.uv(0, 20).cuboid(-1.5F, -0.25F, -0.5F, 3.0F, 1.0F, 1.0F), ModelTransform.pivot(0.0F, -1.5F, 0.0F));
		
		ModelPartData rightstrikeleg = legs.addChild("rightstrikeleg", ModelPartBuilder.create()
				.uv(19, 10).cuboid(-0.5F, 0.25F, -0.5F, 5.0F, 0.0F, 1.0F), ModelTransform.of(1.0F, 0.0F, 0.0F, -0.7935F, 0.8029F, -0.941F));
		rightstrikeleg.addChild("rightstrikeforeleg", ModelPartBuilder.create()
				.uv(2, 13).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F), ModelTransform.of(4.5F, 0.25F, 0.0F, 0.0F, 0.0F, -0.0873F));
		
		ModelPartData rightfrontleg = legs.addChild(EntityModelPartNames.RIGHT_FRONT_LEG, ModelPartBuilder.create()
				.uv(19, 9).cuboid(-0.5F, 0.25F, -0.5F, 5.0F, 0.0F, 1.0F), ModelTransform.of(1.0F, 0.0F, 0.0F, -0.1896F, 0.1978F, -0.7436F));
		rightfrontleg.addChild("rightfrontforeleg", ModelPartBuilder.create()
				.uv(0, 13).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F), ModelTransform.of(4.5F, 0.25F, 0.0F, 0.0F, 0.0F, 0.1745F));
		
		ModelPartData rightmidleg = legs.addChild("rightmidleg", ModelPartBuilder.create()
				.uv(19, 14).cuboid(-0.5F, 0.25F, -0.5F, 4.0F, 0.0F, 1.0F), ModelTransform.of(1.0F, 0.0F, 0.5F, -0.2452F, -0.4063F, -0.8016F));
		ModelPartData rightmidforeleg = rightmidleg.addChild("rightmidforeleg", ModelPartBuilder.create()
				.uv(0, 21).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 3.0F, 1.0F), ModelTransform.of(3.5F, 0.25F, 0.0F, 0.0F, 0.0F, 0.0873F));
		rightmidforeleg.addChild("cube_r1", ModelPartBuilder.create()
				.uv(8, 21).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F), ModelTransform.of(0.0F, 3.0F, 0.0F, 0.0F, 0.0F, 1.9635F));
		
		ModelPartData rightbackleg = legs.addChild(EntityModelPartNames.RIGHT_HIND_LEG, ModelPartBuilder.create()
				.uv(19, 13).cuboid(-0.5F, 0.25F, -0.5F, 4.0F, 0.0F, 1.0F), ModelTransform.of(1.0F, -0.25F, 0.5F, 0.7692F, -0.86F, -0.7762F));
		rightbackleg.addChild("rightbackforeleg", ModelPartBuilder.create()
				.uv(4, 8).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F), ModelTransform.of(3.5F, 0.25F, 0.0F, 0.0F, 0.0F, -0.3491F));
		
		ModelPartData leftstrikeleg = legs.addChild("leftstrikeleg", ModelPartBuilder.create()
				.uv(18, 16).cuboid(-4.5F, 0.25F, -0.5F, 5.0F, 0.0F, 1.0F), ModelTransform.of(-1.0F, 0.0F, 0.0F, -0.7935F, -0.8029F, 0.941F));
		leftstrikeleg.addChild("leftstrikeforeleg", ModelPartBuilder.create()
				.uv(2, 8).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F), ModelTransform.of(-4.5F, 0.25F, 0.0F, 0.0F, 0.0F, 0.0873F));
		
		ModelPartData leftfrontleg = legs.addChild(EntityModelPartNames.LEFT_FRONT_LEG, ModelPartBuilder.create()
				.uv(18, 15).cuboid(-4.5F, 0.25F, -0.5F, 5.0F, 0.0F, 1.0F), ModelTransform.of(-1.0F, 0.0F, 0.0F, -0.1896F, -0.1978F, 0.7436F));
		leftfrontleg.addChild("leftfrontforeleg", ModelPartBuilder.create()
				.uv(0, 8).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F), ModelTransform.of(-4.5F, 0.25F, 0.0F, 0.0F, 0.0F, -0.1745F));
		
		ModelPartData leftmidleg = legs.addChild("leftmidleg", ModelPartBuilder.create()
				.uv(19, 12).cuboid(-3.5F, 0.25F, -0.5F, 4.0F, 0.0F, 1.0F), ModelTransform.of(-1.0F, 0.0F, 0.5F, -0.2452F, 0.4063F, 0.8016F));
		ModelPartData leftmidforeleg = leftmidleg.addChild("leftmidforeleg", ModelPartBuilder.create()
				.uv(14, 19).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 3.0F, 1.0F), ModelTransform.of(-3.5F, 0.25F, 0.0F, 0.0F, 0.0F, -0.0873F));
		leftmidforeleg.addChild("cube_r2", ModelPartBuilder.create()
				.uv(2, 21).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F), ModelTransform.of(0.0F, 3.0F, 0.0F, 0.0F, 0.0F, -1.9635F));
		
		ModelPartData leftbackleg = legs.addChild(EntityModelPartNames.LEFT_HIND_LEG, ModelPartBuilder.create()
				.uv(19, 11).cuboid(-3.5F, 0.25F, -0.5F, 4.0F, 0.0F, 1.0F), ModelTransform.of(-1.0F, -0.25F, 0.5F, 0.7692F, 0.86F, 0.7762F));
		leftbackleg.addChild("leftbackforeleg", ModelPartBuilder.create()
				.uv(6, 3).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F), ModelTransform.of(-3.5F, 0.25F, 0.0F, 0.0F, 0.0F, 0.3491F));
		
		
		ModelPartData rightfang = legs.addChild("rightfang", ModelPartBuilder.create()
				.uv(7, 21).cuboid(-0.5F, 0.25F, -0.5F, 3.0F, 0.0F, 1.0F), ModelTransform.of(1.0F, 0.0F, 0.0F, -1.1989F, 1.1409F, -0.6197F));
		rightfang.addChild("rightforefang", ModelPartBuilder.create()
				.uv(6, 21).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F), ModelTransform.of(2.5F, 0.25F, 0.0F, 0.0F, 0.0F, -0.2618F));
		ModelPartData leftfang = legs.addChild("leftfang", ModelPartBuilder.create()
				.uv(6, 20).cuboid(-2.5F, 0.25F, -0.5F, 3.0F, 0.0F, 1.0F), ModelTransform.of(-1.0F, 0.0F, 0.0F, -1.1989F, -1.1409F, 0.6197F));
		leftfang.addChild("leftforefang", ModelPartBuilder.create()
				.uv(4, 21).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F), ModelTransform.of(-2.5F, 0.25F, 0.0F, 0.0F, 0.0F, 0.2618F));
		
		ModelPartData torax = body.addChild("torax", ModelPartBuilder.create()
				.uv(16, 17).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 3.0F), ModelTransform.of(0.0F, -2.25F, 0.25F, 0.4363F, 0.0F, 0.0F));
		torax.addChild("backnettles_r1", ModelPartBuilder.create()
				.uv(0, 9).cuboid(-3.5F, 1.3505F, 1.125F, 7.0F, 0.0F, 6.0F), ModelTransform.of(0.0F, -0.25F, 0.0F, 0.48F, 0.0F, 0.0F));
		torax.addChild("midnettles_r1", ModelPartBuilder.create()
				.uv(-5, 27).cuboid(-3.5F, 0.8505F, 0.125F, 7.0F, 0.0F, 5.0F), ModelTransform.of(0.0F, -0.25F, 0.0F, 0.6981F, 0.0F, 0.0F));
		torax.addChild("forenettles_r1", ModelPartBuilder.create()
				.uv(0, 15).cuboid(-3.5F, -0.1495F, -0.625F, 7.0F, 0.0F, 5.0F), ModelTransform.of(0.0F, -0.25F, 0.0F, 0.7854F, 0.0F, 0.0F));
		
		ModelPartData stingers = torax.addChild("stingers", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 3.0F));
		stingers.addChild("stingerplane_r1", ModelPartBuilder.create()
				.uv(0, 0).cuboid(-3.5F, 0.0F, -1.0F, 7.0F, 0.0F, 9.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));
		
		return TexturedModelData.of(modelData, 32, 32);
	}
	
	@Override
	public void setAngles(EraserEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.head.yaw = headYaw * 0.017453292F;
		this.head.pitch = headPitch * 0.017453292F;
		
		this.rightHindLeg.roll = -0.7853982F;
		this.leftHindLeg.roll = 0.7853982F;
		this.rightMiddleLeg.roll = -0.58119464F;
		this.leftMiddleLeg.roll = 0.58119464F;
		this.rightFrontLeg.roll = -0.58119464F;
		this.leftFrontLeg.roll = 0.58119464F;
		this.rightHindLeg.yaw = 1.5707964F;
		this.leftHindLeg.yaw = -1.5707964F;
		this.rightMiddleLeg.yaw = 0.7853982F;
		this.leftMiddleLeg.yaw = -0.7853982F;
		this.rightFrontLeg.yaw = -0.7853982F;
		this.leftFrontLeg.yaw = 0.7853982F;
		
		float i = -(MathHelper.cos(limbAngle * 0.6662F * 2.0F + 0.0F) * 0.4F) * limbDistance;
		float j = -(MathHelper.cos(limbAngle * 0.6662F * 2.0F + 3.1415927F) * 0.4F) * limbDistance;
		float k = -(MathHelper.cos(limbAngle * 0.6662F * 2.0F + 1.5707964F) * 0.4F) * limbDistance;
		float l = -(MathHelper.cos(limbAngle * 0.6662F * 2.0F + 4.712389F) * 0.4F) * limbDistance;
		float m = Math.abs(MathHelper.sin(limbAngle * 0.6662F + 0.0F) * 0.4F) * limbDistance;
		float n = Math.abs(MathHelper.sin(limbAngle * 0.6662F + 3.1415927F) * 0.4F) * limbDistance;
		float o = Math.abs(MathHelper.sin(limbAngle * 0.6662F + 1.5707964F) * 0.4F) * limbDistance;
		
		ModelPart part = this.rightHindLeg;
		part.yaw += i;
		part = this.leftHindLeg;
		part.yaw += -i;
		part = this.rightMiddleLeg;
		part.yaw += j;
		part = this.leftMiddleLeg;
		part.yaw += -j;
		part = this.rightFrontLeg;
		part.yaw += k;
		part = this.leftFrontLeg;
		part.yaw += -l;
		part = this.rightHindLeg;
		part.roll += m;
		part = this.leftHindLeg;
		part.roll += -m;
		part = this.rightMiddleLeg;
		part.roll += n;
		part = this.leftMiddleLeg;
		part.roll += -n;
		part = this.rightFrontLeg;
		part.roll += o;
	}
	
	@Override
	public ModelPart getPart() {
		return this.root;
	}
}