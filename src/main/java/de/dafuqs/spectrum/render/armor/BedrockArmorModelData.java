package de.dafuqs.spectrum.render.armor;

import net.minecraft.client.model.*;

public class BedrockArmorModelData {

	public static ModelData getModelData() {
		ModelData data = new ModelData();
		var root = data.getRoot();

		root.addChild("hat", ModelPartBuilder.create(), ModelTransform.NONE);

		var head = root.addChild("head", ModelPartBuilder.create()
				.uv(0, 0).cuboid(-4.5F, -8.5F, -4.5F, 9.0F, 9.0F, 9.0F, Dilation.NONE), ModelTransform.NONE);

		var head_plume = head.addChild("head_plume", ModelPartBuilder.create()
				.uv(38, 61).cuboid(0.0F, -0.0806F, 0.1517F, 0.0F, 11.0F, 7.0F, Dilation.NONE), ModelTransform.of(0.0F, -11.25F, 2.0F, 0.4363F, 0.0F, 0.0F));

		var visor = head.addChild("visor", ModelPartBuilder.create()
				.uv(30, 11).cuboid(-5.0F, -3.0F, -6.8F, 10.0F, 5.0F, 7.0F, Dilation.NONE), ModelTransform.of(0.0F, -6.0F, 1.5F, -0.1745F, 0.0F, 0.0F));

		var visor_frill_left = visor.addChild("visor_frill_left", ModelPartBuilder.create()
				.uv(38, 14).cuboid(1.25F, -4.0F, -4.5F, 0.0F, 8.0F, 9.0F, Dilation.NONE), ModelTransform.of(5.1F, -2.0F, 1.2F, 0.5796F, 0.2344F, 0.1939F));

		var visor_frill_right = visor.addChild("visor_frill_right", ModelPartBuilder.create()
				.uv(56, 14).cuboid(-1.25F, -4.0F, -4.5F, 0.0F, 8.0F, 9.0F, Dilation.NONE), ModelTransform.of(-5.1F, -2.0F, 1.2F, 0.5796F, -0.2344F, -0.1939F));

		var body = root.addChild("body", ModelPartBuilder.create()
				.uv(0, 31).cuboid(-5.0F, -1.0F, -3.0F, 9.0F, 13.0F, 5.0F, Dilation.NONE)
				.uv(28, 41).cuboid(-5.0F, -1.0F, -3.75F, 9.0F, 12.0F, 3.0F, Dilation.NONE), ModelTransform.pivot(0.5F, 0.5F, 0.5F));

		var body_roll = body.addChild("body_roll", ModelPartBuilder.create()
				.uv(28, 31).cuboid(-6.0F, -3.5F, -1.25F, 12.0F, 5.0F, 5.0F, Dilation.NONE), ModelTransform.of(-0.5F, 1.5F, 2.5F, -0.7854F, 0.0F, 0.0F));

		var body_collar_inner = body.addChild("body_collar_inner", ModelPartBuilder.create()
				.uv(0, 83).cuboid(-5.0F, -1.5F, -4.25F, 9.0F, 5.0F, 7.0F, Dilation.NONE)
				.uv(0, 18).cuboid(-6.0F, -1.5F, -5.25F, 11.0F, 5.0F, 8.0F, Dilation.NONE), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		var loincloth = body.addChild("loincloth", ModelPartBuilder.create()
				.uv(72, 78).cuboid(-3.5F, -2.0F, -0.25F, 7.0F, 15.0F, 0.0F, Dilation.NONE), ModelTransform.pivot(-0.5F, 9.5F, -3.25F));

		var cape = body.addChild("cape", ModelPartBuilder.create()
				.uv(64, 14).cuboid(-5.0F, 1.4167F, -0.6833F, 10.0F, 5.0F, 3.0F, Dilation.NONE)
				.uv(0, 49).cuboid(-4.5F, -1.8333F, -0.0833F, 9.0F, 21.0F, 0.0F, Dilation.NONE)
				.uv(62, 78).cuboid(-7.0F, 0.1667F, 0.1667F, 5.0F, 21.0F, 0.0F, Dilation.NONE)
				.uv(52, 68).cuboid(2.0F, 0.1667F, 0.1667F, 5.0F, 21.0F, 0.0F, Dilation.NONE), ModelTransform.pivot(-0.5F, 1.8333F, 2.6833F));

		var right_arm = root.addChild("right_arm", ModelPartBuilder.create()
				.uv(18, 56).cuboid(-4.0F, -1.5F, -2.5F, 5.0F, 12.0F, 5.0F, Dilation.NONE)
				.uv(56, 35).cuboid(-4.5F, -1.5F, -3.0F, 6.0F, 6.0F, 6.0F, Dilation.NONE), ModelTransform.pivot(-4.0F, 2.0F, 0.0F));

		var right_arm_pauldron_top = right_arm.addChild("right_arm_pauldron_top", ModelPartBuilder.create()
				.uv(74, 22).cuboid(1.0F, -1.0F, -2.5F, 2.0F, 5.0F, 7.0F, Dilation.NONE)
				.uv(57, 3).cuboid(-4.0F, 0.0F, -2.5F, 5.0F, 4.0F, 7.0F, Dilation.NONE), ModelTransform.of(-3.0F, -2.5F, -1.0F, 0.0F, 0.0F, -0.2618F));

		var left_arm = root.addChild("left_arm", ModelPartBuilder.create()
				.uv(47, 51).cuboid(-1.0F, -2.0F, -2.5F, 5.0F, 12.0F, 5.0F, Dilation.NONE)
				.uv(16, 73).cuboid(0.5F, 0.0F, -3.0F, 4.0F, 5.0F, 6.0F, Dilation.NONE), ModelTransform.pivot(4.0F, 2.5F, 0.0F));

		var left_arm_bauldron_top = left_arm.addChild("left_arm_bauldron_top", ModelPartBuilder.create()
				.uv(74, 0).cuboid(-0.5F, -5.0F, -3.0F, 5.0F, 1.0F, 6.0F, Dilation.NONE)
				.uv(36, 0).cuboid(-1.5F, -4.0F, -3.5F, 7.0F, 3.0F, 7.0F, Dilation.NONE), ModelTransform.of(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		var left_leg = root.addChild("left_leg", ModelPartBuilder.create(), ModelTransform.pivot(2.0F, 12.0F, 0.0F));

		var left_leg_armor = left_leg.addChild("left_leg_armor", ModelPartBuilder.create()
				.uv(67, 47).cuboid(-2.5F, -0.5F, -2.5F, 5.0F, 10.0F, 5.0F, Dilation.NONE), ModelTransform.NONE);

		var left_boot = left_leg.addChild("left_boot", ModelPartBuilder.create()
				.uv(82, 42).cuboid(-2.5F, 9.5F, -2.5F, 5.0F, 3.0F, 5.0F, Dilation.NONE)
				.uv(32, 80).mirrored().cuboid(-2.5F, 5.0F, -1.25F, 5.0F, 6.0F, 4.0F, Dilation.NONE).mirrored(false), ModelTransform.NONE);

		var right_leg = root.addChild("right_leg", ModelPartBuilder.create(), ModelTransform.pivot(2.0F, 12.0F, 0.0F));

		var right_leg_armor = right_leg.addChild("right_leg_armor", ModelPartBuilder.create()
				.uv(62, 63).cuboid(-2.5F, -0.5F, -2.5F, 5.0F, 10.0F, 5.0F, Dilation.NONE), ModelTransform.NONE);

		var right_boot = right_leg.addChild("right_boot", ModelPartBuilder.create()
				.uv(32, 80).cuboid(-2.5F, 5.0F, -1.25F, 5.0F, 6.0F, 4.0F, Dilation.NONE)
				.uv(80, 34).cuboid(-2.5F, 9.5F, -2.5F, 5.0F, 3.0F, 5.0F, Dilation.NONE), ModelTransform.NONE);
		return data;
	}
}