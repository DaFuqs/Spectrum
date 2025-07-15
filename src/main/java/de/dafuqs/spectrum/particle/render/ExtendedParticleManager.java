package de.dafuqs.spectrum.particle.render;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.neoforged.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public interface ExtendedParticleManager {
	void render(PoseStack matrices, MultiBufferSource vertexConsumers, Camera camera, float tickDelta);
}