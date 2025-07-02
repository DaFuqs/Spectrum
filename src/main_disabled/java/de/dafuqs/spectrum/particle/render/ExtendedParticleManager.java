package de.dafuqs.spectrum.particle.render;

import com.mojang.blaze3d.vertex.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;

@Environment(EnvType.CLIENT)
public interface ExtendedParticleManager {
	void render(PoseStack matrices, MultiBufferSource vertexConsumers, Camera camera, float tickDelta);
}