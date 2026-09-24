package de.dafuqs.spectrum.entity.render;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.entity.models.*;
import de.dafuqs.spectrum.registries.client.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.animal.*;

public class KoiRenderer extends MobRenderer<Koi, KoiModel<Koi>> {
	
	public static final ResourceLocation TEXTURE = SpectrumCommon.locate("textures/entity/koi/koi.png");

    public KoiRenderer(EntityRendererProvider.Context context) {
        super(context, new KoiModel<>(context.bakeLayer(SpectrumModelLayerLocations.KOI)), 0.3F);
    }
	
	@Override
    public ResourceLocation getTextureLocation(Koi entity) {
        return TEXTURE;
    }

	@Override
    protected void setupRotations(Koi entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);
        float f = 4.3F * Mth.sin(0.6F * bob);
        poseStack.mulPose(Axis.YP.rotationDegrees(f));
        if (!entity.isInWater()) {
            poseStack.translate(0.1F, 0.1F, -0.1F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        }
    }
}
