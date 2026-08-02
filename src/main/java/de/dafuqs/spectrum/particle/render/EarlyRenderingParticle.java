package de.dafuqs.spectrum.particle.render;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;


public interface EarlyRenderingParticle {
	void renderAfterEntities(final PoseStack matrices, final MultiBufferSource vertexConsumers, final Camera camera, final float tickDelta);
}