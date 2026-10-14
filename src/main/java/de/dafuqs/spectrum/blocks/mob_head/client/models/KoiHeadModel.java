package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;


public class KoiHeadModel extends SpectrumSkullModel {
	
	public KoiHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(10, 8).addBox(-1.0F, -2.0F, -2.75F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -2.0F, 0.0F));
		PartDefinition whiskers = head.addOrReplaceChild("whiskers", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 4.25F));
		whiskers.addOrReplaceChild("whisker_right_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.135F, -1.0945F, -1.25F, 2.0F, 4.0F, 4.0F, new CubeDeformation(-1.0F)), PartPose.offsetAndRotation(-1.15F, -1.0F, -7.0F, 0.0F, 0.0F, 0.3491F));
		whiskers.addOrReplaceChild("whisker_left_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.865F, -1.0945F, -1.25F, 2.0F, 4.0F, 4.0F, new CubeDeformation(-1.0F)), PartPose.offsetAndRotation(1.15F, -1.0F, -7.0F, 0.0F, 0.0F, -0.3491F));
		
		PartDefinition body_front = head.addOrReplaceChild("body_front", CubeListBuilder.create().texOffs(0, 8).addBox(-1.0F, -3.0F, -3.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 1.0F, 3.75F));
		PartDefinition frontfin = body_front.addOrReplaceChild("frontfin", CubeListBuilder.create(), PartPose.offset(3.5F, 1.75F, 0.25F));
		frontfin.addOrReplaceChild("frontfin_right_r1", CubeListBuilder.create().texOffs(6, 20).addBox(-0.0972F, -0.6039F, -0.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.9204F, -1.6188F, -3.5F, 0.0F, 0.0F, 0.48F));
		frontfin.addOrReplaceChild("frontfin_left_r1", CubeListBuilder.create().texOffs(6, 20).addBox(0.0972F, -0.6039F, -0.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0796F, -1.6188F, -3.5F, 0.0F, 0.0F, -0.48F));
		
		return LayerDefinition.create(meshdefinition, 32, 32);
	}
	
}