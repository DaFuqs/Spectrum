package de.dafuqs.spectrum.render.armor;

import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.*;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

public class BedrockArmorModel extends BipedEntityModel<LivingEntity> {
    final EquipmentSlot slot;

    public BedrockArmorModel(ModelPart root, EquipmentSlot slot) {
        super(root);
        this.slot = slot;
    }

    public static ModelData getModelData() {
        ModelData data = new ModelData();
        var root = data.getRoot();

        root.addChild("hat", ModelPartBuilder.create(), ModelTransform.NONE);

        var head = root.addChild("head", ModelPartBuilder.create()
                .uv(0, 0).cuboid(-4.5F, -8.625F, -4.5F, 9.0F, 9.0F, 9.0F, new Dilation(.3f)), ModelTransform.NONE);

        var head_plume = head.addChild("head_plume", ModelPartBuilder.create()
                .uv(38, 61).cuboid(0.0F, -0.0806F, 0.1517F, 0.0F, 11.0F, 7.0F, Dilation.NONE), ModelTransform.of(0.0F, -11.25F, 2.0F, 0.4363F, 0.0F, 0.0F));

        var visor = head.addChild("visor", ModelPartBuilder.create()
                .uv(30, 11)
            .cuboid(-5.0F, -3.0F, -6.8F, 10.0F, 5.0F, 7.0F, new Dilation(0.125f)),
            ModelTransform.of(0.0F, -6.0F, 1.5F, -0.1745F, 0.0F, 0.0F));

        var visor_frill_left = visor.addChild("visor_frill_left", ModelPartBuilder.create()
                .uv(38, 14).cuboid(1.25F, -4.0F, -4.5F, 0.0F, 8.0F, 9.0F, new Dilation(0, 0.125f, 0.125f)), ModelTransform.of(5.1F, -2.0F, 1.2F, 0.5796F, 0.2344F, 0.1939F));

        var visor_frill_right = visor.addChild("visor_frill_right", ModelPartBuilder.create()
                .uv(56, 14).cuboid(-1.25F, -4.0F, -4.5F, 0.0F, 8.0F, 9.0F, new Dilation(0, 0.125f, 0.125f)), ModelTransform.of(-5.1F, -2.0F, 1.2F, 0.5796F, -0.2344F, -0.1939F));

        var body = root.addChild("body", ModelPartBuilder.create()
                .uv(0, 31)
                .cuboid(-4.5F, -1.0F, -2.5F, 9.0F, 13.0F, 5.0F, new Dilation(0.25f))
                .uv(28, 41)
                .cuboid(-4.5F, -1.0F, -3.75F, 9.0F, 12.0F, 3.0F, Dilation.NONE),
            ModelTransform.pivot(0.0F, 0.5F, 0.5F));

        var body_roll = body.addChild("body_roll", ModelPartBuilder.create()
                .uv(28, 31)
            .cuboid(-5.5F, -3.5F, -1.25F, 12.0F, 5.0F, 5.0F, Dilation.NONE), ModelTransform.of(-0.5F, 1.5F, 2.5F, -0.7854F, 0.0F, 0.0F));

        var body_collar = body.addChild("body_collar", ModelPartBuilder.create()
                .uv(0, 83).cuboid(-4.5F, -1.5F, -4.25F, 9.0F, 5.0F, 7.0F, new Dilation(0.25f))
                .uv(0, 18).cuboid(-5.5F, -1.5F, -5.25F, 11.0F, 5.0F, 8.0F, new Dilation(0.25f)), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        var right_arm = root.addChild("right_arm", ModelPartBuilder.create()
                .uv(18, 56).cuboid(-4.0F, -1.5F, -2.5F, 5.0F, 12.0F, 5.0F, new Dilation(0.175f))
                .uv(56, 35).cuboid(-4.5F, -1.5F, -3.0F, 6.0F, 6.0F, 6.0F, new Dilation(0.175f)), ModelTransform.pivot(-4.0F, 2.0F, 0.0F));

        var right_arm_pauldron_top = right_arm.addChild("right_arm_pauldron_top", ModelPartBuilder.create()
                .uv(74, 22).cuboid(1.0F, -1.0F, -2.5F, 2.0F, 5.0F, 7.0F, Dilation.NONE)
                .uv(57, 3).cuboid(-4.0F, 0.0F, -2.5F, 5.0F, 4.0F, 7.0F, Dilation.NONE), ModelTransform.of(-3.0F, -2.5F, -1.0F, 0.0F, 0.0F, -0.2618F));

        var left_arm = root.addChild("left_arm", ModelPartBuilder.create()
                .uv(47, 51).cuboid(-1.0F, -2.0F, -2.5F, 5.0F, 12.0F, 5.0F, new Dilation(0.175f))
                .uv(16, 73).cuboid(0.5F, 0.0F, -3.0F, 4.0F, 5.0F, 6.0F, new Dilation(0.175f)), ModelTransform.pivot(4.0F, 2.5F, 0.0F));

        var left_arm_bauldron_top = left_arm.addChild("left_arm_bauldron_top", ModelPartBuilder.create()
                .uv(74, 0).cuboid(-0.5F, -5.0F, -3.0F, 5.0F, 1.0F, 6.0F, Dilation.NONE)
                .uv(36, 0).cuboid(-1.5F, -4.0F, -3.5F, 7.0F, 3.0F, 7.0F, Dilation.NONE), ModelTransform.of(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        var left_leg = root.addChild("left_leg", ModelPartBuilder.create(), ModelTransform.pivot(2.0F, 12.0F, 0.0F));

        var left_leg_armor = left_leg.addChild("left_leg_armor", ModelPartBuilder.create()
                .uv(67, 47).cuboid(-2.5F, -0.5F, -2.5F, 5.0F, 10.0F, 5.0F, new Dilation(0.1f)), ModelTransform.NONE);

        var left_boot = left_leg.addChild("left_boot", ModelPartBuilder.create()
                .uv(82, 42).cuboid(-2.5F, 9.25f, -2.5F, 5.0F, 3.0F, 5.0F, new Dilation(0.1f))
                .uv(32, 80).mirrored().cuboid(-2.25F, 5.0F, -1.25F, 5.0F, 6.0F, 4.0F, new Dilation(0.125f)).mirrored(false), ModelTransform.NONE);

        var right_leg = root.addChild("right_leg", ModelPartBuilder.create(), ModelTransform.pivot(2.0F, 12.0F, 0.0F));

        var right_leg_armor = right_leg.addChild("right_leg_armor", ModelPartBuilder.create()
                .uv(62, 63).cuboid(-2.5F, -0.5F, -2.5F, 5.0F, 10.0F, 5.0F, new Dilation(0.1f)), ModelTransform.NONE);

        var right_boot = right_leg.addChild("right_boot", ModelPartBuilder.create()
                .uv(32, 80).cuboid(-2.75F, 5.0F, -1.25F, 5.0F, 6.0F, 4.0F, new Dilation(0.1f))
                .uv(80, 34).cuboid(-2.5F, 9.25f, -2.5F, 5.0F, 3.0F, 5.0F, new Dilation(0.125f)), ModelTransform.NONE);
        return data;
    }

    @Override
    public void animateModel(LivingEntity livingEntity, float f, float g, float h) {
        super.animateModel(livingEntity, f, g, h);
    }

    @Override
    public void setAngles(LivingEntity entity, float f, float g, float h, float i, float j) {
        if (!(entity instanceof ArmorStandEntity stand)) {
            super.setAngles(entity, f, g, h, i, j);
            return;
        }

        this.head.pitch = ((float) Math.PI / 180F) * stand.getHeadRotation().getPitch();
        this.head.yaw = ((float) Math.PI / 180F) * stand.getHeadRotation().getYaw();
        this.head.roll = ((float) Math.PI / 180F) * stand.getHeadRotation().getRoll();
        this.head.setPivot(0.0F, 1.0F, 0.0F);
        this.body.pitch = ((float) Math.PI / 180F) * stand.getBodyRotation().getPitch();
        this.body.yaw = ((float) Math.PI / 180F) * stand.getBodyRotation().getYaw();
        this.body.roll = ((float) Math.PI / 180F) * stand.getBodyRotation().getRoll();
        this.leftArm.pitch = ((float) Math.PI / 180F) * stand.getLeftArmRotation().getPitch();
        this.leftArm.yaw = ((float) Math.PI / 180F) * stand.getLeftArmRotation().getYaw();
        this.leftArm.roll = ((float) Math.PI / 180F) * stand.getLeftArmRotation().getRoll();
        this.rightArm.pitch = ((float) Math.PI / 180F) * stand.getRightArmRotation().getPitch();
        this.rightArm.yaw = ((float) Math.PI / 180F) * stand.getRightArmRotation().getYaw();
        this.rightArm.roll = ((float) Math.PI / 180F) * stand.getRightArmRotation().getRoll();
        this.leftLeg.pitch = ((float) Math.PI / 180F) * stand.getLeftLegRotation().getPitch();
        this.leftLeg.yaw = ((float) Math.PI / 180F) * stand.getLeftLegRotation().getYaw();
        this.leftLeg.roll = ((float) Math.PI / 180F) * stand.getLeftLegRotation().getRoll();
        this.leftLeg.setPivot(1.9F, 11.0F, 0.0F);
        this.rightLeg.pitch = ((float) Math.PI / 180F) * stand.getRightLegRotation().getPitch();
        this.rightLeg.yaw = ((float) Math.PI / 180F) * stand.getRightLegRotation().getYaw();
        this.rightLeg.roll = ((float) Math.PI / 180F) * stand.getRightLegRotation().getRoll();
        this.rightLeg.setPivot(-1.9F, 11.0F, 0.0F);
        this.hat.copyTransform(head);
    }

    @Override
    public void render(MatrixStack ms, VertexConsumer buffer, int light, int overlay, float r, float g, float b, float a) {
        renderArmorPart(slot);
        super.render(ms, buffer, light, overlay, r, g, b, a);
    }

    public static Pair<Float, Float> computeFrontClothRotation(PlayerEntity player, float delta) {
        // Vanilla cape values
        double x = MathHelper.lerp(delta / 2, player.prevCapeX, player.capeX)
                - MathHelper.lerp(delta / 2, player.prevX, player.getX());
        double y = MathHelper.lerp(delta / 2, player.prevCapeY, player.capeY)
                - MathHelper.lerp(delta / 2, player.prevY, player.getY());
        double z = MathHelper.lerp(delta / 2, player.prevCapeZ, player.capeZ)
                - MathHelper.lerp(delta / 2, player.prevZ, player.getZ());
        float yaw = MathHelper.lerpAngleDegrees(delta, player.prevBodyYaw, player.bodyYaw);
        double o = MathHelper.sin(yaw * (float) (Math.PI / 180.0));
        double p = -MathHelper.cos(yaw * (float) (Math.PI / 180.0));
        float q = (float) y * 10.0F;
        q = MathHelper.clamp(q, -2.0F, 24.0F);
        float r = (float) (x * o + z * p) * 100.0F;
        r = MathHelper.clamp(r, 0.0F, 150.0F);
        float capeZOffset = (float) (x * p - z * o) * 100.0F;
        capeZOffset = MathHelper.clamp(capeZOffset, -20.0F, 20.0F);
        if (r < 0.0F) {
            r = 0.0F;
        }
        float t = MathHelper.lerp(delta, player.prevStrideDistance, player.strideDistance);
        q += MathHelper.sin(MathHelper.lerp(delta, player.prevHorizontalSpeed, player.horizontalSpeed) * 6.0F) * 32.0F * t;

        if (player.isInSneakingPose()) {
            q += 25.0F;
        }
        return new Pair<>(-(6.0F + r / 2.0F + q), capeZOffset);
    }

    private void renderArmorPart(EquipmentSlot slot) {
        setVisible(false);
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
