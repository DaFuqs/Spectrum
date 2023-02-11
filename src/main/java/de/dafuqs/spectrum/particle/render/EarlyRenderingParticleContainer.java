package de.dafuqs.spectrum.particle.render;

import com.google.common.collect.*;
import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.api.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class EarlyRenderingParticleContainer {
    private static final int MAX_PARTICLES = 16384;
    private final Map<ParticleTextureSheet, Queue<EarlyRenderingParticle>> particles = new Object2ReferenceOpenHashMap<>();

    public void add(final Particle particle) {
        if (particle instanceof EarlyRenderingParticle earlyRenderingParticle) {
            particles.computeIfAbsent(particle.getType(), sheet -> EvictingQueue.create(MAX_PARTICLES)).add(earlyRenderingParticle);
        }
    }

    public void removeDead() {
        for (final Queue<EarlyRenderingParticle> particles : particles.values()) {
            particles.removeIf(particle -> !((Particle) particle).isAlive());
        }
    }

    public void render(final MatrixStack matrices, final VertexConsumerProvider vertexConsumers, final Camera camera, final float tickDelta) {
        for (final Queue<EarlyRenderingParticle> particles : particles.values()) {
            for (final EarlyRenderingParticle particle : particles) {
                particle.renderAsEntity(matrices, vertexConsumers, camera, tickDelta);
            }
        }
    }
}