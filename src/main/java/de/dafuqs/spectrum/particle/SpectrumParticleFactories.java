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

		registerColoredSporeBlossomParticle(SpectrumParticleTypes.BLACK_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.BLACK_SPORE_BLOSSOM_AIR, 0.1F, 0.1F, 0.1F);
		registerColoredSporeBlossomParticle(SpectrumParticleTypes.BLUE_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.BLUE_SPORE_BLOSSOM_AIR, 0.05F, 0.011F, 0.95F);
		registerColoredSporeBlossomParticle(SpectrumParticleTypes.BROWN_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.BROWN_SPORE_BLOSSOM_AIR, 0.31F, 0.16F, 0.05F);
		registerColoredSporeBlossomParticle(SpectrumParticleTypes.CYAN_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.CYAN_SPORE_BLOSSOM_AIR, 0.0F, 1.0F, 1.0F);
		registerColoredSporeBlossomParticle(SpectrumParticleTypes.GRAY_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.GRAY_SPORE_BLOSSOM_AIR, 0.3F, 0.3F, 0.3F);
		registerColoredSporeBlossomParticle(SpectrumParticleTypes.GREEN_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.GREEN_SPORE_BLOSSOM_AIR, 0.14F, 0.24F, 0.0F);
		registerColoredSporeBlossomParticle(SpectrumParticleTypes.LIGHT_BLUE_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.LIGHT_BLUE_SPORE_BLOSSOM_AIR, 0.0F, 0.75F, 0.95F);
		registerColoredSporeBlossomParticle(SpectrumParticleTypes.LIGHT_GRAY_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.LIGHT_GRAY_SPORE_BLOSSOM_AIR, 0.68F, 0.68F, 0.68F);
		registerColoredSporeBlossomParticle(SpectrumParticleTypes.LIME_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.LIME_SPORE_BLOSSOM_AIR, 0.0F, 0.86F, 0.0F);
		registerColoredSporeBlossomParticle(SpectrumParticleTypes.MAGENTA_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.MAGENTA_SPORE_BLOSSOM_AIR, 1.0F, 0.0F, 1.0F);
		registerColoredSporeBlossomParticle(SpectrumParticleTypes.ORANGE_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.ORANGE_SPORE_BLOSSOM_AIR, 0.93F, 0.39F, 0.0F);
		registerColoredSporeBlossomParticle(SpectrumParticleTypes.PINK_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.PINK_SPORE_BLOSSOM_AIR, 1.0F, 0.78F, 0.87F);
		registerColoredSporeBlossomParticle(SpectrumParticleTypes.PURPLE_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.PURPLE_SPORE_BLOSSOM_AIR, 0.43F, 0.0F, 0.68F);
		registerColoredSporeBlossomParticle(SpectrumParticleTypes.RED_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.RED_SPORE_BLOSSOM_AIR, 0.95F, 0.0F, 0.0F);
		registerColoredSporeBlossomParticle(SpectrumParticleTypes.WHITE_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.WHITE_SPORE_BLOSSOM_AIR, 0.97F, 0.97F, 0.97F);
		registerColoredSporeBlossomParticle(SpectrumParticleTypes.YELLOW_FALLING_SPORE_BLOSSOM, SpectrumParticleTypes.YELLOW_SPORE_BLOSSOM_AIR, 0.93F, 0.93F, 0.0F);
		
		registerColoredCraftingParticles(SpectrumParticleTypes.BLACK_CRAFTING, 0.1F, 0.1F, 0.1F);
		registerColoredCraftingParticles(SpectrumParticleTypes.BLUE_CRAFTING, 0.05F, 0.011F, 0.95F);
		registerColoredCraftingParticles(SpectrumParticleTypes.BROWN_CRAFTING, 0.31F, 0.16F, 0.05F);
		registerColoredCraftingParticles(SpectrumParticleTypes.CYAN_CRAFTING, 0.0F, 1.0F, 1.0F);
		registerColoredCraftingParticles(SpectrumParticleTypes.GRAY_CRAFTING, 0.3F, 0.3F, 0.3F);
		registerColoredCraftingParticles(SpectrumParticleTypes.GREEN_CRAFTING, 0.14F, 0.24F, 0.0F);
		registerColoredCraftingParticles(SpectrumParticleTypes.LIGHT_BLUE_CRAFTING, 0.0F, 0.75F, 0.95F);
		registerColoredCraftingParticles(SpectrumParticleTypes.LIGHT_GRAY_CRAFTING, 0.68F, 0.68F, 0.68F);
		registerColoredCraftingParticles(SpectrumParticleTypes.LIME_CRAFTING, 0.0F, 0.86F, 0.0F);
		registerColoredCraftingParticles(SpectrumParticleTypes.MAGENTA_CRAFTING, 1.0F, 0.0F, 1.0F);
		registerColoredCraftingParticles(SpectrumParticleTypes.ORANGE_CRAFTING, 0.93F, 0.39F, 0.0F);
		registerColoredCraftingParticles(SpectrumParticleTypes.PINK_CRAFTING, 1.0F, 0.78F, 0.87F);
		registerColoredCraftingParticles(SpectrumParticleTypes.PURPLE_CRAFTING, 0.43F, 0.0F, 0.68F);
		registerColoredCraftingParticles(SpectrumParticleTypes.RED_CRAFTING, 0.95F, 0.0F, 0.0F);
		registerColoredCraftingParticles(SpectrumParticleTypes.WHITE_CRAFTING, 0.97F, 0.97F, 0.97F);
		registerColoredCraftingParticles(SpectrumParticleTypes.YELLOW_CRAFTING, 0.93F, 0.93F, 0.0F);
		
		registerColoredRisingParticles(SpectrumParticleTypes.BLACK_RISING, 0.1F, 0.1F, 0.1F);
		registerColoredRisingParticles(SpectrumParticleTypes.BLUE_RISING, 0.05F, 0.011F, 0.95F);
		registerColoredRisingParticles(SpectrumParticleTypes.BROWN_RISING, 0.31F, 0.16F, 0.05F);
		registerColoredRisingParticles(SpectrumParticleTypes.CYAN_RISING, 0.0F, 1.0F, 1.0F);
		registerColoredRisingParticles(SpectrumParticleTypes.GRAY_RISING, 0.3F, 0.3F, 0.3F);
		registerColoredRisingParticles(SpectrumParticleTypes.GREEN_RISING, 0.14F, 0.24F, 0.0F);
		registerColoredRisingParticles(SpectrumParticleTypes.LIGHT_BLUE_RISING, 0.0F, 0.75F, 0.95F);
		registerColoredRisingParticles(SpectrumParticleTypes.LIGHT_GRAY_RISING, 0.68F, 0.68F, 0.68F);
		registerColoredRisingParticles(SpectrumParticleTypes.LIME_RISING, 0.0F, 0.86F, 0.0F);
		registerColoredRisingParticles(SpectrumParticleTypes.MAGENTA_RISING, 1.0F, 0.0F, 1.0F);
		registerColoredRisingParticles(SpectrumParticleTypes.ORANGE_RISING, 0.93F, 0.39F, 0.0F);
		registerColoredRisingParticles(SpectrumParticleTypes.PINK_RISING, 1.0F, 0.78F, 0.87F);
		registerColoredRisingParticles(SpectrumParticleTypes.PURPLE_RISING, 0.43F, 0.0F, 0.68F);
		registerColoredRisingParticles(SpectrumParticleTypes.RED_RISING, 0.95F, 0.0F, 0.0F);
		registerColoredRisingParticles(SpectrumParticleTypes.WHITE_RISING, 0.97F, 0.97F, 0.97F);
		registerColoredRisingParticles(SpectrumParticleTypes.YELLOW_RISING, 0.93F, 0.93F, 0.0F);
	}
	
	public static void registerColoredSporeBlossomParticle(DefaultParticleType fallingParticleType, DefaultParticleType airParticleType, float red, float green, float blue) {
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

	public static void registerColoredCraftingParticles(DefaultParticleType particleType,  float red, float green, float blue) {
		ParticleFactoryRegistry.getInstance().register(particleType, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			CraftingParticle.Factory factory = new CraftingParticle.Factory(provider);
			Particle particle = factory.createParticle(particleType, world, x, y, z, velocityX, velocityY, velocityZ);
			particle.setColor(red, green, blue);
			return particle;
		});
	}

	public static void registerColoredRisingParticles(DefaultParticleType particleType,  float red, float green, float blue) {
		ParticleFactoryRegistry.getInstance().register(particleType, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			RisingParticle.Factory factory = new RisingParticle.Factory(provider);
			Particle particle = factory.createParticle(particleType, world, x, y, z, velocityX, velocityY, velocityZ);
			particle.setColor(red, green, blue);
			return particle;
		});
	}

}