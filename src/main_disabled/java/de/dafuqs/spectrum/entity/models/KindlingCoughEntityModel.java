package de.dafuqs.spectrum.entity.models;

import de.dafuqs.spectrum.entity.entity.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@Environment(EnvType.CLIENT)
public class KindlingCoughEntityModel extends HierarchicalModel<KindlingCoughEntity> {
	
	private final ModelPart root;
	
	public KindlingCoughEntityModel(ModelPart root) {
		this.root = root;
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		
		modelData.getRoot().addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0)
				.addBox(-4.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F)
				.addBox(0.0F, -4.0F, 0.0F, 2.0F, 2.0F, 2.0F)
				.addBox(0.0F, 0.0F, -4.0F, 2.0F, 2.0F, 2.0F)
				.addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F)
				.addBox(2.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F)
				.addBox(0.0F, 2.0F, 0.0F, 2.0F, 2.0F, 2.0F)
				.addBox(0.0F, 0.0F, 2.0F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
		
		return LayerDefinition.create(modelData, 64, 32);
	}
	
	public void setupAnim(KindlingCoughEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}
	
	public ModelPart root() {
		return this.root;
	}
}
