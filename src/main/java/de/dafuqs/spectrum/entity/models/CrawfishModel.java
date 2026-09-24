package de.dafuqs.spectrum.entity.models;

import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;

public class CrawfishModel<T extends Entity> extends HierarchicalModel<T> {
	
	private final ModelPart root;
	private final ModelPart tail;
	
	public CrawfishModel(ModelPart root) {
		super();
		this.root = root;
		this.tail = root.getChild("tail");
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(12, 9).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.75F, -3.0F));
		
		PartDefinition whiskers = head.addOrReplaceChild("whiskers", CubeListBuilder.create(), PartPose.offset(1.65F, -1.25F, -0.5F));
		whiskers.addOrReplaceChild("whisker_left_r1", CubeListBuilder.create().texOffs(0, 9).addBox(-0.865F, -2.0945F, -1.25F, 2.0F, 5.0F, 4.0F, new CubeDeformation(-1.0F))
				.texOffs(0, 9).mirror().addBox(-2.235F, -2.0945F, -1.25F, 2.0F, 5.0F, 4.0F, new CubeDeformation(-1.0F)).mirror(false), PartPose.offsetAndRotation(-1.1F, -2.0F, -2.1F, 0.2618F, 0.0F, 0.0F));
		whiskers.addOrReplaceChild("whisker_base_right_r1", CubeListBuilder.create().texOffs(8, 19).mirror().addBox(0.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.75F)).mirror(false)
				.texOffs(8, 19).addBox(1.3F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.75F)), PartPose.offsetAndRotation(-3.3F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));
		
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(14, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 22.7F, -0.25F));
		body.addOrReplaceChild("butt_r1", CubeListBuilder.create().texOffs(0, 18).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.05F, 2.5F, -0.2182F, 0.0F, 0.0F));
		body.addOrReplaceChild("legs_right_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -1.0F, 2.0F, 4.0F, 5.0F, new CubeDeformation(-1.0F)), PartPose.offsetAndRotation(0.0F, 3.05F, 0.25F, 0.0F, 0.0F, 0.2793F));
		body.addOrReplaceChild("legs_left_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 5.0F, new CubeDeformation(-1.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 3.05F, 0.25F, 0.0F, 0.0F, -0.2793F));
		
		PartDefinition claws = body.addOrReplaceChild("claws", CubeListBuilder.create(), PartPose.offset(-2.0F, 0.8F, -0.75F));
		claws.addOrReplaceChild("upper_claw_right_r1", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3981F, 0.1611F, 0.0674F));
		claws.addOrReplaceChild("upper_claw_left_r1", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, 0.3981F, -0.1611F, -0.0674F));
		
		PartDefinition claw_itself = claws.addOrReplaceChild("claw_itself", CubeListBuilder.create(), PartPose.offset(-0.5F, 0.5F, -2.0F));
		claw_itself.addOrReplaceChild("inner_claw_left_r1", CubeListBuilder.create().texOffs(12, 14).addBox(-2.0F, -2.0F, -1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(-0.6F)), PartPose.offsetAndRotation(4.5F, 0.7F, -0.3F, 0.2657F, 0.1685F, 0.0456F));
		claw_itself.addOrReplaceChild("inner_claw_right_r1", CubeListBuilder.create().texOffs(12, 14).mirror().addBox(0.0F, -2.0F, -1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(-0.6F)).mirror(false), PartPose.offsetAndRotation(0.5F, 0.7F, -0.3F, 0.2657F, -0.1685F, -0.0456F));
		claw_itself.addOrReplaceChild("outer_claw_left_r1", CubeListBuilder.create().texOffs(22, 7).mirror().addBox(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(5.0F, 0.0F, 0.0F, 0.0F, -0.2618F, 0.0F));
		claw_itself.addOrReplaceChild("outer_claw_right_r1", CubeListBuilder.create().texOffs(22, 7).addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.2618F, 0.0F));
		
		PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(14, 5).addBox(-2.0F, -0.25F, 0.75F, 4.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.75F, 4.25F));
		tail.addOrReplaceChild("tail_r1", CubeListBuilder.create().texOffs(16, 19).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));
		
		return LayerDefinition.create(meshdefinition, 32, 32);
	}
	
	@Override
	public ModelPart root() {
		return root;
	}
	
	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 1.0F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.tail.yRot = -f * 0.45F * Mth.sin(0.6F * ageInTicks);
	}
	
}