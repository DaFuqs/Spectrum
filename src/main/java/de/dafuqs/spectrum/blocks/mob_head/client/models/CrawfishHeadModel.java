package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;


public class CrawfishHeadModel extends SpectrumSkullModel {
	
	public CrawfishHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(12, 9).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.ZERO);
		
		PartDefinition whiskers = head.addOrReplaceChild("whiskers", CubeListBuilder.create(), PartPose.offset(1.65F, -1.25F, -0.5F));
		whiskers.addOrReplaceChild("whisker_left_r1", CubeListBuilder.create().texOffs(0, 9).addBox(-0.865F, -2.0945F, -1.25F, 2.0F, 5.0F, 4.0F, new CubeDeformation(-1.0F))
				.texOffs(0, 9).mirror().addBox(-2.235F, -2.0945F, -1.25F, 2.0F, 5.0F, 4.0F, new CubeDeformation(-1.0F)).mirror(false), PartPose.offsetAndRotation(-1.1F, -2.0F, -2.1F, 0.2618F, 0.0F, 0.0F));
		whiskers.addOrReplaceChild("whisker_base_right_r1", CubeListBuilder.create().texOffs(8, 19).mirror().addBox(0.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.75F)).mirror(false)
				.texOffs(8, 19).addBox(1.3F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.75F)), PartPose.offsetAndRotation(-3.3F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));
		
		return LayerDefinition.create(meshdefinition, 32, 32);
	}
	
}