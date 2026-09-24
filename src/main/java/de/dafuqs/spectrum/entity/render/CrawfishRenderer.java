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
import net.minecraft.world.entity.monster.*;

public class CrawfishRenderer extends MobRenderer<Crawfish, CrawfishModel<Crawfish>> {
	
	public static final ResourceLocation TEXTURE = SpectrumCommon.locate("textures/entity/crawfish/crawfish.png");

    public CrawfishRenderer(EntityRendererProvider.Context context) {
        super(context, new CrawfishModel<>(context.bakeLayer(SpectrumModelLayerLocations.CRAWFISH)), 0.3F);
    }
	
	@Override
    public ResourceLocation getTextureLocation(Crawfish entity) {
        return TEXTURE;
    }

	@Override
    protected void setupRotations(Crawfish entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);
        float f = 4.3F * Mth.sin(0.6F * bob);
        poseStack.mulPose(Axis.YP.rotationDegrees(f));
    }
	
	/*@Override
	protected void scale(Crawfish crawfish, PoseStack poseStack, float partialTickTime) {
		int collectedXP = crawfish.getCollectedXP();
		float scale = 1.0F + 0.2F * (float)collectedXP;
		poseStack.scale(scale, scale, scale);
	}*/
	
}
