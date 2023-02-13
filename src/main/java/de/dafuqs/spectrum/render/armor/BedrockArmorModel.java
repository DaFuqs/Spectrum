package de.dafuqs.spectrum.render.armor;

import net.minecraft.client.model.*;

public class BedrockArmorModel {
	public final ModelPart head;
	public final ModelPart body;
	public final ModelPart rightArm;
	public final ModelPart left_arm;
	public final ModelPart rightLeg;
	public final ModelPart leftLeg;
	
	public BedrockArmorModel(ModelPart root) {
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.rightArm = root.getChild("right_arm");
		this.left_arm = root.getChild("left_arm");
		this.rightLeg = root.getChild("right_leg");
		this.leftLeg = root.getChild("left_leg");
	}
	
	public static ModelData getModelData() {
		ModelData data = new ModelData();
		var root = data.getRoot();
		
		root.addChild("hat", ModelPartBuilder.create(), ModelTransform.NONE);
		
		var head = root.addChild("head", ModelPartBuilder.create(), ModelTransform.NONE);
		
		head.addChild(
				"armor_head",
				ModelPartBuilder.create()
						.uv(0, 20)
						.cuboid(-4.5F, -8.5F, -4.5F, 9.0F, 9.0F, 9.0F)
						.uv(0, 0)
						.cuboid(-5.0F, -9.0F, -5.0F, 10.0F, 10.0F, 10.0F),
				ModelTransform.NONE
		);
		
		var body = root.addChild("body", ModelPartBuilder.create(), ModelTransform.NONE);
		
		body.addChild(
				"armor_body",
				ModelPartBuilder.create()
						.uv(31, 33)
						.cuboid(-4.5F, -0.5F, -2.5F, 9.0F, 13.0F, 5.0F)
						.uv(36, 20)
						.cuboid(-5.0F, 0.0F, -3.0F, 10.0F, 10.0F, 3.0F),
				ModelTransform.NONE
		);
		
		var rightArm = root.addChild(
				"right_arm",
				ModelPartBuilder.create(),
				ModelTransform.NONE
		);

		var armorRightArm = rightArm.addChild(
				"armor_right_arm",
				ModelPartBuilder.create()
						.uv(22, 51)
						.cuboid(-4.25F, -2.5F, -2.5F, 5.0F, 13.0F, 5.0F),
				ModelTransform.pivot(1.0F, 0.0F, 0.0F)
		);

		armorRightArm.addChild(
				"armor_right_arm_extra",
				ModelPartBuilder.create()
						.uv(57, 45)
						.cuboid(-4.0F, -1.5F, -3.0F, 6.0F, 4.0F, 6.0F, new Dilation(0.10F)),
				ModelTransform.of(-1.5F, -2.0F, 0.0F, 0.0F, 0.0F, -0.4363F)
		);
		
		var leftArm = root.addChild(
				"left_arm",
				ModelPartBuilder.create(),
				ModelTransform.NONE
		);
		
		var armorLeftArm = leftArm.addChild(
				"armor_left_arm",
				ModelPartBuilder.create()
						.uv(40, 0)
						.cuboid(-1.5F, -2.5F, -2.5F, 5.0F, 13.0F, 5.0F),
				ModelTransform.NONE
		);
		
		armorLeftArm.addChild(
				"armor_left_arm_extra",
				ModelPartBuilder.create()
						.uv(62, 20)
						.cuboid(-1.75F, -1.25F, -2.0F, 5.0F, 1.0F, 5.0F, new Dilation(0.10F))
						.uv(54, 12)
						.cuboid(-1.75F, -0.25F, -2.5F, 6.0F, 2.0F, 6.0F, new Dilation(0.10F)),
				ModelTransform.of(1.0F, -2.25F, -0.5F, 0.0F, 0.0F, 0.4363F)
		);
		
		var leftLeg = root.addChild(
				"left_leg",
				ModelPartBuilder.create(),
				ModelTransform.NONE
		);
		
		leftLeg.addChild(
				"armor_left_leg",
				ModelPartBuilder.create()
						.uv(42, 51)
						.cuboid(-2.5F, 0.0F, -2.5F, 5.0F, 10.0F, 5.0F),
				ModelTransform.NONE
		);
		
		leftLeg.addChild(
				"armor_left_boot",
				ModelPartBuilder.create()
						.uv(60, 0)
						.cuboid(-2.5F, 9.0F, -2.5F, 5.0F, 3.0F, 5.0F),
				ModelTransform.NONE
		);
		
		var rightLeg = root.addChild(
				"right_leg",
				ModelPartBuilder.create(),
				ModelTransform.NONE
		);
		
		rightLeg.addChild(
				"armor_right_leg",
				ModelPartBuilder.create()
						.uv(59, 28)
						.cuboid(-2.5F, 0.0F, -2.5F, 5.0F, 10.0F, 5.0F),
				ModelTransform.NONE
		);
		
		rightLeg.addChild(
				"right_boot",
				ModelPartBuilder.create()
						.uv(0, 61)
						.cuboid(-2.5F, 9.0F, -2.5F, 5.0F, 3.0F, 5.0F),
				ModelTransform.NONE
		);
		
		return data;
		
	}
	
}
