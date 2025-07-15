package de.dafuqs.spectrum.entity.models;

import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.*;

public class EraserEntityModel extends HierarchicalModel<EraserEntity> {
	
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart rightHindLeg;
	private final ModelPart leftHindLeg;
	private final ModelPart rightMiddleLeg;
	private final ModelPart leftMiddleLeg;
	private final ModelPart rightFrontLeg;
	private final ModelPart leftFrontLeg;
	private final ModelPart rightStrikeLeg;
	private final ModelPart leftStrikeLeg;
	
	public EraserEntityModel(ModelPart root) {
		this.root = root;
		
		ModelPart body = root.getChild(PartNames.BODY);
		this.head = body.getChild(PartNames.HEAD);
		ModelPart legs = body.getChild("legs");
		this.rightHindLeg = legs.getChild(PartNames.RIGHT_HIND_LEG);
		this.leftHindLeg = legs.getChild(PartNames.LEFT_HIND_LEG);
		this.rightMiddleLeg = legs.getChild(PartNames.RIGHT_FRONT_LEG);
		this.leftMiddleLeg = legs.getChild(PartNames.LEFT_FRONT_LEG);
		this.rightFrontLeg = legs.getChild("rightstrikeleg");
		this.leftFrontLeg = legs.getChild("leftstrikeleg");
		this.rightStrikeLeg = legs.getChild("rightstrikeleg");
		this.leftStrikeLeg = legs.getChild("leftstrikeleg");
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition body = modelPartData.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		
		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F), PartPose.offset(0.0F, -2.25F, 0.0F));
		head.addOrReplaceChild("nettles", CubeListBuilder.create()
				.texOffs(0, 4).addBox(-1.5F, -4.0F, 0.0F, 3.0F, 5.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -0.9F, -1.0F, -0.4363F, 0.0F, 0.0F));
		
		PartDefinition legs = body.addOrReplaceChild("legs", CubeListBuilder.create()
				.texOffs(0, 20).addBox(-1.5F, -0.25F, -0.5F, 3.0F, 1.0F, 1.0F), PartPose.offset(0.0F, -1.5F, 0.0F));
		
		PartDefinition rightstrikeleg = legs.addOrReplaceChild("rightstrikeleg", CubeListBuilder.create()
				.texOffs(19, 10).addBox(-0.5F, 0.25F, -0.5F, 5.0F, 0.0F, 1.0F), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, -0.7935F, 0.8029F, -0.941F));
		rightstrikeleg.addOrReplaceChild("rightstrikeforeleg", CubeListBuilder.create()
				.texOffs(2, 13).addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(4.5F, 0.25F, 0.0F, 0.0F, 0.0F, -0.0873F));
		
		PartDefinition rightfrontleg = legs.addOrReplaceChild(PartNames.RIGHT_FRONT_LEG, CubeListBuilder.create()
				.texOffs(19, 9).addBox(-0.5F, 0.25F, -0.5F, 5.0F, 0.0F, 1.0F), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, -0.1896F, 0.1978F, -0.7436F));
		rightfrontleg.addOrReplaceChild("rightfrontforeleg", CubeListBuilder.create()
				.texOffs(0, 13).addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(4.5F, 0.25F, 0.0F, 0.0F, 0.0F, 0.1745F));
		
		PartDefinition rightmidleg = legs.addOrReplaceChild("rightmidleg", CubeListBuilder.create()
				.texOffs(19, 14).addBox(-0.5F, 0.25F, -0.5F, 4.0F, 0.0F, 1.0F), PartPose.offsetAndRotation(1.0F, 0.0F, 0.5F, -0.2452F, -0.4063F, -0.8016F));
		PartDefinition rightmidforeleg = rightmidleg.addOrReplaceChild("rightmidforeleg", CubeListBuilder.create()
				.texOffs(0, 21).addBox(0.0F, 0.0F, -0.5F, 0.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(3.5F, 0.25F, 0.0F, 0.0F, 0.0F, 0.0873F));
		rightmidforeleg.addOrReplaceChild("cube_r1", CubeListBuilder.create()
				.texOffs(8, 21).addBox(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.0F, 0.0F, 1.9635F));
		
		PartDefinition rightbackleg = legs.addOrReplaceChild(PartNames.RIGHT_HIND_LEG, CubeListBuilder.create()
				.texOffs(19, 13).addBox(-0.5F, 0.25F, -0.5F, 4.0F, 0.0F, 1.0F), PartPose.offsetAndRotation(1.0F, -0.25F, 0.5F, 0.7692F, -0.86F, -0.7762F));
		rightbackleg.addOrReplaceChild("rightbackforeleg", CubeListBuilder.create()
				.texOffs(4, 8).addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(3.5F, 0.25F, 0.0F, 0.0F, 0.0F, -0.3491F));
		
		PartDefinition leftstrikeleg = legs.addOrReplaceChild("leftstrikeleg", CubeListBuilder.create()
				.texOffs(18, 16).addBox(-4.5F, 0.25F, -0.5F, 5.0F, 0.0F, 1.0F), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, -0.7935F, -0.8029F, 0.941F));
		leftstrikeleg.addOrReplaceChild("leftstrikeforeleg", CubeListBuilder.create()
				.texOffs(2, 8).addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(-4.5F, 0.25F, 0.0F, 0.0F, 0.0F, 0.0873F));
		
		PartDefinition leftfrontleg = legs.addOrReplaceChild(PartNames.LEFT_FRONT_LEG, CubeListBuilder.create()
				.texOffs(18, 15).addBox(-4.5F, 0.25F, -0.5F, 5.0F, 0.0F, 1.0F), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, -0.1896F, -0.1978F, 0.7436F));
		leftfrontleg.addOrReplaceChild("leftfrontforeleg", CubeListBuilder.create()
				.texOffs(0, 8).addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(-4.5F, 0.25F, 0.0F, 0.0F, 0.0F, -0.1745F));
		
		PartDefinition leftmidleg = legs.addOrReplaceChild("leftmidleg", CubeListBuilder.create()
				.texOffs(19, 12).addBox(-3.5F, 0.25F, -0.5F, 4.0F, 0.0F, 1.0F), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.5F, -0.2452F, 0.4063F, 0.8016F));
		PartDefinition leftmidforeleg = leftmidleg.addOrReplaceChild("leftmidforeleg", CubeListBuilder.create()
				.texOffs(14, 19).addBox(0.0F, 0.0F, -0.5F, 0.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(-3.5F, 0.25F, 0.0F, 0.0F, 0.0F, -0.0873F));
		leftmidforeleg.addOrReplaceChild("cube_r2", CubeListBuilder.create()
				.texOffs(2, 21).addBox(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.0F, 0.0F, -1.9635F));
		
		PartDefinition leftbackleg = legs.addOrReplaceChild(PartNames.LEFT_HIND_LEG, CubeListBuilder.create()
				.texOffs(19, 11).addBox(-3.5F, 0.25F, -0.5F, 4.0F, 0.0F, 1.0F), PartPose.offsetAndRotation(-1.0F, -0.25F, 0.5F, 0.7692F, 0.86F, 0.7762F));
		leftbackleg.addOrReplaceChild("leftbackforeleg", CubeListBuilder.create()
				.texOffs(6, 3).addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(-3.5F, 0.25F, 0.0F, 0.0F, 0.0F, 0.3491F));
		
		
		PartDefinition rightfang = legs.addOrReplaceChild("rightfang", CubeListBuilder.create()
				.texOffs(7, 21).addBox(-0.5F, 0.25F, -0.5F, 3.0F, 0.0F, 1.0F), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, -1.1989F, 1.1409F, -0.6197F));
		rightfang.addOrReplaceChild("rightforefang", CubeListBuilder.create()
				.texOffs(6, 21).addBox(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(2.5F, 0.25F, 0.0F, 0.0F, 0.0F, -0.2618F));
		PartDefinition leftfang = legs.addOrReplaceChild("leftfang", CubeListBuilder.create()
				.texOffs(6, 20).addBox(-2.5F, 0.25F, -0.5F, 3.0F, 0.0F, 1.0F), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, -1.1989F, -1.1409F, 0.6197F));
		leftfang.addOrReplaceChild("leftforefang", CubeListBuilder.create()
				.texOffs(4, 21).addBox(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(-2.5F, 0.25F, 0.0F, 0.0F, 0.0F, 0.2618F));
		
		PartDefinition torax = body.addOrReplaceChild("torax", CubeListBuilder.create()
				.texOffs(16, 17).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 3.0F), PartPose.offsetAndRotation(0.0F, -2.25F, 0.25F, 0.4363F, 0.0F, 0.0F));
		torax.addOrReplaceChild("backnettles_r1", CubeListBuilder.create()
				.texOffs(0, 9).addBox(-3.5F, 1.3505F, 1.125F, 7.0F, 0.0F, 6.0F), PartPose.offsetAndRotation(0.0F, -0.25F, 0.0F, 0.48F, 0.0F, 0.0F));
		torax.addOrReplaceChild("midnettles_r1", CubeListBuilder.create()
				.texOffs(-5, 27).addBox(-3.5F, 0.8505F, 0.125F, 7.0F, 0.0F, 5.0F), PartPose.offsetAndRotation(0.0F, -0.25F, 0.0F, 0.6981F, 0.0F, 0.0F));
		torax.addOrReplaceChild("forenettles_r1", CubeListBuilder.create()
				.texOffs(0, 15).addBox(-3.5F, -0.1495F, -0.625F, 7.0F, 0.0F, 5.0F), PartPose.offsetAndRotation(0.0F, -0.25F, 0.0F, 0.7854F, 0.0F, 0.0F));
		
		PartDefinition stingers = torax.addOrReplaceChild("stingers", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 3.0F));
		stingers.addOrReplaceChild("stingerplane_r1", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-3.5F, 0.0F, -1.0F, 7.0F, 0.0F, 9.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));
		
		return LayerDefinition.create(modelData, 32, 32);
	}
	
	private static final float PI = (float) Math.PI;
	
	@Override
	public void setupAnim(EraserEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.head.yRot = headYaw * 0.017453292F;
		this.head.xRot = headPitch * 0.017453292F;
		
		float i = -(Mth.cos(limbAngle * 0.6662F * 2.0F + 0.0F * PI) * 0.4F) * limbDistance;
		float j = -(Mth.cos(limbAngle * 0.6662F * 2.0F + 1.0F * PI) * 0.4F) * limbDistance;
		float k = -(Mth.cos(limbAngle * 0.6662F * 2.0F + 0.5F * 3.1415927F) * 0.4F) * limbDistance;
		float l = -(Mth.cos(limbAngle * 0.6662F * 2.0F + 1.5F * PI) * 0.4F) * limbDistance;
		float m = Math.abs(Mth.sin(limbAngle * 0.6662F + 0.0F * PI) * 0.4F) * limbDistance;
		float n = Math.abs(Mth.sin(limbAngle * 0.6662F + 1.0F * PI) * 0.4F) * limbDistance;
		float o = Math.abs(Mth.sin(limbAngle * 0.6662F + 0.5F * 3.1415927F) * 0.4F) * limbDistance;
		float p = Math.abs(Mth.sin(limbAngle * 0.6662F + 1.5F * PI) * 0.4F) * limbDistance;
		
		this.rightHindLeg.yRot = this.rightHindLeg.getInitialPose().yRot + i;
		this.leftHindLeg.yRot = this.leftHindLeg.getInitialPose().yRot - i;
		this.rightMiddleLeg.yRot = this.rightMiddleLeg.getInitialPose().yRot + j;
		this.leftMiddleLeg.yRot = this.leftMiddleLeg.getInitialPose().yRot - j;
		this.rightFrontLeg.yRot = this.rightFrontLeg.getInitialPose().yRot + k;
		this.leftFrontLeg.yRot = this.leftFrontLeg.getInitialPose().yRot - k;
		this.rightStrikeLeg.yRot = this.rightStrikeLeg.getInitialPose().yRot + l;
		this.leftStrikeLeg.yRot = this.leftStrikeLeg.getInitialPose().yRot - l;
		this.rightHindLeg.zRot = this.rightHindLeg.getInitialPose().zRot + m;
		this.leftHindLeg.zRot = this.leftHindLeg.getInitialPose().zRot - m;
		this.rightMiddleLeg.zRot = this.rightMiddleLeg.getInitialPose().zRot + n;
		this.leftMiddleLeg.zRot = this.leftMiddleLeg.getInitialPose().zRot - n;
		this.rightFrontLeg.zRot = this.rightFrontLeg.getInitialPose().zRot + o;
		this.leftFrontLeg.zRot = this.leftFrontLeg.getInitialPose().zRot - o;
		this.rightStrikeLeg.zRot = this.rightStrikeLeg.getInitialPose().zRot + p;
		this.leftStrikeLeg.zRot = this.leftStrikeLeg.getInitialPose().zRot - p;
	}
	
	@Override
	public ModelPart root() {
		return this.root;
	}
}
