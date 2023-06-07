package de.dafuqs.spectrum.entity.models;

import de.dafuqs.spectrum.entity.entity.KindlingEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.*;

import javax.swing.text.html.parser.Entity;

// Made with Blockbench 4.6.4
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class KindlingEntityModel extends EntityModel<KindlingEntity> {
	private final ModelPart torso;
	private final ModelPart head;
	private final ModelPart rightwing;
	private final ModelPart rightwingjoint;
	private final ModelPart leftwing;
	private final ModelPart leftwingjoint;
	private final ModelPart rightforeleg;
	private final ModelPart leftforeleg;
	private final ModelPart rightbackleg;
	private final ModelPart leftbackleg;
	private final ModelPart tail;
	private final ModelPart midtail;
	private final ModelPart fartail;
	public KindlingEntityModel(ModelPart root) {
		this.torso = root.getChild("torso");
		head = torso.getChild("head");
		rightwing = torso.getChild("rightwing");
		leftwing = torso.getChild("leftwing");
		rightwingjoint = rightwing.getChild("wingjoint");
		leftwingjoint = leftwing.getChild("wingjoint2");

		rightforeleg = torso.getChild("rightforeleg");
		leftforeleg = torso.getChild("leftforeleg");
		rightbackleg = torso.getChild("rightbackleg");
		leftbackleg = torso.getChild("leftbackleg");
		tail = torso.getChild("tail");
		midtail = tail.getChild("midtail");
		fartail = midtail.getChild("fartail");

	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData torso = modelPartData.addChild("torso", ModelPartBuilder.create().uv(0, 0).cuboid(-3.5F, -3.5F, -7.25F, 7.0F, 7.0F, 13.0F, new Dilation(0.0F))
		.uv(0, 21).cuboid(-4.5F, -5.0F, -8.75F, 9.0F, 9.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 15.5F, -0.25F));

		ModelPartData head = torso.addChild("head", ModelPartBuilder.create().uv(0, 48).cuboid(-3.5F, -7.5F, -6.75F, 7.0F, 8.0F, 7.0F, new Dilation(0.0F))
		.uv(30, 76).cuboid(-4.0F, -8.0F, -7.25F, 8.0F, 9.0F, 8.0F, new Dilation(0.0F))
		.uv(29, 21).cuboid(-1.5F, -2.51F, -8.75F, 3.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -3.0F, -6.5F));

		head.addChild("middlehorn_r1", ModelPartBuilder.create().uv(54, 0).cuboid(0.0F, -10.5F, -4.0F, 0.0F, 12.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -7.0F, -6.75F, 0.3927F, 0.0F, 0.0F));

		ModelPartData righthorns = head.addChild("righthorns", ModelPartBuilder.create(), ModelTransform.pivot(0.5F, 6.5F, 6.75F));

		righthorns.addChild("lowerrighthorn_r1", ModelPartBuilder.create().uv(46, 0).cuboid(-3.0F, 2.5F, -0.5F, 7.0F, 6.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(4.5F, -13.5F, -9.0F, 0.0F, -0.4363F, 0.0F));

		righthorns.addChild("upperrighthorn_r1", ModelPartBuilder.create().uv(64, 48).cuboid(-3.5F, -4.5F, -0.25F, 9.0F, 7.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(4.5F, -12.5F, -9.0F, -0.0203F, -0.4359F, -0.4318F));

		ModelPartData lefthorns = head.addChild("lefthorns", ModelPartBuilder.create(), ModelTransform.pivot(-0.5F, 6.5F, 6.75F));

		lefthorns.addChild("lowerrighthorn_r2", ModelPartBuilder.create().uv(0, 41).cuboid(-4.0F, 2.5F, -0.5F, 7.0F, 6.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-4.5F, -13.5F, -9.0F, 0.0F, 0.4363F, 0.0F));

		lefthorns.addChild("upperrighthorn_r2", ModelPartBuilder.create().uv(63, 0).cuboid(-5.5F, -4.5F, -0.25F, 9.0F, 7.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-4.5F, -12.5F, -9.0F, -0.0203F, 0.4359F, 0.4318F));

		ModelPartData rightwing = torso.addChild("rightwing", ModelPartBuilder.create().uv(93, 22).cuboid(0.0F, -6.0F, -1.5F, 0.0F, 6.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, -3.5F, -3.75F, 0.0F, 0.0F, 0.3054F));

		rightwing.addChild("wingjoint", ModelPartBuilder.create().uv(84, -9).cuboid(0.0F, -20.0F, -2.5F, 0.0F, 20.0F, 20.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -6.0F, -1.0F));

		ModelPartData leftwing = torso.addChild("leftwing", ModelPartBuilder.create().uv(93, 22).mirrored().cuboid(0.0F, -6.0F, -1.5F, 0.0F, 6.0F, 9.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-2.0F, -3.5F, -3.75F, 0.0F, 0.0F, -0.3054F));

		leftwing.addChild("wingjoint2", ModelPartBuilder.create().uv(84, -9).mirrored().cuboid(0.0F, -20.0F, -2.5F, 0.0F, 20.0F, 20.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, -6.0F, -1.0F));

		torso.addChild("rightforeleg", ModelPartBuilder.create().uv(67, 16).cuboid(0.0F, -2.5F, -3.5F, 3.0F, 6.0F, 5.0F, new Dilation(0.0F))
		.uv(69, 28).cuboid(3.0F, -2.5F, 1.5F, 0.0F, 7.0F, 5.0F, new Dilation(0.0F))
		.uv(25, 71).cuboid(0.0F, 3.5F, -1.5F, 3.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(2.5F, 1.0F, -5.0F));

		torso.addChild("leftforeleg", ModelPartBuilder.create().uv(44, 64).cuboid(-3.0F, -2.5F, -3.5F, 3.0F, 6.0F, 5.0F, new Dilation(0.0F))
		.uv(71, 8).cuboid(-3.0F, 3.5F, -1.5F, 3.0F, 4.0F, 3.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-3.0F, -2.5F, 1.5F, 0.0F, 7.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.5F, 1.0F, -5.0F));

		torso.addChild("rightbackleg", ModelPartBuilder.create().uv(23, 58).cuboid(-2.0F, -2.0F, -3.25F, 4.0F, 6.0F, 6.0F, new Dilation(0.0F))
		.uv(14, 70).cuboid(2.0F, -2.0F, 2.75F, 0.0F, 6.0F, 5.0F, new Dilation(0.0F))
		.uv(72, 69).cuboid(-2.0F, 4.0F, -0.25F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(3.5F, 1.5F, 4.5F));

		torso.addChild("leftbackleg", ModelPartBuilder.create().uv(54, 35).cuboid(-4.0F, -2.0F, -3.25F, 4.0F, 6.0F, 6.0F, new Dilation(0.0F))
		.uv(71, 56).cuboid(-3.0F, 4.0F, -0.25F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F))
		.uv(61, 69).cuboid(-4.0F, -2.0F, 2.75F, 0.0F, 6.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.5F, 1.5F, 4.5F));

		ModelPartData tail = torso.addChild("tail", ModelPartBuilder.create().uv(28, 0).cuboid(-2.5F, -2.48F, 0.0F, 5.0F, 5.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.0F, 5.75F));

		ModelPartData midtail = tail.addChild("midtail", ModelPartBuilder.create().uv(57, 57).cuboid(-1.5F, -1.97F, 0.0F, 3.0F, 4.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.5F, 7.0F));

		midtail.addChild("fartail", ModelPartBuilder.create().uv(39, 43).cuboid(0.0F, -6.5F, 3.0F, 0.0F, 8.0F, 12.0F, new Dilation(0.0F))
		.uv(0, 64).cuboid(-1.0F, -1.5F, 0.0F, 2.0F, 3.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.5F, 7.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
	@Override
	public void setAngles(KindlingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		torso.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}