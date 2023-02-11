package de.dafuqs.spectrum.particle.render;

import net.fabricmc.api.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.*;

@Environment(EnvType.CLIENT)
public interface EarlyRenderingParticle {
    void renderAsEntity(final MatrixStack matrices, final VertexConsumerProvider vertexConsumers, final Camera camera, final float tickDelta);
}