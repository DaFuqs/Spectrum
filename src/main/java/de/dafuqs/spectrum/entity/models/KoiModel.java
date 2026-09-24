package de.dafuqs.spectrum.entity.models;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.*;

public class KoiModel<T extends Koi> extends CodModel<T> {
	
	public KoiModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(10, 8).addBox(-1.0F, -2.0F, -2.75F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 20.0F, -4.25F));
		PartDefinition whiskers = head.addOrReplaceChild("whiskers", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 4.25F));
		whiskers.addOrReplaceChild("whisker_right_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.135F, -1.0945F, -1.25F, 2.0F, 4.0F, 4.0F, new CubeDeformation(-1.0F)), PartPose.offsetAndRotation(-1.15F, -1.0F, -7.0F, 0.0F, 0.0F, 0.3491F));
		whiskers.addOrReplaceChild("whisker_left_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.865F, -1.0945F, -1.25F, 2.0F, 4.0F, 4.0F, new CubeDeformation(-1.0F)), PartPose.offsetAndRotation(1.15F, -1.0F, -7.0F, 0.0F, 0.0F, -0.3491F));
		
		PartDefinition body_front = partdefinition.addOrReplaceChild("body_front", CubeListBuilder.create().texOffs(0, 8).addBox(-1.0F, -3.0F, -3.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 21.0F, -0.5F));
		PartDefinition frontfin = body_front.addOrReplaceChild("frontfin", CubeListBuilder.create(), PartPose.offset(3.5F, 1.75F, 0.25F));
		frontfin.addOrReplaceChild("frontfin_right_r1", CubeListBuilder.create().texOffs(6, 20).addBox(-0.0972F, -0.6039F, -0.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.9204F, -1.6188F, -3.5F, 0.0F, 0.0F, 0.48F));
		frontfin.addOrReplaceChild("frontfin_left_r1", CubeListBuilder.create().texOffs(6, 20).addBox(0.0972F, -0.6039F, -0.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0796F, -1.6188F, -3.5F, 0.0F, 0.0F, -0.48F));
		
		PartDefinition body_back = partdefinition.addOrReplaceChild("body_back", CubeListBuilder.create().texOffs(12, 0).addBox(-1.0F, -3.0F, -0.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.5F))
		.texOffs(10, 14).addBox(0.0F, -5.25F, -1.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, 0.5F));
		PartDefinition backfin = body_back.addOrReplaceChild("backfin", CubeListBuilder.create(), PartPose.offset(-1.9395F, 0.9377F, 1.75F));
		backfin.addOrReplaceChild("backfin_right_r1", CubeListBuilder.create().texOffs(20, 6).addBox(0.0182F, -0.3038F, -0.3478F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -1.0F, -0.75F, -0.4363F, 0.0F, 0.48F));
		backfin.addOrReplaceChild("backfin_left_r1", CubeListBuilder.create().texOffs(20, 6).addBox(-0.0182F, -0.3038F, -0.3478F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.379F, -1.0F, -0.75F, -0.4363F, 0.0F, -0.48F));
		partdefinition.addOrReplaceChild("tail_fin", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, -2.0F, -0.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(18, 14).addBox(0.0F, -4.0F, 2.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 20).addBox(0.0F, 0.0F, 2.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 3.5F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}
	
}