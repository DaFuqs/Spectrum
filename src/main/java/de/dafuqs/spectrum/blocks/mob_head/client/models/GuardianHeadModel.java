package de.dafuqs.spectrum.blocks.mob_head.client.models;

import de.dafuqs.spectrum.blocks.mob_head.client.*;
import net.neoforged.api.distmarker.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@OnlyIn(Dist.CLIENT)
public class GuardianHeadModel extends SpectrumSkullModel {
	
	public GuardianHeadModel(ModelPart root) {
		super(root);
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition head = modelPartData.addOrReplaceChild(
				PartNames.HEAD,
				CubeListBuilder.create(),
				PartPose.ZERO
		);
		
		head.addOrReplaceChild(
				PartNames.HEAD,
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-6.0F, -14.0F, -8.0F, 12.0F, 12.0F, 16.0F)
						.texOffs(0, 28).addBox(-8.0F, -14.0F, -6.0F, 2.0F, 12.0F, 12.0F)
						.texOffs(0, 28).addBox(6.0F, -14.0F, -6.0F, 2.0F, 12.0F, 12.0F, true)
						.texOffs(16, 40).addBox(-6.0F, -16.0F, -6.0F, 12.0F, 2.0F, 12.0F)
						.texOffs(16, 40).addBox(-6.0F, -2.0F, -6.0F, 12.0F, 2.0F, 12.0F),
				PartPose.ZERO
		);
		
		head.addOrReplaceChild(PartNames.EYES, CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, 15.0F, 0.0F, 2.0F, 2.0F, 1.0F), PartPose.offset(0.0F, -24.0F, -8.25F));
		
		PartDefinition spikes = head.addOrReplaceChild("spikes", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, 0.0F));
		spikes.addOrReplaceChild("spike_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -17.0F, -1.0F, 2.0F, 9.0F, 2.0F), PartPose.rotation(0.0F, 0.0F, -0.7854F));
		spikes.addOrReplaceChild("spike_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -17.0F, -1.0F, 2.0F, 9.0F, 2.0F), PartPose.rotation(0.7854F, 0.0F, 0.0F));
		spikes.addOrReplaceChild("spike_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -17.0F, -1.0F, 2.0F, 9.0F, 2.0F), PartPose.rotation(2.3562F, 0.0F, 0.0F));
		spikes.addOrReplaceChild("spike_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -17.0F, -1.0F, 2.0F, 9.0F, 2.0F), PartPose.rotation(0.0F, 0.0F, 2.3562F));
		spikes.addOrReplaceChild("spike_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -17.0F, -1.0F, 2.0F, 9.0F, 2.0F), PartPose.rotation(0.0F, 0.0F, -2.3562F));
		spikes.addOrReplaceChild("spike_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -17.0F, -1.0F, 2.0F, 9.0F, 2.0F), PartPose.rotation(-2.3562F, 0.0F, 0.0F));
		spikes.addOrReplaceChild("spike_r7", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -17.0F, -1.0F, 2.0F, 9.0F, 2.0F), PartPose.rotation(-0.7854F, 0.0F, 0.0F));
		spikes.addOrReplaceChild("spike_r8", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -17.0F, -1.0F, 2.0F, 9.0F, 2.0F), PartPose.rotation(0.0F, 0.0F, 0.7854F));
		
		return LayerDefinition.create(modelData, 64, 64);
	}
	
	@Override
	public float getScale() {
		return 0.5F;
	}
	
}