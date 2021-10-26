package de.dafuqs.spectrum.particle;

import de.dafuqs.spectrum.particle.client.*;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.BlockLeakParticle;
import net.minecraft.client.particle.BubblePopParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.WaterSuspendParticle;
import net.minecraft.particle.DefaultParticleType;

public class SpectrumParticleFactories {

	public static void register() {
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.PARTICLE_SPAWNER, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			ParticleSpawnerParticle particle = new ParticleSpawnerParticle(world, x, y, z, velocityX, velocityY, velocityZ);
			particle.setSprite(provider);
			particle.apply(parameters);
			return particle;
		});

		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.ITEM_TRANSFER, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			ItemTransferParticle particle = new ItemTransferParticle(world, parameters.getItemTransfer(), parameters.getItemTransfer().getArrivalInTicks());
			particle.setSprite(provider);
			return particle;
		});

		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.WIRELESS_REDSTONE_TRANSMISSION, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			WirelessRedstoneTransmissionParticle particle = new WirelessRedstoneTransmissionParticle(world, parameters.getWirelessRedstoneTransmission(), parameters.getWirelessRedstoneTransmission().getArrivalInTicks());
			particle.setSprite(provider);
			return particle;
		});

		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.SHOOTING_STAR, ShootingStarParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.SPARKLESTONE_SPARKLE, SparklestoneSparkleParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.SPARKLESTONE_SPARKLE_SMALL, SparklestoneSparkleParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.SPARKLESTONE_SPARKLE_TINY, SparklestoneSparkleParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.VOID_FOG, VoidFogParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.MUD_POP, BubblePopParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.LIQUID_CRYSTAL_SPARKLE, SparklestoneSparkleParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.BLUE_BUBBLE_POP, BubblePopParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.SPIRIT_SALLOW, WindParticle.Factory::new);

		registerColoredSporeBlossomParticles(SpectrumParticleTypes.BLACK_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.BLACK_SPORE_BLOSSOM_AIR, 0.1F, 0.1F, 0.1F);
		registerColoredSporeBlossomParticles(SpectrumParticleTypes.BLUE_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.BLUE_SPORE_BLOSSOM_AIR, 0.05F, 0.011F, 0.95F);
		registerColoredSporeBlossomParticles(SpectrumParticleTypes.BROWN_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.BROWN_SPORE_BLOSSOM_AIR, 0.31F, 0.16F, 0.05F);
		registerColoredSporeBlossomParticles(SpectrumParticleTypes.CYAN_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.CYAN_SPORE_BLOSSOM_AIR, 0.0F, 1.0F, 1.0F);
		registerColoredSporeBlossomParticles(SpectrumParticleTypes.GRAY_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.GRAY_SPORE_BLOSSOM_AIR, 0.3F, 0.3F, 0.3F);
		registerColoredSporeBlossomParticles(SpectrumParticleTypes.GREEN_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.GREEN_SPORE_BLOSSOM_AIR, 0.14F, 0.24F, 0.0F);
		registerColoredSporeBlossomParticles(SpectrumParticleTypes.LIGHT_BLUE_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.LIGHT_BLUE_SPORE_BLOSSOM_AIR, 0.0F, 0.75F, 0.95F);
		registerColoredSporeBlossomParticles(SpectrumParticleTypes.LIGHT_GRAY_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.LIGHT_GRAY_SPORE_BLOSSOM_AIR, 0.68F, 0.68F, 0.68F);
		registerColoredSporeBlossomParticles(SpectrumParticleTypes.LIME_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.LIME_SPORE_BLOSSOM_AIR, 0.0F, 0.86F, 0.0F);
		registerColoredSporeBlossomParticles(SpectrumParticleTypes.MAGENTA_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.MAGENTA_SPORE_BLOSSOM_AIR, 1.0F, 0.0F, 1.0F);
		registerColoredSporeBlossomParticles(SpectrumParticleTypes.ORANGE_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.ORANGE_SPORE_BLOSSOM_AIR, 0.93F, 0.39F, 0.0F);
		registerColoredSporeBlossomParticles(SpectrumParticleTypes.PINK_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.PINK_SPORE_BLOSSOM_AIR, 1.0F, 0.78F, 0.87F);
		registerColoredSporeBlossomParticles(SpectrumParticleTypes.PURPLE_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.PURPLE_SPORE_BLOSSOM_AIR, 0.43F, 0.0F, 0.68F);
		registerColoredSporeBlossomParticles(SpectrumParticleTypes.RED_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.RED_SPORE_BLOSSOM_AIR, 0.95F, 0.0F, 0.0F);
		registerColoredSporeBlossomParticles(SpectrumParticleTypes.WHITE_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.WHITE_SPORE_BLOSSOM_AIR, 0.97F, 0.97F, 0.97F);
		registerColoredSporeBlossomParticles(SpectrumParticleTypes.YELLOW_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.YELLOW_SPORE_BLOSSOM_AIR, 0.93F, 0.93F, 0.0F);
	}

	public static void registerColoredSporeBlossomParticles(DefaultParticleType fallingParticleType, DefaultParticleType airParticleType, float red, float green, float blue) {
		ParticleFactoryRegistry.getInstance().register(fallingParticleType, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			BlockLeakParticle.FallingSporeBlossomFactory factory = new BlockLeakParticle.FallingSporeBlossomFactory(provider);
			Particle particle = factory.createParticle(fallingParticleType, world, x, y, z, velocityX, velocityY, velocityZ);
			particle.setColor(red, green, blue);
			return particle;
		});

		ParticleFactoryRegistry.getInstance().register(airParticleType, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			WaterSuspendParticle.SporeBlossomAirFactory factory = new WaterSuspendParticle.SporeBlossomAirFactory(provider);
			Particle particle = factory.createParticle(airParticleType, world, x, y, z, velocityX, velocityY, velocityZ);
			particle.setColor(red, green, blue);
			return particle;
		});
	}

}