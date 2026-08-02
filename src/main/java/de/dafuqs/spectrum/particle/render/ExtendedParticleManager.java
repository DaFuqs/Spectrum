package de.dafuqs.spectrum.particle.render;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;


public interface ExtendedParticleManager {
	void renderAfterEntities(PoseStack matrices, MultiBufferSource vertexConsumers, Camera camera, float tickDelta);
}