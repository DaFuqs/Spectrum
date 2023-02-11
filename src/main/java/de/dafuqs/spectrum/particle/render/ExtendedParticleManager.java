package de.dafuqs.spectrum.particle.render;

import net.fabricmc.api.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.*;

@Environment(EnvType.CLIENT)
public interface ExtendedParticleManager {
    void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Camera camera, float tickDelta);
}