package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.entity.models.*;
import de.dafuqs.spectrum.registries.client.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.util.math.*;
import net.minecraft.util.*;

@Environment(EnvType.CLIENT)
public class GuardianTurretEntityRenderer extends MobEntityRenderer<GuardianTurretEntity, GuardianTurretEntityModel<GuardianTurretEntity>> {
	
	public static final Identifier TEXTURE = SpectrumCommon.locate("textures/entity/guardian_turret.png");
	
	public GuardianTurretEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new GuardianTurretEntityModel<>(context.getPart(SpectrumModelLayers.GUARDIAN_TURRET)), 0.0F);
	}
	
	@Override
	public Identifier getTexture(GuardianTurretEntity turretEntity) {
		return TEXTURE;
	}
	
	@Override
	protected void setupTransforms(GuardianTurretEntity turretEntity, MatrixStack matrixStack, float f, float g, float h) {
		super.setupTransforms(turretEntity, matrixStack, f, g + 180.0F, h);
		matrixStack.translate(0.0, 0.5, 0.0);
		matrixStack.multiply(turretEntity.getAttachedFace().getOpposite().getRotationQuaternion());
		matrixStack.translate(0.0, -0.5, 0.0);
	}
	
}