package de.dafuqs.spectrum.entity.models;

import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.entity.*;

@Environment(EnvType.CLIENT)
public class KindlingCoughEntityModel<T extends Entity> extends SinglePartEntityModel<T> {
	
	private final ModelPart root;
	
	public KindlingCoughEntityModel(ModelPart root) {
		this.root = root;
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		
		modelData.getRoot().addChild("main", ModelPartBuilder.create().uv(0, 0)
				.cuboid(-4.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F)
				.cuboid(0.0F, -4.0F, 0.0F, 2.0F, 2.0F, 2.0F)
				.cuboid(0.0F, 0.0F, -4.0F, 2.0F, 2.0F, 2.0F)
				.cuboid(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F)
				.cuboid(2.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F)
				.cuboid(0.0F, 2.0F, 0.0F, 2.0F, 2.0F, 2.0F)
				.cuboid(0.0F, 0.0F, 2.0F, 2.0F, 2.0F, 2.0F), ModelTransform.NONE);
		
		return TexturedModelData.of(modelData, 64, 32);
	}
	
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}
	
	public ModelPart getPart() {
		return this.root;
	}
}
