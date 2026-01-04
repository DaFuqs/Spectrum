package de.dafuqs.spectrum.particle.render;

import com.google.common.collect.*;
import com.mojang.blaze3d.vertex.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;

import java.util.*;


public class EarlyRenderingParticleContainer {
	
	private static final int MAX_PARTICLES = 16384;
	private final static Map<ParticleRenderType, Queue<EarlyRenderingParticle>> particles = new Object2ReferenceOpenHashMap<>();
	
	public void add(final Particle particle) {
		if (particle instanceof EarlyRenderingParticle earlyRenderingParticle) {
			particles.computeIfAbsent(particle.getRenderType(), sheet -> EvictingQueue.create(MAX_PARTICLES)).add(earlyRenderingParticle);
		}
	}
	
	public void removeDead() {
		for (final Queue<EarlyRenderingParticle> particles : particles.values()) {
			particles.removeIf(particle -> !((Particle) particle).isAlive());
		}
	}
	
	public static void clear() {
		particles.clear();
	}
	
	public void render(final PoseStack matrices, final MultiBufferSource vertexConsumers, final Camera camera, final float tickDelta) {
		for (final Queue<EarlyRenderingParticle> particles : particles.values()) {
			for (final EarlyRenderingParticle particle : particles) {
				particle.renderAsEntity(matrices, vertexConsumers, camera, tickDelta);
			}
		}
	}
}