package de.dafuqs.spectrum.blocks.structure;

import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;

public class ManxiModel {
	
	private final ModelPart root;
	private final ModelPart torso;
	private final ModelPart head;
	private final ModelPart rightear;
	private final ModelPart rightearjoint;
	private final ModelPart leftear;
	private final ModelPart leftearjoint;
	private final ModelPart rightarm;
	private final ModelPart leftarm;
	private final ModelPart tail;
	private final ModelPart tail1;
	private final ModelPart tail2;
	private final ModelPart tail3;
	private final ModelPart legs;
	private final ModelPart leftleg;
	private final ModelPart rightleg;
	
	public ManxiModel(ModelPart root) {
		this.root = root.getChild("root");
		this.torso = root.getChild("torso");
		this.head = root.getChild("head");
		this.rightear = root.getChild("rightear");
		this.rightearjoint = root.getChild("rightearjoint");
		this.leftear = root.getChild("leftear");
		this.leftearjoint = root.getChild("leftearjoint");
		this.rightarm = root.getChild("rightarm");
		this.leftarm = root.getChild("leftarm");
		this.tail = root.getChild("tail");
		this.tail1 = root.getChild("tail1");
		this.tail2 = root.getChild("tail2");
		this.tail3 = root.getChild("tail3");
		this.legs = root.getChild("legs");
		this.leftleg = root.getChild("leftleg");
		this.rightleg = root.getChild("rightleg");
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition root = modelPartData.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offsetAndRotation(2.5F, 19.75F, -2.0F, 0.0F, -0.1745F, -1.5708F));
		
		PartDefinition torso = root.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 34).addBox(-4.0F, -13.0F, -2.0F, 8.0F, 13.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(27, 29).addBox(-4.5F, -13.5F, -2.5F, 9.0F, 14.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 2.0F, 0.0F, -0.2618F, 0.1309F));
		
		PartDefinition breasts_r1 = torso.addOrReplaceChild("breasts_r1", CubeListBuilder.create().texOffs(27, 0).addBox(-3.99F, -3.5F, -0.5F, 8.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.85F, -3.25F, -0.5236F, 0.0F, 0.0F));
		
		PartDefinition head = torso.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 18).addBox(-4.4616F, -8.2745F, -3.7643F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-4.9616F, -8.7745F, -4.2643F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(20, 34).addBox(-6.4616F, -4.7745F, 0.4857F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(32, 24).addBox(-7.4616F, -6.2745F, 1.4857F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 34).addBox(3.5384F, -4.7745F, 0.4857F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(0, 18).addBox(3.5384F, -6.2745F, 1.4857F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0384F, -12.7255F, -0.2357F, -0.0219F, -0.5207F, -0.2843F));
		
		PartDefinition hairtuff_r1 = head.addOrReplaceChild("hairtuff_r1", CubeListBuilder.create().texOffs(49, 0).addBox(-4.0F, -3.0F, 0.0F, 8.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0384F, -6.7745F, -4.4143F, 0.0F, 0.0F, 0.5672F));
		
		PartDefinition rightear = head.addOrReplaceChild("rightear", CubeListBuilder.create().texOffs(44, 7).addBox(-1.7622F, -1.4363F, 0.0434F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.2116F, -5.7745F, 0.7357F, -0.0456F, 0.1685F, -0.2657F));
		
		PartDefinition rightearjoint = rightear.addOrReplaceChild("rightearjoint", CubeListBuilder.create().texOffs(42, 66).addBox(-2.0F, -9.0F, 0.0F, 4.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2378F, -1.4363F, 0.0434F, 0.2193F, -0.017F, -0.1298F));
		
		PartDefinition leftear = head.addOrReplaceChild("leftear", CubeListBuilder.create().texOffs(0, 23).addBox(-2.2378F, -1.4363F, 0.0434F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.2884F, -5.7745F, 0.7357F, -0.0456F, -0.1685F, 0.2657F));
		
		PartDefinition leftearjoint = leftear.addOrReplaceChild("leftearjoint", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -9.0F, 0.0F, 4.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2378F, -1.4363F, 0.0434F, -0.4314F, -0.0594F, -0.1642F));
		
		PartDefinition rightarm = torso.addOrReplaceChild("rightarm", CubeListBuilder.create().texOffs(28, 66).addBox(-3.5F, -1.5F, -2.0F, 3.0F, 13.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(55, 28).addBox(-4.0F, -2.0F, -2.5F, 4.0F, 14.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -11.5F, 0.0F, -1.2062F, 0.2865F, 0.1074F));
		
		PartDefinition leftarm = torso.addOrReplaceChild("leftarm", CubeListBuilder.create().texOffs(14, 66).addBox(0.5F, -1.5F, -2.0F, 3.0F, 13.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 51).addBox(0.0F, -2.0F, -2.5F, 4.0F, 14.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.25F, -11.5F, 0.5F, -0.9683F, 1.2673F, 0.1567F));
		
		PartDefinition tail = torso.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(27, 9).addBox(-1.0F, -1.0F, -1.0F, 4.0F, 6.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -3.75F, 2.0F, -0.3084F, 0.1032F, -0.0751F));
		
		PartDefinition tail1 = tail.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(58, 6).addBox(-0.5F, -4.0F, 0.0F, 3.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 8.0F, 0.3646F, 0.2865F, 0.1074F));
		
		PartDefinition tail2 = tail1.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(43, 14).addBox(-1.0F, -3.0F, 0.0F, 2.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 7.0F, -0.3919F, 0.0852F, 0.0189F));
		
		PartDefinition tail3 = tail2.addOrReplaceChild("tail3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.5F, 6.0F, -0.2618F, 0.0F, 0.0F));
		
		PartDefinition tailtip_r1 = tail3.addOrReplaceChild("tailtip_r1", CubeListBuilder.create().texOffs(44, 0).addBox(1.0F, 0.0F, -1.0F, 0.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -1.5F, 1.0F, -0.0436F, 0.0F, 0.0F));
		
		PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 2.0F));
		
		PartDefinition leftleg = legs.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(59, 47).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 13.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(19, 48).addBox(-2.5F, 0.5F, -1.5F, 5.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 0.0F, -1.5F, 0.0311F, 0.2739F, 0.4291F));
		
		PartDefinition rightleg = legs.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(55, 64).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 13.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(39, 48).addBox(-2.5F, 0.5F, -1.5F, 5.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, -1.0F, 0.5236F, 0.0873F, 0.0F));
		return LayerDefinition.create(modelData, 128, 128);
	}
	
}