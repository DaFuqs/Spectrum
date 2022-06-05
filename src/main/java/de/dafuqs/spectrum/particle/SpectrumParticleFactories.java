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
		
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.PARTICLE_SPAWNER_ALWAYS_SHOW, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
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
		
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.EXPERIENCE_TRANSFER, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			ExperienceTransferParticle particle = new ExperienceTransferParticle(world, parameters.getExperienceTransfer(), parameters.getExperienceTransfer().getArrivalInTicks());
			particle.setSprite(provider);
			return particle;
		});
		
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.WIRELESS_REDSTONE_TRANSMISSION, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			WirelessRedstoneTransmissionParticle particle = new WirelessRedstoneTransmissionParticle(world, parameters.getWirelessRedstoneTransmission(), parameters.getWirelessRedstoneTransmission().getArrivalInTicks());
			particle.setSprite(provider);
			return particle;
		});
		
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.TRANSPHERE, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			TransphereParticle particle = new TransphereParticle(world, parameters.getTransphere(), parameters.getTransphere().getArrivalInTicks());
			particle.setSprite(provider);
			return particle;
		});
		
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.BLOCK_POS_EVENT_TRANSFER, provider -> (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> {
			BlockPosEventTransferParticle particle = new BlockPosEventTransferParticle(world, parameters.getBlockPosEvent(), parameters.getBlockPosEvent().getArrivalInTicks());
			particle.setSprite(provider);
			return particle;
		});
		
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.SHOOTING_STAR, LitParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.SPARKLESTONE_SPARKLE, LitParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.SPARKLESTONE_SPARKLE_SMALL, LitParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.SPARKLESTONE_SPARKLE_TINY, LitParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.LIQUID_CRYSTAL_SPARKLE, LitParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.VOID_FOG, VoidFogParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.MUD_POP, BubblePopParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.BLUE_BUBBLE_POP, BubblePopParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.GREEN_BUBBLE_POP, BubblePopParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.SPIRIT_SALLOW, WindParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.DECAY_PLACE, CraftingParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.JADE_VINES, ZigZagParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SpectrumParticleTypes.JADE_VINES_BLOOM, ZigZagParticle.Factory::new);
		
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
		
		// Used for Pigment Pedestal crafting
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
		
		// Used in the fusion shrine fluid animation while crafting
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
		
		// Used in the item bowl
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