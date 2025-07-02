package de.dafuqs.spectrum.entity.render;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.*;

public class SeatEntityRenderer extends EntityRenderer<SeatEntity> {
	
	private static final ResourceLocation NOOP = SpectrumCommon.locate("noop");
	
	public SeatEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
	public void render(SeatEntity entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
    }

    @Override
	public ResourceLocation getTextureLocation(SeatEntity entity) {
        return NOOP;
    }
}
