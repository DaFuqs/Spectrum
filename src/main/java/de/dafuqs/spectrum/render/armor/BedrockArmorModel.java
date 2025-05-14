package de.dafuqs.spectrum.render.armor;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.*;
import net.minecraft.world.entity.player.*;

public class BedrockArmorModel extends HumanoidModel<LivingEntity> {
    final EquipmentSlot slot;

    public BedrockArmorModel(ModelPart root, EquipmentSlot slot) {
        super(root);
        this.slot = slot;
    }

    @SuppressWarnings("unused")
	public static MeshDefinition getModelData() {
		MeshDefinition data = new MeshDefinition();
        var root = data.getRoot();
		
		root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
		
		var head = root.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-4.5F, -8.625F, -4.5F, 9.0F, 9.0F, 9.0F, new CubeDeformation(.3f)), PartPose.ZERO);
		
		var head_plume = head.addOrReplaceChild("head_plume", CubeListBuilder.create()
				.texOffs(38, 61).addBox(0.0F, -0.0806F, 0.1517F, 0.0F, 11.0F, 7.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, -11.25F, 2.0F, 0.4363F, 0.0F, 0.0F));
		
		var visor = head.addOrReplaceChild("visor", CubeListBuilder.create()
						.texOffs(30, 11)
						.addBox(-5.0F, -3.0F, -6.8F, 10.0F, 5.0F, 7.0F, new CubeDeformation(0.125f)),
				PartPose.offsetAndRotation(0.0F, -6.0F, 1.5F, -0.1745F, 0.0F, 0.0F));
		
		var visor_frill_left = visor.addOrReplaceChild("visor_frill_left", CubeListBuilder.create()
				.texOffs(38, 14).addBox(1.25F, -4.0F, -4.5F, 0.0F, 8.0F, 9.0F, new CubeDeformation(0, 0.125f, 0.125f)), PartPose.offsetAndRotation(5.1F, -2.0F, 1.2F, 0.5796F, 0.2344F, 0.1939F));
		
		var visor_frill_right = visor.addOrReplaceChild("visor_frill_right", CubeListBuilder.create()
				.texOffs(56, 14).addBox(-1.25F, -4.0F, -4.5F, 0.0F, 8.0F, 9.0F, new CubeDeformation(0, 0.125f, 0.125f)), PartPose.offsetAndRotation(-5.1F, -2.0F, 1.2F, 0.5796F, -0.2344F, -0.1939F));
		
		var body = root.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(0, 31)
						.addBox(-4.5F, -1.0F, -2.5F, 9.0F, 13.0F, 5.0F, new CubeDeformation(0.25f))
						.texOffs(28, 41)
						.addBox(-4.5F, -1.0F, -3.75F, 9.0F, 12.0F, 3.0F, CubeDeformation.NONE),
				PartPose.offset(0.0F, 0.5F, 0.5F));
		
		var body_roll = body.addOrReplaceChild("body_roll", CubeListBuilder.create()
				.texOffs(28, 31)
				.addBox(-5.5F, -3.5F, -1.25F, 12.0F, 5.0F, 5.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-0.5F, 1.5F, 2.5F, -0.7854F, 0.0F, 0.0F));
		
		var body_collar = body.addOrReplaceChild("body_collar", CubeListBuilder.create()
				.texOffs(0, 83).addBox(-4.5F, -1.5F, -4.25F, 9.0F, 5.0F, 7.0F, new CubeDeformation(0.25f))
				.texOffs(0, 18).addBox(-5.5F, -1.5F, -5.25F, 11.0F, 5.0F, 8.0F, new CubeDeformation(0.25f)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.1745F, 0.0F, 0.0F));
		
		var right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(18, 56).addBox(-4.0F, -1.5F, -2.5F, 5.0F, 12.0F, 5.0F, new CubeDeformation(0.175f))
				.texOffs(56, 35).addBox(-4.5F, -1.5F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.175f)), PartPose.offset(-4.0F, 2.0F, 0.0F));
		
		var right_arm_pauldron_top = right_arm.addOrReplaceChild("right_arm_pauldron_top", CubeListBuilder.create()
				.texOffs(74, 22).addBox(1.0F, -1.0F, -2.5F, 2.0F, 5.0F, 7.0F, CubeDeformation.NONE)
				.texOffs(57, 3).addBox(-4.0F, 0.0F, -2.5F, 5.0F, 4.0F, 7.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-3.0F, -2.5F, -1.0F, 0.0F, 0.0F, -0.2618F));
		
		var left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create()
				.texOffs(47, 51).addBox(-1.0F, -2.0F, -2.5F, 5.0F, 12.0F, 5.0F, new CubeDeformation(0.175f))
				.texOffs(16, 73).addBox(0.5F, 0.0F, -3.0F, 4.0F, 5.0F, 6.0F, new CubeDeformation(0.175f)), PartPose.offset(4.0F, 2.5F, 0.0F));
		
		var left_arm_bauldron_top = left_arm.addOrReplaceChild("left_arm_bauldron_top", CubeListBuilder.create()
				.texOffs(74, 0).addBox(-0.5F, -5.0F, -3.0F, 5.0F, 1.0F, 6.0F, CubeDeformation.NONE)
				.texOffs(36, 0).addBox(-1.5F, -4.0F, -3.5F, 7.0F, 3.0F, 7.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.2618F));
		
		var left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, 12.0F, 0.0F));
		
		var left_leg_armor = left_leg.addOrReplaceChild("left_leg_armor", CubeListBuilder.create()
				.texOffs(67, 47).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.1f)), PartPose.ZERO);
		
		var left_boot = left_leg.addOrReplaceChild("left_boot", CubeListBuilder.create()
				.texOffs(82, 42).addBox(-2.5F, 9.25f, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.1f))
				.texOffs(32, 80).mirror().addBox(-2.25F, 5.0F, -1.25F, 5.0F, 6.0F, 4.0F, new CubeDeformation(0.125f)).mirror(false), PartPose.ZERO);
		
		var right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(2.0F, 12.0F, 0.0F));
		
		var right_leg_armor = right_leg.addOrReplaceChild("right_leg_armor", CubeListBuilder.create()
				.texOffs(62, 63).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.1f)), PartPose.ZERO);
		
		var right_boot = right_leg.addOrReplaceChild("right_boot", CubeListBuilder.create()
				.texOffs(32, 80).addBox(-2.75F, 5.0F, -1.25F, 5.0F, 6.0F, 4.0F, new CubeDeformation(0.1f))
				.texOffs(80, 34).addBox(-2.5F, 9.25f, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.125f)), PartPose.ZERO);
        return data;
    }

    @Override
	public void prepareMobModel(LivingEntity livingEntity, float f, float g, float h) {
		super.prepareMobModel(livingEntity, f, g, h);
    }

    @Override
	public void setupAnim(LivingEntity entity, float f, float g, float h, float i, float j) {
		if (!(entity instanceof ArmorStand stand)) {
			super.setupAnim(entity, f, g, h, i, j);
            return;
        }
		
		this.head.xRot = ((float) Math.PI / 180F) * stand.getHeadPose().getX();
		this.head.yRot = ((float) Math.PI / 180F) * stand.getHeadPose().getY();
		this.head.zRot = ((float) Math.PI / 180F) * stand.getHeadPose().getZ();
		this.head.setPos(0.0F, 1.0F, 0.0F);
		this.body.xRot = ((float) Math.PI / 180F) * stand.getBodyPose().getX();
		this.body.yRot = ((float) Math.PI / 180F) * stand.getBodyPose().getY();
		this.body.zRot = ((float) Math.PI / 180F) * stand.getBodyPose().getZ();
		this.leftArm.xRot = ((float) Math.PI / 180F) * stand.getLeftArmPose().getX();
		this.leftArm.yRot = ((float) Math.PI / 180F) * stand.getLeftArmPose().getY();
		this.leftArm.zRot = ((float) Math.PI / 180F) * stand.getLeftArmPose().getZ();
		this.rightArm.xRot = ((float) Math.PI / 180F) * stand.getRightArmPose().getX();
		this.rightArm.yRot = ((float) Math.PI / 180F) * stand.getRightArmPose().getY();
		this.rightArm.zRot = ((float) Math.PI / 180F) * stand.getRightArmPose().getZ();
		this.leftLeg.xRot = ((float) Math.PI / 180F) * stand.getLeftLegPose().getX();
		this.leftLeg.yRot = ((float) Math.PI / 180F) * stand.getLeftLegPose().getY();
		this.leftLeg.zRot = ((float) Math.PI / 180F) * stand.getLeftLegPose().getZ();
		this.leftLeg.setPos(1.9F, 11.0F, 0.0F);
		this.rightLeg.xRot = ((float) Math.PI / 180F) * stand.getRightLegPose().getX();
		this.rightLeg.yRot = ((float) Math.PI / 180F) * stand.getRightLegPose().getY();
		this.rightLeg.zRot = ((float) Math.PI / 180F) * stand.getRightLegPose().getZ();
		this.rightLeg.setPos(-1.9F, 11.0F, 0.0F);
		this.hat.copyFrom(head);
    }

    @Override
	public void renderToBuffer(PoseStack ms, VertexConsumer buffer, int light, int overlay, int color) {
        renderArmorPart(slot);
		super.renderToBuffer(ms, buffer, light, overlay, color);
    }
	
	public static Tuple<Float, Float> computeFrontClothRotation(Player player, float delta) {
        // Vanilla cape values
		double x = Mth.lerp(delta / 2, player.xCloakO, player.xCloak)
				- Mth.lerp(delta / 2, player.xo, player.getX());
		double y = Mth.lerp(delta / 2, player.yCloakO, player.yCloak)
				- Mth.lerp(delta / 2, player.yo, player.getY());
		double z = Mth.lerp(delta / 2, player.zCloakO, player.zCloak)
				- Mth.lerp(delta / 2, player.zo, player.getZ());
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
