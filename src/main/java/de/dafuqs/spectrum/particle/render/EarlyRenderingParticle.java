package de.dafuqs.spectrum.particle.render;

import com.mojang.blaze3d.vertex.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;

@Environment(EnvType.CLIENT)
public interface EarlyRenderingParticle {
	void renderAsEntity(final PoseStack matrices, final MultiBufferSource vertexConsumers, final Camera camera, final float tickDelta);
}