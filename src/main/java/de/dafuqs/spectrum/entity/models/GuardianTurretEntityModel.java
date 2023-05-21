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
	
	public GuardianTurretEntityModel(ModelPart root) {
		super(RenderLayer::getEntityCutoutNoCullZOffset);
		this.body = root.getChild(EntityModelPartNames.BODY);
		this.head = root.getChild(EntityModelPartNames.HEAD);
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create()
				.uv(0, 0).cuboid(-7.0F, -8.0F, -7.0F, 14.0F, 8.0F, 14.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		
		modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
				.uv(0, 37).cuboid(-5.0F, -13.0F, -5.0F, 10.0F, 5.0F, 10.0F, new Dilation(0.0F))
				.uv(0, 22).cuboid(-6.0F, -16.0F, -6.0F, 12.0F, 3.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		
		return TexturedModelData.of(modelData, 64, 64);
	}
	
	@Override
	public void setAngles(T turretEntity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		float k = animationProgress - (float) turretEntity.age;
		float l = (float) ((0.5F + turretEntity.getOpenProgress(k)) * Math.PI);
		float n = 0.0F;
		if (l > Math.PI) {
			n = MathHelper.sin(animationProgress * 0.1F) * 0.7F;
		}
		
		this.head.setPivot(0.0F, 16.0F + MathHelper.sin(l) * 8.0F + n, 0.0F);
		this.head.pitch = headPitch * 0.017453292F;
		if (turretEntity.getOpenProgress(k) > 0.3F) {
			this.head.yaw = (turretEntity.headYaw - 180.0F - turretEntity.bodyYaw) * 0.017453292F;
		}
	}
	
	@Override
	public Iterable<ModelPart> getParts() {
		return ImmutableList.of(this.body, this.head);
	}
	
	public ModelPart getHead() {
		return this.head;
	}
	
}
