package de.dafuqs.spectrum.entity.render;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.entity.models.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.*;

@Environment(EnvType.CLIENT)
public class PreservationTurretEntityRenderer extends MobRenderer<PreservationTurretEntity, PreservationTurretEntityModel<PreservationTurretEntity>> {
	
	public static final ResourceLocation TEXTURE = SpectrumCommon.locate("textures/entity/preservation_turret/preservation_turret.png");
	
	public PreservationTurretEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new PreservationTurretEntityModel<>(context.bakeLayer(SpectrumModelLayers.PRESERVATION_TURRET)), 0.0F);
	}
	
	@Override
	public ResourceLocation getTextureLocation(PreservationTurretEntity turretEntity) {
		return TEXTURE;
	}
	
	@Override
	protected void setupRotations(PreservationTurretEntity turretEntity, PoseStack matrices, float animationProgress, float bodyYaw, float tickDelta, float scale) {
		super.setupRotations(turretEntity, matrices, animationProgress, bodyYaw + 180.0F, tickDelta, scale);
		matrices.translate(0.0, 0.5, 0.0);
		matrices.mulPose(turretEntity.getAttachedFace().getOpposite().getRotation());
		matrices.translate(0.0, -0.5, 0.0);
	}
	
}