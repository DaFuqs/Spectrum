package de.dafuqs.spectrum.particle;

import de.dafuqs.spectrum.particle.client.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.particle.v1.*;
import net.minecraft.client.*;
import net.minecraft.client.particle.*;
import net.minecraft.particle.*;

@Environment(EnvType.CLIENT)
public class SpectrumParticleFactories {
	
	public static void register() {
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.PARTICLE_SPAWNER, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			ParticleSpawnerParticle particle = new ParticleSpawnerParticle(world, x, y, z, velocityX, velocityY, velocityZ);
			particle.setSprite(provider);
			particle.apply(parameters);
			return particle;
		});
		
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.PARTICLE_SPAWNER_ALWAYS_SHOW, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			ParticleSpawnerParticle particle = new ParticleSpawnerParticle(world, x, y, z, velocityX, velocityY, velocityZ);
			particle.setSprite(provider);
			particle.apply(parameters);
			return particle;
		});
		
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.ITEM_TRANSMISSION, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			TransmissionParticle particle = new TransmissionParticle(world, x, y, z, parameters.getDestination(), parameters.getArrivalInTicks());
			particle.setSprite(provider);
			return particle;
		});
		
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.EXPERIENCE_TRANSMISSION, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			TransmissionParticle particle = new TransmissionParticle(world, x, y, z, parameters.getDestination(), parameters.getArrivalInTicks());
			particle.setSprite(provider);
			return particle;
		});
		
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.WIRELESS_REDSTONE_TRANSMISSION, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			TransmissionParticle particle = new TransmissionParticle(world, x, y, z, parameters.getDestination(), parameters.getArrivalInTicks());
			particle.setSprite(provider);
			return particle;
		});
		
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.COLORED_TRANSMISSION, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
            ColoredTransmissionParticle particle = new ColoredTransmissionParticle(world, x, y, z, parameters.getDestination(), parameters.getArrivalInTicks(), parameters.getDyeColor());
            particle.setSprite(provider);
            return particle;
        });

        ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.BLOCK_POS_EVENT_TRANSMISSION, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
            TransmissionParticle particle = new TransmissionParticle(world, x, y, z, parameters.getDestination(), parameters.getArrivalInTicks());
            particle.setSprite(provider);
            return particle;
        });

        ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.PASTEL_TRANSMISSION, provider -> (pastelTransmissionParticleEffect, world, x, y, z, velocityX, velocityY, velocityZ) -> {
            PastelTransmissionParticle particle = new PastelTransmissionParticle(MinecraftClient.getInstance().getEntityRenderDispatcher(), MinecraftClient.getInstance().getBufferBuilders(),
                    world, x, y, z, pastelTransmissionParticleEffect.getNodePositions(), pastelTransmissionParticleEffect.getStack(), pastelTransmissionParticleEffect.getTravelTime());
            particle.setSprite(provider);
            return particle;
        });

        ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.DIVINITY, HardcoreParticle.Factory::new);

        ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.SHOOTING_STAR, LitParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.SHIMMERSTONE_SPARKLE, LitParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.SHIMMERSTONE_SPARKLE_SMALL, LitParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.SHIMMERSTONE_SPARKLE_TINY, LitParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.LIQUID_CRYSTAL_SPARKLE, LitParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.VOID_FOG, VoidFogParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.MUD_POP, BubblePopParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.BLUE_BUBBLE_POP, BubblePopParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.GREEN_BUBBLE_POP, BubblePopParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.SPIRIT_SALLOW, WindParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.DECAY_PLACE, CraftingParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.JADE_VINES, ZigZagParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.JADE_VINES_BLOOM, ZigZagParticle.Factory::new);

		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.MOONSTONE_STRIKE, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			MoonstoneStrikeParticle.Factory factory = new MoonstoneStrikeParticle.Factory();
			return factory.createParticle(SpectrumParticleTypes.MOONSTONE_STRIKE, world, x, y, z, velocityX, velocityY, velocityZ);
		});

		// Fluid Splash
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.MUD_SPLASH, WaterSplashParticle.SplashFactory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.LIQUID_CRYSTAL_SPLASH, WaterSplashParticle.SplashFactory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.MIDNIGHT_SOLUTION_SPLASH, WaterSplashParticle.SplashFactory::new);

		// Fluid Dripping
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.DRIPPING_MUD, SpectrumBlockLeakParticles.DrippingMudFactory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.DRIPPING_LIQUID_CRYSTAL, SpectrumBlockLeakParticles.DrippingLiquidCrystalFactory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.DRIPPING_MIDNIGHT_SOLUTION, SpectrumBlockLeakParticles.DrippingMidnightSolutionFactory::new);
		
		// Fluid Falling
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.FALLING_MUD, SpectrumBlockLeakParticles.FallingMudFactory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.FALLING_LIQUID_CRYSTAL, SpectrumBlockLeakParticles.FallingLiquidCrystalFactory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.FALLING_MIDNIGHT_SOLUTION, SpectrumBlockLeakParticles.FallingMidnightSolutionFactory::new);
		
		// Fluid Landing
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.LANDING_MUD, SpectrumBlockLeakParticles.LandingMudFactory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.LANDING_LIQUID_CRYSTAL, SpectrumBlockLeakParticles.LandingLiquidCrystalFactory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.LANDING_MIDNIGHT_SOLUTION, SpectrumBlockLeakParticles.LandingMidnightSolutionFactory::new);
		
		// Fluid Fishing
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.LAVA_FISHING, FishingParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.MUD_FISHING, FishingParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.LIQUID_CRYSTAL_FISHING, FishingParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.MIDNIGHT_SOLUTION_FISHING, FishingParticle.Factory::new);
		
		// Used for the colored spore blossoms
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
		
		registerColoredCraftingParticle(SpectrumParticleTypes.BLACK_CRAFTING, 0.1F, 0.1F, 0.1F);
		registerColoredCraftingParticle(SpectrumParticleTypes.BLUE_CRAFTING, 0.05F, 0.011F, 0.95F);
		registerColoredCraftingParticle(SpectrumParticleTypes.BROWN_CRAFTING, 0.31F, 0.16F, 0.05F);
		registerColoredCraftingParticle(SpectrumParticleTypes.CYAN_CRAFTING, 0.0F, 1.0F, 1.0F);
		registerColoredCraftingParticle(SpectrumParticleTypes.GRAY_CRAFTING, 0.3F, 0.3F, 0.3F);
		registerColoredCraftingParticle(SpectrumParticleTypes.GREEN_CRAFTING, 0.14F, 0.24F, 0.0F);
		registerColoredCraftingParticle(SpectrumParticleTypes.LIGHT_BLUE_CRAFTING, 0.0F, 0.75F, 0.95F);
		registerColoredCraftingParticle(SpectrumParticleTypes.LIGHT_GRAY_CRAFTING, 0.68F, 0.68F, 0.68F);
		registerColoredCraftingParticle(SpectrumParticleTypes.LIME_CRAFTING, 0.0F, 0.86F, 0.0F);
		registerColoredCraftingParticle(SpectrumParticleTypes.MAGENTA_CRAFTING, 1.0F, 0.0F, 1.0F);
		registerColoredCraftingParticle(SpectrumParticleTypes.ORANGE_CRAFTING, 0.93F, 0.39F, 0.0F);
		registerColoredCraftingParticle(SpectrumParticleTypes.PINK_CRAFTING, 1.0F, 0.78F, 0.87F);
		registerColoredCraftingParticle(SpectrumParticleTypes.PURPLE_CRAFTING, 0.43F, 0.0F, 0.68F);
		registerColoredCraftingParticle(SpectrumParticleTypes.RED_CRAFTING, 0.95F, 0.0F, 0.0F);
		registerColoredCraftingParticle(SpectrumParticleTypes.WHITE_CRAFTING, 0.97F, 0.97F, 0.97F);
		registerColoredCraftingParticle(SpectrumParticleTypes.YELLOW_CRAFTING, 0.93F, 0.93F, 0.0F);
		
		registerColoredRisingParticle(SpectrumParticleTypes.BLACK_FLUID_RISING, 0.1F, 0.1F, 0.1F);
		registerColoredRisingParticle(SpectrumParticleTypes.BLUE_FLUID_RISING, 0.05F, 0.011F, 0.95F);
		registerColoredRisingParticle(SpectrumParticleTypes.BROWN_FLUID_RISING, 0.31F, 0.16F, 0.05F);
		registerColoredRisingParticle(SpectrumParticleTypes.CYAN_FLUID_RISING, 0.0F, 1.0F, 1.0F);
		registerColoredRisingParticle(SpectrumParticleTypes.GRAY_FLUID_RISING, 0.3F, 0.3F, 0.3F);
		registerColoredRisingParticle(SpectrumParticleTypes.GREEN_FLUID_RISING, 0.14F, 0.24F, 0.0F);
		registerColoredRisingParticle(SpectrumParticleTypes.LIGHT_BLUE_FLUID_RISING, 0.0F, 0.75F, 0.95F);
		registerColoredRisingParticle(SpectrumParticleTypes.LIGHT_GRAY_FLUID_RISING, 0.68F, 0.68F, 0.68F);
		registerColoredRisingParticle(SpectrumParticleTypes.LIME_FLUID_RISING, 0.0F, 0.86F, 0.0F);
		registerColoredRisingParticle(SpectrumParticleTypes.MAGENTA_FLUID_RISING, 1.0F, 0.0F, 1.0F);
		registerColoredRisingParticle(SpectrumParticleTypes.ORANGE_FLUID_RISING, 0.93F, 0.39F, 0.0F);
		registerColoredRisingParticle(SpectrumParticleTypes.PINK_FLUID_RISING, 1.0F, 0.78F, 0.87F);
		registerColoredRisingParticle(SpectrumParticleTypes.PURPLE_FLUID_RISING, 0.43F, 0.0F, 0.68F);
		registerColoredRisingParticle(SpectrumParticleTypes.RED_FLUID_RISING, 0.95F, 0.0F, 0.0F);
		registerColoredRisingParticle(SpectrumParticleTypes.WHITE_FLUID_RISING, 0.97F, 0.97F, 0.97F);
		registerColoredRisingParticle(SpectrumParticleTypes.YELLOW_FLUID_RISING, 0.93F, 0.93F, 0.0F);
		
		registerColoredRisingParticle(SpectrumParticleTypes.BLACK_SPARKLE_RISING, 0.1F, 0.1F, 0.1F);
		registerColoredRisingParticle(SpectrumParticleTypes.BLUE_SPARKLE_RISING, 0.05F, 0.011F, 0.95F);
		registerColoredRisingParticle(SpectrumParticleTypes.BROWN_SPARKLE_RISING, 0.31F, 0.16F, 0.05F);
		registerColoredRisingParticle(SpectrumParticleTypes.CYAN_SPARKLE_RISING, 0.0F, 1.0F, 1.0F);
		registerColoredRisingParticle(SpectrumParticleTypes.GRAY_SPARKLE_RISING, 0.3F, 0.3F, 0.3F);
		registerColoredRisingParticle(SpectrumParticleTypes.GREEN_SPARKLE_RISING, 0.14F, 0.24F, 0.0F);
		registerColoredRisingParticle(SpectrumParticleTypes.LIGHT_BLUE_SPARKLE_RISING, 0.0F, 0.75F, 0.95F);
		registerColoredRisingParticle(SpectrumParticleTypes.LIGHT_GRAY_SPARKLE_RISING, 0.68F, 0.68F, 0.68F);
		registerColoredRisingParticle(SpectrumParticleTypes.LIME_SPARKLE_RISING, 0.0F, 0.86F, 0.0F);
		registerColoredRisingParticle(SpectrumParticleTypes.MAGENTA_SPARKLE_RISING, 1.0F, 0.0F, 1.0F);
		registerColoredRisingParticle(SpectrumParticleTypes.ORANGE_SPARKLE_RISING, 0.93F, 0.39F, 0.0F);
		registerColoredRisingParticle(SpectrumParticleTypes.PINK_SPARKLE_RISING, 1.0F, 0.78F, 0.87F);
		registerColoredRisingParticle(SpectrumParticleTypes.PURPLE_SPARKLE_RISING, 0.43F, 0.0F, 0.68F);
		registerColoredRisingParticle(SpectrumParticleTypes.RED_SPARKLE_RISING, 0.95F, 0.0F, 0.0F);
		registerColoredRisingParticle(SpectrumParticleTypes.WHITE_SPARKLE_RISING, 0.97F, 0.97F, 0.97F);
		registerColoredRisingParticle(SpectrumParticleTypes.YELLOW_SPARKLE_RISING, 0.93F, 0.93F, 0.0F);
		
		registerColoredExplosionParticle(SpectrumParticleTypes.BLACK_EXPLOSION, 0.1F, 0.1F, 0.1F);
		registerColoredExplosionParticle(SpectrumParticleTypes.BLUE_EXPLOSION, 0.05F, 0.011F, 0.95F);
		registerColoredExplosionParticle(SpectrumParticleTypes.BROWN_EXPLOSION, 0.31F, 0.16F, 0.05F);
		registerColoredExplosionParticle(SpectrumParticleTypes.CYAN_EXPLOSION, 0.0F, 1.0F, 1.0F);
		registerColoredExplosionParticle(SpectrumParticleTypes.GRAY_EXPLOSION, 0.3F, 0.3F, 0.3F);
		registerColoredExplosionParticle(SpectrumParticleTypes.GREEN_EXPLOSION, 0.14F, 0.24F, 0.0F);
		registerColoredExplosionParticle(SpectrumParticleTypes.LIGHT_BLUE_EXPLOSION, 0.0F, 0.75F, 0.95F);
		registerColoredExplosionParticle(SpectrumParticleTypes.LIGHT_GRAY_EXPLOSION, 0.68F, 0.68F, 0.68F);
		registerColoredExplosionParticle(SpectrumParticleTypes.LIME_EXPLOSION, 0.0F, 0.86F, 0.0F);
		registerColoredExplosionParticle(SpectrumParticleTypes.MAGENTA_EXPLOSION, 1.0F, 0.0F, 1.0F);
		registerColoredExplosionParticle(SpectrumParticleTypes.ORANGE_EXPLOSION, 0.93F, 0.39F, 0.0F);
		registerColoredExplosionParticle(SpectrumParticleTypes.PINK_EXPLOSION, 1.0F, 0.78F, 0.87F);
		registerColoredExplosionParticle(SpectrumParticleTypes.PURPLE_EXPLOSION, 0.43F, 0.0F, 0.68F);
		registerColoredExplosionParticle(SpectrumParticleTypes.RED_EXPLOSION, 0.95F, 0.0F, 0.0F);
		registerColoredExplosionParticle(SpectrumParticleTypes.WHITE_EXPLOSION, 0.97F, 0.97F, 0.97F);
		registerColoredExplosionParticle(SpectrumParticleTypes.YELLOW_EXPLOSION, 0.93F, 0.93F, 0.0F);
	}
	
	public static void registerColoredExplosionParticle(DefaultParticleType particleType, float red, float green, float blue) {
		ParticleFactoryRegistry.getInstance().register(particleType, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			ExplosionLargeParticle.Factory factory = new ExplosionLargeParticle.Factory(provider);
			Particle particle = factory.createParticle(particleType, world, x, y, z, velocityX, velocityY, velocityZ);
			particle.setColor(red, green, blue);
			return particle;
		});
	}
	
	public static void registerColoredCraftingParticle(DefaultParticleType particleType, float red, float green, float blue) {
		ParticleFactoryRegistry.getInstance().register(particleType, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			CraftingParticle.Factory factory = new CraftingParticle.Factory(provider);
			Particle particle = factory.createParticle(particleType, world, x, y, z, velocityX, velocityY, velocityZ);
			particle.setColor(red, green, blue);
			return particle;
		});
	}
	
	public static void registerColoredRisingParticle(DefaultParticleType particleType, float red, float green, float blue) {
		ParticleFactoryRegistry.getInstance().register(particleType, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			FixedVelocityParticle.Factory factory = new FixedVelocityParticle.Factory(provider);
			Particle particle = factory.createParticle(particleType, world, x, y, z, velocityX, velocityY, velocityZ);
			particle.setColor(red, green, blue);
			return particle;
		});
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