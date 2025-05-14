package de.dafuqs.spectrum.entity.models;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

@Environment(EnvType.CLIENT)
public class EggLayingWoolyPigWoolEntityModel extends EntityModel<EggLayingWoolyPigEntity> {
	
	private final ModelPart torso;
	
	public EggLayingWoolyPigWoolEntityModel(ModelPart root) {
		super();
		this.torso = root.getChild("torso");
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		modelPartData.addOrReplaceChild("torso", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-6.5F, -15.5F, -9.5F, 13.0F, 13.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		
		return LayerDefinition.create(modelData, 128, 128);
	}
	
	@Override
	public void setupAnim(EggLayingWoolyPigEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	
	}
	
	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		if (young) {
			matrices.scale(0.6f, 0.6f, 0.6f);
			matrices.translate(0, 1, 0);
		}
		torso.render(matrices, vertices, light, overlay, color);
	}
}