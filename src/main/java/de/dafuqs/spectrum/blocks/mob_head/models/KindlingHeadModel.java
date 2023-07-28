package de.dafuqs.spectrum.blocks.mob_head.models;

import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

@Environment(EnvType.CLIENT)
public class KindlingHeadModel extends SpectrumHeadModel {
	
	public KindlingHeadModel(ModelPart root) {
		super(root);
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		
		ModelPartData head = modelData.getRoot().addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 48)
				.cuboid(-3.5F, -8.0F, -3.5F, 7.0F, 8.0F, 7.0F, new Dilation(0.0F))
				.uv(30, 76).cuboid(-4.0F, -8.5F, -4.0F, 8.0F, 9.0F, 8.0F, new Dilation(0.0F))
				.uv(29, 21).cuboid(-1.5F, -3.01F, -5.5F, 3.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.NONE);
		
		head.addChild("middlehorn_r1", ModelPartBuilder.create().uv(54, 0).cuboid(0.0F, -10.5F, -4.0F, 0.0F, 12.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -7.5F, -3.5F, 0.3927F, 0.0F, 0.0F));
		
		ModelPartData righthorns = head.addChild("righthorns", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		righthorns.addChild("lowerrighthorn_r1", ModelPartBuilder.create().uv(46, 0).cuboid(-3.0F, 2.5F, -0.5F, 7.0F, 6.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, -7.5F, 1.0F, 0.0F, -0.4363F, 0.0F));
		righthorns.addChild("upperrighthorn_r1", ModelPartBuilder.create().uv(64, 48).cuboid(-3.5F, -4.5F, -0.25F, 9.0F, 7.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, -6.5F, 1.0F, -0.0203F, -0.4359F, -0.4318F));
		
		ModelPartData lefthorns = head.addChild("lefthorns", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		lefthorns.addChild("lowerrighthorn_r2", ModelPartBuilder.create().uv(0, 41).cuboid(-4.0F, 2.5F, -0.5F, 7.0F, 6.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.0F, -7.5F, 1.0F, 0.0F, 0.4363F, 0.0F));
		lefthorns.addChild("upperrighthorn_r2", ModelPartBuilder.create().uv(63, 0).cuboid(-5.5F, -4.5F, -0.25F, 9.0F, 7.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-5.0F, -6.5F, 1.0F, -0.0203F, 0.4359F, 0.4318F));
		
		return TexturedModelData.of(modelData, 128, 128);
	}
	
	@Override
	public float getScale() {
		return 0.86F;
	}
	
}