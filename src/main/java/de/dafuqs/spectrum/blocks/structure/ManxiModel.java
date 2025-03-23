package de.dafuqs.spectrum.blocks.structure;

import net.minecraft.client.model.*;

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

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.of(2.5F, 19.75F, -2.0F, 0.0F, -0.1745F, -1.5708F));

		ModelPartData torso = root.addChild("torso", ModelPartBuilder.create().uv(0, 34).cuboid(-4.0F, -13.0F, -2.0F, 8.0F, 13.0F, 4.0F, new Dilation(0.0F))
		.uv(27, 29).cuboid(-4.5F, -13.5F, -2.5F, 9.0F, 14.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 2.0F, 0.0F, -0.2618F, 0.1309F));

		ModelPartData breasts_r1 = torso.addChild("breasts_r1", ModelPartBuilder.create().uv(27, 0).cuboid(-3.99F, -3.5F, -0.5F, 8.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -7.85F, -3.25F, -0.5236F, 0.0F, 0.0F));

		ModelPartData head = torso.addChild("head", ModelPartBuilder.create().uv(0, 18).cuboid(-4.4616F, -8.2745F, -3.7643F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-4.9616F, -8.7745F, -4.2643F, 9.0F, 9.0F, 9.0F, new Dilation(0.0F))
		.uv(20, 34).cuboid(-6.4616F, -4.7745F, 0.4857F, 2.0F, 4.0F, 0.0F, new Dilation(0.0F))
		.uv(32, 24).cuboid(-7.4616F, -6.2745F, 1.4857F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F))
		.uv(0, 34).cuboid(3.5384F, -4.7745F, 0.4857F, 2.0F, 4.0F, 0.0F, new Dilation(0.0F))
		.uv(0, 18).cuboid(3.5384F, -6.2745F, 1.4857F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.0384F, -12.7255F, -0.2357F, -0.0219F, -0.5207F, -0.2843F));

		ModelPartData hairtuff_r1 = head.addChild("hairtuff_r1", ModelPartBuilder.create().uv(49, 0).cuboid(-4.0F, -3.0F, 0.0F, 8.0F, 7.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.0384F, -6.7745F, -4.4143F, 0.0F, 0.0F, 0.5672F));

		ModelPartData rightear = head.addChild("rightear", ModelPartBuilder.create().uv(44, 7).cuboid(-1.7622F, -1.4363F, 0.0434F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-4.2116F, -5.7745F, 0.7357F, -0.0456F, 0.1685F, -0.2657F));

		ModelPartData rightearjoint = rightear.addChild("rightearjoint", ModelPartBuilder.create().uv(42, 66).cuboid(-2.0F, -9.0F, 0.0F, 4.0F, 9.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.2378F, -1.4363F, 0.0434F, 0.2193F, -0.017F, -0.1298F));

		ModelPartData leftear = head.addChild("leftear", ModelPartBuilder.create().uv(0, 23).cuboid(-2.2378F, -1.4363F, 0.0434F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.2884F, -5.7745F, 0.7357F, -0.0456F, -0.1685F, 0.2657F));

		ModelPartData leftearjoint = leftear.addChild("leftearjoint", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -9.0F, 0.0F, 4.0F, 9.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.2378F, -1.4363F, 0.0434F, -0.4314F, -0.0594F, -0.1642F));

		ModelPartData rightarm = torso.addChild("rightarm", ModelPartBuilder.create().uv(28, 66).cuboid(-3.5F, -1.5F, -2.0F, 3.0F, 13.0F, 4.0F, new Dilation(0.0F))
		.uv(55, 28).cuboid(-4.0F, -2.0F, -2.5F, 4.0F, 14.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-3.5F, -11.5F, 0.0F, -1.2062F, 0.2865F, 0.1074F));

		ModelPartData leftarm = torso.addChild("leftarm", ModelPartBuilder.create().uv(14, 66).cuboid(0.5F, -1.5F, -2.0F, 3.0F, 13.0F, 4.0F, new Dilation(0.0F))
		.uv(0, 51).cuboid(0.0F, -2.0F, -2.5F, 4.0F, 14.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(3.25F, -11.5F, 0.5F, -0.9683F, 1.2673F, 0.1567F));

		ModelPartData tail = torso.addChild("tail", ModelPartBuilder.create().uv(27, 9).cuboid(-1.0F, -1.0F, -1.0F, 4.0F, 6.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, -3.75F, 2.0F, -0.3084F, 0.1032F, -0.0751F));

		ModelPartData tail1 = tail.addChild("tail1", ModelPartBuilder.create().uv(58, 6).cuboid(-0.5F, -4.0F, 0.0F, 3.0F, 5.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 4.0F, 8.0F, 0.3646F, 0.2865F, 0.1074F));

		ModelPartData tail2 = tail1.addChild("tail2", ModelPartBuilder.create().uv(43, 14).cuboid(-1.0F, -3.0F, 0.0F, 2.0F, 4.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 0.0F, 7.0F, -0.3919F, 0.0852F, 0.0189F));

		ModelPartData tail3 = tail2.addChild("tail3", ModelPartBuilder.create(), ModelTransform.of(0.0F, -0.5F, 6.0F, -0.2618F, 0.0F, 0.0F));

		ModelPartData tailtip_r1 = tail3.addChild("tailtip_r1", ModelPartBuilder.create().uv(44, 0).cuboid(1.0F, 0.0F, -1.0F, 0.0F, 3.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, -1.5F, 1.0F, -0.0436F, 0.0F, 0.0F));

		ModelPartData legs = root.addChild("legs", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, 2.0F));

		ModelPartData leftleg = legs.addChild("leftleg", ModelPartBuilder.create().uv(59, 47).cuboid(-2.0F, 0.0F, -1.0F, 4.0F, 13.0F, 4.0F, new Dilation(0.0F))
		.uv(19, 48).cuboid(-2.5F, 0.5F, -1.5F, 5.0F, 13.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(1.5F, 0.0F, -1.5F, 0.0311F, 0.2739F, 0.4291F));

		ModelPartData rightleg = legs.addChild("rightleg", ModelPartBuilder.create().uv(55, 64).cuboid(-2.0F, 0.0F, -1.0F, 4.0F, 13.0F, 4.0F, new Dilation(0.0F))
		.uv(39, 48).cuboid(-2.5F, 0.5F, -1.5F, 5.0F, 13.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 0.0F, -1.0F, 0.5236F, 0.0873F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}

}