package de.dafuqs.spectrum.particle;

import de.dafuqs.spectrum.particle.client.ParticleEmitterParticle;
import de.dafuqs.spectrum.particle.client.ShootingStarParticle;
import de.dafuqs.spectrum.particle.client.SparklestoneSparkleParticle;
import de.dafuqs.spectrum.particle.client.VoidFogParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class SpectrumParticleFactories {

	public static void register() {
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.SHOOTING_STAR, ShootingStarParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.SPARKLESTONE_SPARKLE, SparklestoneSparkleParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.PARTICLE_EMITTER, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			ParticleEmitterParticle particle = new ParticleEmitterParticle(world, x, y, z, velocityX, velocityY, velocityZ);
			particle.setSprite(provider);
			return particle;
		});
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.VOID_FOG, VoidFogParticle.Factory::new);
	}

}