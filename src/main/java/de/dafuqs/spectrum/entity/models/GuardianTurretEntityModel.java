package de.dafuqs.spectrum.entity.models;

import com.google.common.collect.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.util.math.*;

@Environment(EnvType.CLIENT)
public class GuardianTurretEntityModel<T extends GuardianTurretEntity> extends CompositeEntityModel<T> {
	
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart cover;
	
	public GuardianTurretEntityModel(ModelPart root) {
		super(RenderLayer::getEntityCutoutNoCullZOffset);
		this.body = root.getChild(EntityModelPartNames.BODY);
		this.head = body.getChild(EntityModelPartNames.HEAD);
		this.cover = body.getChild("cover");
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		
		ModelPartData body = modelData.getRoot().addChild(EntityModelPartNames.BODY, ModelPartBuilder.create()
				.uv(0, 0).cuboid(-8.0F, -8.0F, -8.0F, 16.0F, 8.0F, 16.0F, Dilation.NONE)
				.uv(0, 24).cuboid(-8.0F, -16.0F, -8.0F, 16.0F, 2.0F, 16.0F, Dilation.NONE), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		
		body.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(48, 0).cuboid(-5.0F, -14.0F, -5.0F, 10.0F, 6.0F, 10.0F, Dilation.NONE), ModelTransform.NONE);
		body.addChild("cover", ModelPartBuilder.create().uv(0, 42).cuboid(-7.0F, -14.0F, -7.0F, 14.0F, 6.0F, 14.0F, Dilation.NONE), ModelTransform.NONE);
		
		return TexturedModelData.of(modelData, 128, 128);
	}
	
	@Override
	public void setAngles(T turretEntity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		float k = animationProgress - (float) turretEntity.age;
		float l = (float) ((0.5F + turretEntity.getOpenProgress(k)) * Math.PI);
		float coverOffset = 0.0F;
		if (l > Math.PI) {
			coverOffset = MathHelper.sin(animationProgress * 0.1F) * 0.7F;
		}
		this.cover.setPivot(0.0F, 8.0F - MathHelper.sin(l) * 8.0F - coverOffset, 0.0F);
		
		this.head.pitch = headPitch * 0.017453292F;
		if (turretEntity.getOpenProgress(k) > 0.3F) {
			this.head.yaw = (turretEntity.headYaw - 180.0F - turretEntity.bodyYaw) * 0.017453292F;
		}
	}
	
	@Override
	public Iterable<ModelPart> getParts() {
		return ImmutableList.of(this.body);
	}
	
	public ModelPart getHead() {
		return this.head;
	}
	
}
