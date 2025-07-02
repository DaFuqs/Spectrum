package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@Environment(EnvType.CLIENT)
public class ParrotHeadModel extends SpectrumSkullModel {

    public ParrotHeadModel(ModelPart root) {
        super(root);
    }
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition modelPartData2 = modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(2, 2).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.ZERO);
		modelPartData2.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(10, 0).addBox(-1.0F, -4.0F, -3.0F, 2.0F, 1.0F, 4.0F), PartPose.ZERO);
		modelPartData2.addOrReplaceChild("beak1", CubeListBuilder.create().texOffs(11, 7).addBox(-0.5F, -3.0F, -2.0F, 1.0F, 2.0F, 1.0F), PartPose.ZERO);
		modelPartData2.addOrReplaceChild("beak2", CubeListBuilder.create().texOffs(16, 7).addBox(-0.5F, -3.0F, -3.0F, 1.0F, 2.0F, 1.0F), PartPose.offset(0.0F, -0.25F, 0.05F));
		modelPartData2.addOrReplaceChild("feather", CubeListBuilder.create().texOffs(2, 18).addBox(0.0F, -7.0F, -2.0F, 0.0F, 5.0F, 4.0F), PartPose.offsetAndRotation(0.0F, -0.72F, -0.50F, -0.2214F, 0.0F, 0.0F));
		
		return LayerDefinition.create(modelData, 32, 32);
    }

}