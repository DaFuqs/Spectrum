package de.dafuqs.spectrum.render.armor;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.*;
import net.minecraft.world.entity.player.*;

public class BedrockArmorModel extends HumanoidArmorModel<LivingEntity> {
	final EquipmentSlot slot;
	
	public final ModelPart head;
	public final ModelPart body;
	public final ModelPart rightArm;
	public final ModelPart left_arm;
	public final ModelPart rightLeg;
	public final ModelPart leftLeg;
	
	public BedrockArmorModel(ModelPart root, EquipmentSlot slot) {
		super(root);
		this.slot = slot;
		
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.rightArm = root.getChild("right_arm");
		this.left_arm = root.getChild("left_arm");
		this.rightLeg = root.getChild("right_leg");
		this.leftLeg = root.getChild("left_leg");
	}
	
	public static MeshDefinition getMeshDefinition() {
		MeshDefinition data = new MeshDefinition();
		var root = data.getRoot();
		
		root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
		
		var head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		
		head.addOrReplaceChild(
				"armor_head",
				CubeListBuilder.create()
						.texOffs(0, 20)
						.addBox(-4.5F, -8.5F, -4.5F, 9.0F, 9.0F, 9.0F)
						.texOffs(0, 0)
						.addBox(-5.0F, -9.0F, -5.0F, 10.0F, 10.0F, 10.0F),
				PartPose.ZERO
		);
		
		var body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
		
		body.addOrReplaceChild(
				"armor_body",
				CubeListBuilder.create()
						.texOffs(31, 33)
						.addBox(-4.5F, -0.5F, -2.5F, 9.0F, 13.0F, 5.0F)
						.texOffs(36, 20)
						.addBox(-5.0F, 0.0F, -3.0F, 10.0F, 10.0F, 3.0F),
				PartPose.ZERO
		);
		
		var rightArm = root.addOrReplaceChild(
				"right_arm",
				CubeListBuilder.create(),
				PartPose.ZERO
		);
		
		var armorRightArm = rightArm.addOrReplaceChild(
				"armor_right_arm",
				CubeListBuilder.create()
						.texOffs(22, 51)
						.addBox(-4.25F, -2.5F, -2.5F, 5.0F, 13.0F, 5.0F),
				PartPose.offset(1.0F, 0.0F, 0.0F)
		);
		
		armorRightArm.addOrReplaceChild(
				"armor_right_arm_extra",
				CubeListBuilder.create()
						.texOffs(57, 45)
						.addBox(-4.0F, -1.5F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.10F)),
				PartPose.offsetAndRotation(-1.5F, -2.0F, 0.0F, 0.0F, 0.0F, -0.4363F)
		);
		
		var leftArm = root.addOrReplaceChild(
				"left_arm",
				CubeListBuilder.create(),
				PartPose.ZERO
		);
		
		var armorLeftArm = leftArm.addOrReplaceChild(
				"armor_left_arm",
				CubeListBuilder.create()
						.texOffs(40, 0)
						.addBox(-1.5F, -2.5F, -2.5F, 5.0F, 13.0F, 5.0F),
				PartPose.ZERO
		);
		
		armorLeftArm.addOrReplaceChild(
				"armor_left_arm_extra",
				CubeListBuilder.create()
						.texOffs(62, 20)
						.addBox(-1.75F, -1.25F, -2.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.10F))
						.texOffs(54, 12)
						.addBox(-1.75F, -0.25F, -2.5F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.10F)),
				PartPose.offsetAndRotation(1.0F, -2.25F, -0.5F, 0.0F, 0.0F, 0.4363F)
		);
		
		var leftLeg = root.addOrReplaceChild(
				"left_leg",
				CubeListBuilder.create(),
				PartPose.ZERO
		);
		
		leftLeg.addOrReplaceChild(
				"left_leg_armor",
				CubeListBuilder.create()
						.texOffs(42, 51)
						.addBox(-2.5F, -0.15F, -2.5F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.15F)),
				PartPose.ZERO
		);
		
		leftLeg.addOrReplaceChild(
				"left_boot",
				CubeListBuilder.create()
						.texOffs(60, 0)
						.addBox(-2.5F, 9.15F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.25F)),
				PartPose.ZERO
		);
		
		var rightLeg = root.addOrReplaceChild(
				"right_leg",
				CubeListBuilder.create(),
				PartPose.ZERO
		);
		
		rightLeg.addOrReplaceChild(
				"right_leg_armor",
				CubeListBuilder.create()
						.texOffs(59, 28)
						.addBox(-2.5F, -0.15F, -2.5F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.149F)),
				PartPose.ZERO
		);
		
		rightLeg.addOrReplaceChild(
				"right_boot",
				CubeListBuilder.create()
						.texOffs(0, 61)
						.addBox(-2.5F, 9.15F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.249F)),
				PartPose.ZERO
		);
		
		return data;
		
	}
	
	
	@Override
	public void setupAnim(LivingEntity entity, float f, float g, float h, float i, float j) {
		if (!(entity instanceof ArmorStand stand)) {
			super.setupAnim(entity, f, g, h, i, j);
			return;
		}
		
		this.head.xRot = ((float) Math.PI / 180F) * stand.getHeadPose()
				.getX();
		this.head.yRot = ((float) Math.PI / 180F) * stand.getHeadPose()
				.getY();
		this.head.zRot = ((float) Math.PI / 180F) * stand.getHeadPose()
				.getZ();
		this.head.setPos(0.0F, 1.0F, 0.0F);
		this.body.xRot = ((float) Math.PI / 180F) * stand.getBodyPose()
				.getX();
		this.body.yRot = ((float) Math.PI / 180F) * stand.getBodyPose()
				.getY();
		this.body.zRot = ((float) Math.PI / 180F) * stand.getBodyPose()
				.getZ();
		this.leftArm.xRot = ((float) Math.PI / 180F) * stand.getLeftArmPose()
				.getX();
		this.leftArm.yRot = ((float) Math.PI / 180F) * stand.getLeftArmPose()
				.getY();
		this.leftArm.zRot = ((float) Math.PI / 180F) * stand.getLeftArmPose()
				.getZ();
		this.rightArm.xRot = ((float) Math.PI / 180F) * stand.getRightArmPose()
				.getX();
		this.rightArm.yRot = ((float) Math.PI / 180F) * stand.getRightArmPose()
				.getY();
		this.rightArm.zRot = ((float) Math.PI / 180F) * stand.getRightArmPose()
				.getZ();
		this.leftLeg.xRot = ((float) Math.PI / 180F) * stand.getLeftLegPose()
				.getX();
		this.leftLeg.yRot = ((float) Math.PI / 180F) * stand.getLeftLegPose()
				.getY();
		this.leftLeg.zRot = ((float) Math.PI / 180F) * stand.getLeftLegPose()
				.getZ();
		this.leftLeg.setPos(1.9F, 11.0F, 0.0F);
		this.rightLeg.xRot = ((float) Math.PI / 180F) * stand.getRightLegPose()
				.getX();
		this.rightLeg.yRot = ((float) Math.PI / 180F) * stand.getRightLegPose()
				.getY();
		this.rightLeg.zRot = ((float) Math.PI / 180F) * stand.getRightLegPose()
				.getZ();
		this.rightLeg.setPos(-1.9F, 11.0F, 0.0F);
		this.hat.copyFrom(head);
	}
	
	public static Tuple<Float, Float> computeFrontClothRotation(Player player, float delta) {
		// Vanilla cape values
		double x = Mth.lerp(delta / 2, player.xCloakO, player.xCloak) - Mth.lerp(delta / 2, player.xo, player.getX());
		double y = Mth.lerp(delta / 2, player.yCloakO, player.yCloak) - Mth.lerp(delta / 2, player.yo, player.getY());
		double z = Mth.lerp(delta / 2, player.zCloakO, player.zCloak) - Mth.lerp(delta / 2, player.zo, player.getZ());
		float yaw = Mth.rotLerp(delta, player.yBodyRotO, player.yBodyRot);
		double o = Mth.sin(yaw * (float) (Math.PI / 180.0));
		double p = -Mth.cos(yaw * (float) (Math.PI / 180.0));
		float q = (float) y * 10.0F;
		q = Mth.clamp(q, -2.0F, 24.0F);
		float r = (float) (x * o + z * p) * 100.0F;
		r = Mth.clamp(r, 0.0F, 150.0F);
		float capeZOffset = (float) (x * p - z * o) * 100.0F;
		capeZOffset = Mth.clamp(capeZOffset, -20.0F, 20.0F);
		if (r < 0.0F) {
			r = 0.0F;
		}
		float t = Mth.lerp(delta, player.oBob, player.bob);
		q += Mth.sin(Mth.lerp(delta, player.walkDistO, player.walkDist) * 6.0F) * 32.0F * t;
		
		if (player.isCrouching()) {
			q += 25.0F;
		}
		return new Tuple<>(-(6.0F + r / 2.0F + q), capeZOffset);
	}
	
	
	@Override
	public void renderToBuffer(PoseStack ms, VertexConsumer buffer, int light, int overlay, int color) {
		renderArmorPart(slot);
		super.renderToBuffer(ms, buffer, light, overlay, color);
	}
	
	private void renderArmorPart(EquipmentSlot slot) {
		setAllVisible(false);
		rightLeg.getChild("right_leg_armor").visible = false;
		leftLeg.getChild("left_leg_armor").visible = false;
		rightLeg.getChild("right_boot").visible = false;
		leftLeg.getChild("left_boot").visible = false;
		switch (slot) {
			case HEAD -> head.visible = true;
			case CHEST -> {
				body.visible = true;
				rightArm.visible = true;
				leftArm.visible = true;
			}
			case LEGS -> {
				rightLeg.visible = true;
				leftLeg.visible = true;
				rightLeg.getChild("right_leg_armor").visible = true;
				leftLeg.getChild("left_leg_armor").visible = true;
			}
			case FEET -> {
				rightLeg.visible = true;
				leftLeg.visible = true;
				rightLeg.getChild("right_boot").visible = true;
				leftLeg.getChild("left_boot").visible = true;
			}
			case MAINHAND, OFFHAND -> {
			}
		}
	}
	
}