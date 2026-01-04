package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;


public class EraserHeadModel extends SpectrumSkullModel {
	
	public EraserHeadModel(ModelPart root) {
		super(root);
	}
	
	@SuppressWarnings("unused")
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		
		PartDefinition body = modelData.getRoot().addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create(), PartPose.offset(0.0F, 0F, 0.0F));
		PartDefinition legs = body.addOrReplaceChild("legs", CubeListBuilder.create().texOffs(0, 20).addBox(-1.5F, -0.25F, -0.5F, 3.0F, 1.0F, 1.0F, CubeDeformation.NONE), PartPose.offset(0.0F, -1.5F, 0.0F));
		PartDefinition rightfang = legs.addOrReplaceChild("rightfang", CubeListBuilder.create().texOffs(7, 21).addBox(-0.5F, 0.25F, -0.5F, 3.0F, 0.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, -1.1989F, 1.1409F, -0.6197F));
		PartDefinition rightforefang = rightfang.addOrReplaceChild("rightforefang", CubeListBuilder.create().texOffs(6, 21).addBox(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(2.5F, 0.25F, 0.0F, 0.0F, 0.0F, -0.2618F));
		PartDefinition rightstrikeleg = legs.addOrReplaceChild("rightstrikeleg", CubeListBuilder.create().texOffs(19, 10).addBox(-0.5F, 0.25F, -0.5F, 5.0F, 0.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, -0.7935F, 0.8029F, -0.941F));
		PartDefinition rightstrikeforeleg = rightstrikeleg.addOrReplaceChild("rightstrikeforeleg", CubeListBuilder.create().texOffs(2, 13).addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(4.5F, 0.25F, 0.0F, 0.0F, 0.0F, -0.0873F));
		PartDefinition rightfrontleg = legs.addOrReplaceChild("rightfrontleg", CubeListBuilder.create().texOffs(19, 9).addBox(-0.5F, 0.25F, -0.5F, 5.0F, 0.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, -0.1896F, 0.1978F, -0.7436F));
		PartDefinition rightfrontforeleg = rightfrontleg.addOrReplaceChild("rightfrontforeleg", CubeListBuilder.create().texOffs(0, 13).addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(4.5F, 0.25F, 0.0F, 0.0F, 0.0F, 0.1745F));
		PartDefinition rightmidleg = legs.addOrReplaceChild("rightmidleg", CubeListBuilder.create().texOffs(19, 14).addBox(-0.5F, 0.25F, -0.5F, 4.0F, 0.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(1.0F, 0.0F, 0.5F, -0.2452F, -0.4063F, -0.8016F));
		PartDefinition rightmidforeleg = rightmidleg.addOrReplaceChild("rightmidforeleg", CubeListBuilder.create().texOffs(0, 21).addBox(0.0F, 0.0F, -0.5F, 0.0F, 3.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(3.5F, 0.25F, 0.0F, 0.0F, 0.0F, 0.0873F));
		PartDefinition cube_r1 = rightmidforeleg.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(8, 21).addBox(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.0F, 0.0F, 1.9635F));
		PartDefinition rightbackleg = legs.addOrReplaceChild("rightbackleg", CubeListBuilder.create().texOffs(19, 13).addBox(-0.5F, 0.25F, -0.5F, 4.0F, 0.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(1.0F, -0.25F, 0.5F, 0.7692F, -0.86F, -0.7762F));
		PartDefinition rightbackforeleg = rightbackleg.addOrReplaceChild("rightbackforeleg", CubeListBuilder.create().texOffs(4, 8).addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(3.5F, 0.25F, 0.0F, 0.0F, 0.0F, -0.3491F));
		PartDefinition leftfang = legs.addOrReplaceChild("leftfang", CubeListBuilder.create().texOffs(6, 20).addBox(-2.5F, 0.25F, -0.5F, 3.0F, 0.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, -1.1989F, -1.1409F, 0.6197F));
		PartDefinition leftforefang = leftfang.addOrReplaceChild("leftforefang", CubeListBuilder.create().texOffs(4, 21).addBox(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-2.5F, 0.25F, 0.0F, 0.0F, 0.0F, 0.2618F));
		PartDefinition leftstrikeleg = legs.addOrReplaceChild("leftstrikeleg", CubeListBuilder.create().texOffs(18, 16).addBox(-4.5F, 0.25F, -0.5F, 5.0F, 0.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, -0.7935F, -0.8029F, 0.941F));
		PartDefinition leftstrikeforeleg = leftstrikeleg.addOrReplaceChild("leftstrikeforeleg", CubeListBuilder.create().texOffs(2, 8).addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-4.5F, 0.25F, 0.0F, 0.0F, 0.0F, 0.0873F));
		PartDefinition leftfrontleg = legs.addOrReplaceChild("leftfrontleg", CubeListBuilder.create().texOffs(18, 15).addBox(-4.5F, 0.25F, -0.5F, 5.0F, 0.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, -0.1896F, -0.1978F, 0.7436F));
		PartDefinition leftfrontforeleg = leftfrontleg.addOrReplaceChild("leftfrontforeleg", CubeListBuilder.create().texOffs(0, 8).addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-4.5F, 0.25F, 0.0F, 0.0F, 0.0F, -0.1745F));
		PartDefinition leftmidleg = legs.addOrReplaceChild("leftmidleg", CubeListBuilder.create().texOffs(19, 12).addBox(-3.5F, 0.25F, -0.5F, 4.0F, 0.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.5F, -0.2452F, 0.4063F, 0.8016F));
		PartDefinition leftmidforeleg = leftmidleg.addOrReplaceChild("leftmidforeleg", CubeListBuilder.create().texOffs(14, 19).addBox(0.0F, 0.0F, -0.5F, 0.0F, 3.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-3.5F, 0.25F, 0.0F, 0.0F, 0.0F, -0.0873F));
		PartDefinition cube_r2 = leftmidforeleg.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(2, 21).addBox(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.0F, 0.0F, -1.9635F));
		PartDefinition leftbackleg = legs.addOrReplaceChild("leftbackleg", CubeListBuilder.create().texOffs(19, 11).addBox(-3.5F, 0.25F, -0.5F, 4.0F, 0.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-1.0F, -0.25F, 0.5F, 0.7692F, 0.86F, 0.7762F));
		PartDefinition leftbackforeleg = leftbackleg.addOrReplaceChild("leftbackforeleg", CubeListBuilder.create().texOffs(6, 3).addBox(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-3.5F, 0.25F, 0.0F, 0.0F, 0.0F, 0.3491F));
		PartDefinition torax = body.addOrReplaceChild("torax", CubeListBuilder.create().texOffs(16, 17).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 3.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, -2.25F, 0.25F, 0.4363F, 0.0F, 0.0F));
		PartDefinition backnettles_r1 = torax.addOrReplaceChild("backnettles_r1", CubeListBuilder.create().texOffs(0, 9).addBox(-3.5F, 1.3505F, 1.125F, 7.0F, 0.0F, 6.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, -0.25F, 0.0F, 0.48F, 0.0F, 0.0F));
		PartDefinition midnettles_r1 = torax.addOrReplaceChild("midnettles_r1", CubeListBuilder.create().texOffs(-5, 27).addBox(-3.5F, 0.8505F, 0.125F, 7.0F, 0.0F, 5.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, -0.25F, 0.0F, 0.6981F, 0.0F, 0.0F));
		PartDefinition forenettles_r1 = torax.addOrReplaceChild("forenettles_r1", CubeListBuilder.create().texOffs(0, 15).addBox(-3.5F, -0.1495F, -0.625F, 7.0F, 0.0F, 5.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, -0.25F, 0.0F, 0.7854F, 0.0F, 0.0F));
		PartDefinition stingers = torax.addOrReplaceChild("stingers", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 3.0F));
		PartDefinition stingerplane_r1 = stingers.addOrReplaceChild("stingerplane_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, 0.0F, -1.0F, 7.0F, 0.0F, 9.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));
		PartDefinition head = body.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, CubeDeformation.NONE), PartPose.offset(0.0F, -2.25F, 0.0F));
		PartDefinition nettles = head.addOrReplaceChild("nettles", CubeListBuilder.create().texOffs(0, 4).addBox(-1.5F, -4.0F, 0.0F, 3.0F, 5.0F, 0.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, -0.9F, -1.0F, -0.4363F, 0.0F, 0.0F));
		
		return LayerDefinition.create(modelData, 32, 32);
	}
	
}