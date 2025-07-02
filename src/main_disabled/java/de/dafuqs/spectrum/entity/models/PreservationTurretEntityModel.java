package de.dafuqs.spectrum.entity.models;

import com.google.common.collect.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.fabricmc.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.*;

@Environment(EnvType.CLIENT)
public class PreservationTurretEntityModel<T extends PreservationTurretEntity> extends ListModel<T> {
	
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart cover;
	
	public PreservationTurretEntityModel(ModelPart root) {
		super(RenderType::entityCutoutNoCullZOffset);
		this.body = root.getChild(PartNames.BODY);
		this.head = body.getChild(PartNames.HEAD);
		this.cover = body.getChild("cover");
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		
		PartDefinition body = modelData.getRoot().addOrReplaceChild(PartNames.BODY, CubeListBuilder.create()
				.texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 8.0F, 16.0F, CubeDeformation.NONE)
				.texOffs(0, 24).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 2.0F, 16.0F, CubeDeformation.NONE), PartPose.offset(0.0F, 24.0F, 0.0F));
		
		body.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(48, 0).addBox(-5.0F, -14.0F, -5.0F, 10.0F, 6.0F, 10.0F, CubeDeformation.NONE), PartPose.ZERO);
		body.addOrReplaceChild("cover", CubeListBuilder.create().texOffs(0, 42).addBox(-7.0F, -14.0F, -7.0F, 14.0F, 6.0F, 14.0F, CubeDeformation.NONE), PartPose.ZERO);
		
		return LayerDefinition.create(modelData, 128, 128);
	}
	
	@Override
	public void setupAnim(T turretEntity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		float k = animationProgress - (float) turretEntity.tickCount;
		float l = (float) ((0.5F + turretEntity.getOpenProgress(k)) * Math.PI);
		float coverOffset = 0.0F;
		if (l > Math.PI) {
			coverOffset = Mth.sin(animationProgress * 0.1F) * 0.7F;
		}
		this.cover.setPos(0.0F, 8.0F - Mth.sin(l) * 8.0F - coverOffset, 0.0F);
		
		this.head.xRot = headPitch * 0.017453292F;
		if (turretEntity.getOpenProgress(k) > 0.3F) {
			this.head.yRot = (turretEntity.yHeadRot - 180.0F - turretEntity.yBodyRot) * 0.017453292F;
		}
	}
	
	@Override
	public Iterable<ModelPart> parts() {
		return ImmutableList.of(this.body);
	}
	
	public ModelPart getHead() {
		return this.head;
	}
	
}
