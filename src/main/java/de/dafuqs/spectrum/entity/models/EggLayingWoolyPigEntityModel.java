package de.dafuqs.spectrum.entity.models;

import de.dafuqs.spectrum.entity.entity.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.math.*;

@Environment(EnvType.CLIENT)
public class EggLayingWoolyPigEntityModel extends EntityModel<EggLayingWoolyPigEntity> {
	
	public static ModelPart torso;
	private final ModelPart head;
	private final ModelPart left_foreleg;
	private final ModelPart right_foreleg;
	private final ModelPart left_backleg;
	private final ModelPart right_backleg;
	
	public EggLayingWoolyPigEntityModel(ModelPart root) {
		super();
		this.torso = root.getChild("torso");
		this.head = torso.getChild("head");
		this.left_foreleg = torso.getChild("left_foreleg");
		this.right_foreleg = torso.getChild("right_foreleg");
		this.left_backleg = torso.getChild("left_backleg");
		this.right_backleg = torso.getChild("right_backleg");
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData torso = modelPartData.addChild("torso", ModelPartBuilder.create()
				.uv(42, 47).cuboid(-5.0F, -14.0F, -7.0F, 10.0F, 8.0F, 14.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		
		ModelPartData head = torso.addChild("head", ModelPartBuilder.create()
				.uv(56, 24).cuboid(-4.0F, -6.0F, -6.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
				.uv(0, 44).cuboid(-2.5F, -2.0F, -7.0F, 5.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -13.0F, -7.0F));
		
		head.addChild("ear_r1", ModelPartBuilder.create()
				.uv(0, 0).cuboid(7.0964F, -13.0939F, -10.5F, 3.0F, 7.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 10.0F, 6.0F, 0.0F, 0.0F, -0.3927F));
		
		head.addChild("ear_r2", ModelPartBuilder.create()
				.uv(0, 32).cuboid(-10.0964F, -13.0939F, -10.5F, 3.0F, 7.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 10.0F, 6.0F, 0.0F, 0.0F, 0.3927F));
		
		torso.addChild("left_foreleg", ModelPartBuilder.create()
				.uv(54, 69).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.0F, -6.0F, -4.0F));
		
		torso.addChild("right_foreleg", ModelPartBuilder.create()
				.uv(38, 69).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(3.0F, -6.0F, -4.0F));
		
		torso.addChild("left_backleg", ModelPartBuilder.create()
				.uv(0, 61).cuboid(-3.0F, -2.0F, -3.0F, 5.0F, 6.0F, 6.0F, new Dilation(0.0F))
				.uv(61, 40).cuboid(-3.0F, 4.0F, 0.0F, 3.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.0F, -8.0F, 5.0F));
		
		torso.addChild("right_backleg", ModelPartBuilder.create()
				.uv(39, 34).cuboid(-2.0F, -2.0F, -3.0F, 5.0F, 6.0F, 6.0F, new Dilation(0.0F))
				.uv(0, 12).cuboid(0.0F, 4.0F, 0.0F, 3.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(3.0F, -8.0F, 5.0F));
		
		torso.addChild("tail", ModelPartBuilder.create()
				.uv(22, 61).cuboid(-2.5F, -15.0F, 7.0F, 5.0F, 5.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, -1.0F));
		
		return TexturedModelData.of(modelData, 128, 128);
	}
	
	@Override
	public void setAngles(EggLayingWoolyPigEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.pitch = headPitch * 0.017453292F;
		this.head.yaw = netHeadYaw * 0.017453292F;
		this.right_backleg.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.left_backleg.pitch = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
		this.right_foreleg.pitch = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
		this.left_foreleg.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		if (child) {
			matrices.scale(0.6f, 0.6f, 0.6f);
			matrices.translate(0, 1, 0);
		}
		torso.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}