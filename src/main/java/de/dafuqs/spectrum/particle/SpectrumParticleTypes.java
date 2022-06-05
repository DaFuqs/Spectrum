package de.dafuqs.spectrum.particle;

import com.mojang.serialization.Codec;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.particle.effect.*;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class SpectrumParticleTypes {
	
	public static ParticleType<ParticleSpawnerParticleEffect> PARTICLE_SPAWNER;
	public static ParticleType<ParticleSpawnerParticleEffectAlwaysShow> PARTICLE_SPAWNER_ALWAYS_SHOW;
	public static ParticleType<ItemTransferParticleEffect> ITEM_TRANSFER;
	public static ParticleType<ExperienceTransferParticleEffect> EXPERIENCE_TRANSFER;
	public static ParticleType<WirelessRedstoneTransmissionParticleEffect> WIRELESS_REDSTONE_TRANSMISSION;
	public static ParticleType<TransphereParticleEffect> TRANSPHERE;
	public static ParticleType<BlockPosEventTransferParticleEffect> BLOCK_POS_EVENT_TRANSFER;
	
	public static DefaultParticleType SHOOTING_STAR; // Dummy entry to get the sprite registered
	public static DefaultParticleType SPARKLESTONE_SPARKLE;
	public static DefaultParticleType SPARKLESTONE_SPARKLE_SMALL;
	public static DefaultParticleType SPARKLESTONE_SPARKLE_TINY;
	public static DefaultParticleType VOID_FOG;
	public static DefaultParticleType MUD_POP;
	public static DefaultParticleType LIQUID_CRYSTAL_SPARKLE;
	public static DefaultParticleType BLUE_BUBBLE_POP;
	public static DefaultParticleType GREEN_BUBBLE_POP;
	public static DefaultParticleType SPIRIT_SALLOW;
	public static DefaultParticleType DECAY_PLACE;
	public static DefaultParticleType JADE_VINES;
	public static DefaultParticleType JADE_VINES_BLOOM;
	
	public static DefaultParticleType BLACK_FALLING_SPORE_BLOSSOM;
	public static DefaultParticleType BLUE_FALLING_SPORE_BLOSSOM;
	public static DefaultParticleType BROWN_FALLING_SPORE_BLOSSOM;
	public static DefaultParticleType CYAN_FALLING_SPORE_BLOSSOM;
	public static DefaultParticleType GRAY_FALLING_SPORE_BLOSSOM;
	public static DefaultParticleType GREEN_FALLING_SPORE_BLOSSOM;
	public static DefaultParticleType LIGHT_BLUE_FALLING_SPORE_BLOSSOM;
	public static DefaultParticleType LIGHT_GRAY_FALLING_SPORE_BLOSSOM;
	public static DefaultParticleType LIME_FALLING_SPORE_BLOSSOM;
	public static DefaultParticleType MAGENTA_FALLING_SPORE_BLOSSOM;
	public static DefaultParticleType ORANGE_FALLING_SPORE_BLOSSOM;
	public static DefaultParticleType PINK_FALLING_SPORE_BLOSSOM;
	public static DefaultParticleType PURPLE_FALLING_SPORE_BLOSSOM;
	public static DefaultParticleType RED_FALLING_SPORE_BLOSSOM;
	public static DefaultParticleType WHITE_FALLING_SPORE_BLOSSOM;
	public static DefaultParticleType YELLOW_FALLING_SPORE_BLOSSOM;
	
	public static DefaultParticleType BLACK_SPORE_BLOSSOM_AIR;
	public static DefaultParticleType BLUE_SPORE_BLOSSOM_AIR;
	public static DefaultParticleType BROWN_SPORE_BLOSSOM_AIR;
	public static DefaultParticleType CYAN_SPORE_BLOSSOM_AIR;
	public static DefaultParticleType GRAY_SPORE_BLOSSOM_AIR;
	public static DefaultParticleType GREEN_SPORE_BLOSSOM_AIR;
	public static DefaultParticleType LIGHT_BLUE_SPORE_BLOSSOM_AIR;
	public static DefaultParticleType LIGHT_GRAY_SPORE_BLOSSOM_AIR;
	public static DefaultParticleType LIME_SPORE_BLOSSOM_AIR;
	public static DefaultParticleType MAGENTA_SPORE_BLOSSOM_AIR;
	public static DefaultParticleType ORANGE_SPORE_BLOSSOM_AIR;
	public static DefaultParticleType PINK_SPORE_BLOSSOM_AIR;
	public static DefaultParticleType PURPLE_SPORE_BLOSSOM_AIR;
	public static DefaultParticleType RED_SPORE_BLOSSOM_AIR;
	public static DefaultParticleType WHITE_SPORE_BLOSSOM_AIR;
	public static DefaultParticleType YELLOW_SPORE_BLOSSOM_AIR;
	
	public static DefaultParticleType BLACK_CRAFTING;
	public static DefaultParticleType BLUE_CRAFTING;
	public static DefaultParticleType BROWN_CRAFTING;
	public static DefaultParticleType CYAN_CRAFTING;
	public static DefaultParticleType GRAY_CRAFTING;
	public static DefaultParticleType GREEN_CRAFTING;
	public static DefaultParticleType LIGHT_BLUE_CRAFTING;
	public static DefaultParticleType LIGHT_GRAY_CRAFTING;
	public static DefaultParticleType LIME_CRAFTING;
	public static DefaultParticleType MAGENTA_CRAFTING;
	public static DefaultParticleType ORANGE_CRAFTING;
	public static DefaultParticleType PINK_CRAFTING;
	public static DefaultParticleType PURPLE_CRAFTING;
	public static DefaultParticleType RED_CRAFTING;
	public static DefaultParticleType WHITE_CRAFTING;
	public static DefaultParticleType YELLOW_CRAFTING;
	
	public static DefaultParticleType BLACK_FLUID_RISING;
	public static DefaultParticleType BLUE_FLUID_RISING;
	public static DefaultParticleType BROWN_FLUID_RISING;
	public static DefaultParticleType CYAN_FLUID_RISING;
	public static DefaultParticleType GRAY_FLUID_RISING;
	public static DefaultParticleType GREEN_FLUID_RISING;
	public static DefaultParticleType LIGHT_BLUE_FLUID_RISING;
	public static DefaultParticleType LIGHT_GRAY_FLUID_RISING;
	public static DefaultParticleType LIME_FLUID_RISING;
	public static DefaultParticleType MAGENTA_FLUID_RISING;
	public static DefaultParticleType ORANGE_FLUID_RISING;
	public static DefaultParticleType PINK_FLUID_RISING;
	public static DefaultParticleType PURPLE_FLUID_RISING;
	public static DefaultParticleType RED_FLUID_RISING;
	public static DefaultParticleType WHITE_FLUID_RISING;
	public static DefaultParticleType YELLOW_FLUID_RISING;
	
	public static DefaultParticleType BLACK_SPARKLE_RISING;
	public static DefaultParticleType BLUE_SPARKLE_RISING;
	public static DefaultParticleType BROWN_SPARKLE_RISING;
	public static DefaultParticleType CYAN_SPARKLE_RISING;
	public static DefaultParticleType GRAY_SPARKLE_RISING;
	public static DefaultParticleType GREEN_SPARKLE_RISING;
	public static DefaultParticleType LIGHT_BLUE_SPARKLE_RISING;
	public static DefaultParticleType LIGHT_GRAY_SPARKLE_RISING;
	public static DefaultParticleType LIME_SPARKLE_RISING;
	public static DefaultParticleType MAGENTA_SPARKLE_RISING;
	public static DefaultParticleType ORANGE_SPARKLE_RISING;
	public static DefaultParticleType PINK_SPARKLE_RISING;
	public static DefaultParticleType PURPLE_SPARKLE_RISING;
	public static DefaultParticleType RED_SPARKLE_RISING;
	public static DefaultParticleType WHITE_SPARKLE_RISING;
	public static DefaultParticleType YELLOW_SPARKLE_RISING;
	
	// Simple particles
	public static DefaultParticleType register(String name, boolean alwaysShow) {
		return Registry.register(Registry.PARTICLE_TYPE, new Identifier(SpectrumCommon.MOD_ID, name), FabricParticleTypes.simple(alwaysShow));
	}
	
	// complex particles
	private static <T extends ParticleEffect> ParticleType<T> register(String name, ParticleEffect.Factory<T> factory, final Function<ParticleType<T>, Codec<T>> function, boolean alwaysShow) {
		return Registry.register(Registry.PARTICLE_TYPE, new Identifier(SpectrumCommon.MOD_ID, name), new ParticleType<T>(alwaysShow, factory) {
			public Codec<T> getCodec() {
				return function.apply(this);
			}
		});
	}
	
	public static void register() {
		PARTICLE_SPAWNER = register("particle_spawner", ParticleSpawnerParticleEffect.FACTORY, (particleType) -> ParticleSpawnerParticleEffect.CODEC, false);
		PARTICLE_SPAWNER_ALWAYS_SHOW = register("particle_spawner_always_show", ParticleSpawnerParticleEffectAlwaysShow.FACTORY, (particleType) -> ParticleSpawnerParticleEffectAlwaysShow.CODEC, true);
		ITEM_TRANSFER = register("item_transfer", ItemTransferParticleEffect.FACTORY, (particleType) -> ItemTransferParticleEffect.CODEC, false);
		EXPERIENCE_TRANSFER = register("experience_transfer", ExperienceTransferParticleEffect.FACTORY, (particleType) -> ExperienceTransferParticleEffect.CODEC, false);
		WIRELESS_REDSTONE_TRANSMISSION = register("wireless_redstone_transmission", WirelessRedstoneTransmissionParticleEffect.FACTORY, (particleType) -> WirelessRedstoneTransmissionParticleEffect.CODEC, false);
		TRANSPHERE = register("transphere", TransphereParticleEffect.FACTORY, (particleType) -> TransphereParticleEffect.CODEC, false);
		BLOCK_POS_EVENT_TRANSFER = register("block_pos_event_transfer", BlockPosEventTransferParticleEffect.FACTORY, (particleType) -> BlockPosEventTransferParticleEffect.CODEC, false);
		
		SPARKLESTONE_SPARKLE = register("sparklestone_sparkle", false);
		SPARKLESTONE_SPARKLE_SMALL = register("sparklestone_sparkle_small", false);
		SPARKLESTONE_SPARKLE_TINY = register("sparklestone_sparkle_tiny", false);
		VOID_FOG = register("void_fog", false);
		MUD_POP = register("mud_pop", false);
		LIQUID_CRYSTAL_SPARKLE = register("liquid_crystal_sparkle", false);
		BLUE_BUBBLE_POP = register("blue_bubble_pop", false);
		GREEN_BUBBLE_POP = register("green_bubble_pop", false);
		SPIRIT_SALLOW = register("spirit_sallow", false);
		DECAY_PLACE = register("decay_place", false);
		SHOOTING_STAR = register("shooting_star", false);
		JADE_VINES = register("jade_vines", false);
		JADE_VINES_BLOOM = register("jade_vines_bloom", false);
		
		
		BLACK_FALLING_SPORE_BLOSSOM = register("black_falling_spore_blossom", false);
		BLUE_FALLING_SPORE_BLOSSOM = register("blue_falling_spore_blossom", false);
		BROWN_FALLING_SPORE_BLOSSOM = register("brown_falling_spore_blossom", false);
		CYAN_FALLING_SPORE_BLOSSOM = register("cyan_falling_spore_blossom", false);
		GRAY_FALLING_SPORE_BLOSSOM = register("gray_falling_spore_blossom", false);
		GREEN_FALLING_SPORE_BLOSSOM = register("green_falling_spore_blossom", false);
		LIGHT_BLUE_FALLING_SPORE_BLOSSOM = register("light_blue_falling_spore_blossom", false);
		LIGHT_GRAY_FALLING_SPORE_BLOSSOM = register("light_gray_falling_spore_blossom", false);
		LIME_FALLING_SPORE_BLOSSOM = register("lime_falling_spore_blossom", false);
		MAGENTA_FALLING_SPORE_BLOSSOM = register("magenta_falling_spore_blossom", false);
		ORANGE_FALLING_SPORE_BLOSSOM = register("orange_falling_spore_blossom", false);
		PINK_FALLING_SPORE_BLOSSOM = register("pink_falling_spore_blossom", false);
		PURPLE_FALLING_SPORE_BLOSSOM = register("purple_falling_spore_blossom", false);
		RED_FALLING_SPORE_BLOSSOM = register("red_falling_spore_blossom", false);
		WHITE_FALLING_SPORE_BLOSSOM = register("white_falling_spore_blossom", false);
		YELLOW_FALLING_SPORE_BLOSSOM = register("yellow_falling_spore_blossom", false);
		
		BLACK_SPORE_BLOSSOM_AIR = register("black_spore_blossom_air", false);
		BLUE_SPORE_BLOSSOM_AIR = register("blue_spore_blossom_air", false);
		BROWN_SPORE_BLOSSOM_AIR = register("brown_spore_blossom_air", false);
		CYAN_SPORE_BLOSSOM_AIR = register("cyan_spore_blossom_air", false);
		GRAY_SPORE_BLOSSOM_AIR = register("gray_spore_blossom_air", false);
		GREEN_SPORE_BLOSSOM_AIR = register("green_spore_blossom_air", false);
		LIGHT_BLUE_SPORE_BLOSSOM_AIR = register("light_blue_spore_blossom_air", false);
		LIGHT_GRAY_SPORE_BLOSSOM_AIR = register("light_gray_spore_blossom_air", false);
		LIME_SPORE_BLOSSOM_AIR = register("lime_spore_blossom_air", false);
		MAGENTA_SPORE_BLOSSOM_AIR = register("magenta_spore_blossom_air", false);
		ORANGE_SPORE_BLOSSOM_AIR = register("orange_spore_blossom_air", false);
		PINK_SPORE_BLOSSOM_AIR = register("pink_spore_blossom_air", false);
		PURPLE_SPORE_BLOSSOM_AIR = register("purple_spore_blossom_air", false);
		RED_SPORE_BLOSSOM_AIR = register("red_spore_blossom_air", false);
		WHITE_SPORE_BLOSSOM_AIR = register("white_spore_blossom_air", false);
		YELLOW_SPORE_BLOSSOM_AIR = register("yellow_spore_blossom_air", false);
		
		BLACK_CRAFTING = register("black_crafting", false);
		BLUE_CRAFTING = register("blue_crafting", false);
		BROWN_CRAFTING = register("brown_crafting", false);
		CYAN_CRAFTING = register("cyan_crafting", false);
		GRAY_CRAFTING = register("gray_crafting", false);
		GREEN_CRAFTING = register("green_crafting", false);
		LIGHT_BLUE_CRAFTING = register("light_blue_crafting", false);
		LIGHT_GRAY_CRAFTING = register("light_gray_crafting", false);
		LIME_CRAFTING = register("lime_crafting", false);
		MAGENTA_CRAFTING = register("magenta_crafting", false);
		ORANGE_CRAFTING = register("orange_crafting", false);
		PINK_CRAFTING = register("pink_crafting", false);
		PURPLE_CRAFTING = register("purple_crafting", false);
		RED_CRAFTING = register("red_crafting", false);
		WHITE_CRAFTING = register("white_crafting", false);
		YELLOW_CRAFTING = register("yellow_crafting", false);
		
		BLACK_FLUID_RISING = register("black_fluid_rising", false);
		BLUE_FLUID_RISING = register("blue_fluid_rising", false);
		BROWN_FLUID_RISING = register("brown_fluid_rising", false);
		CYAN_FLUID_RISING = register("cyan_fluid_rising", false);
		GRAY_FLUID_RISING = register("gray_fluid_rising", false);
		GREEN_FLUID_RISING = register("green_fluid_rising", false);
		LIGHT_BLUE_FLUID_RISING = register("light_blue_fluid_rising", false);
		LIGHT_GRAY_FLUID_RISING = register("light_gray_fluid_rising", false);
		LIME_FLUID_RISING = register("lime_fluid_rising", false);
		MAGENTA_FLUID_RISING = register("magenta_fluid_rising", false);
		ORANGE_FLUID_RISING = register("orange_fluid_rising", false);
		PINK_FLUID_RISING = register("pink_fluid_rising", false);
		PURPLE_FLUID_RISING = register("purple_fluid_rising", false);
		RED_FLUID_RISING = register("red_fluid_rising", false);
		WHITE_FLUID_RISING = register("white_fluid_rising", false);
		YELLOW_FLUID_RISING = register("yellow_fluid_rising", false);
		
		BLACK_SPARKLE_RISING = register("black_sparkle_rising", false);
		BLUE_SPARKLE_RISING = register("blue_sparkle_rising", false);
		BROWN_SPARKLE_RISING = register("brown_sparkle_rising", false);
		CYAN_SPARKLE_RISING = register("cyan_sparkle_rising", false);
		GRAY_SPARKLE_RISING = register("gray_sparkle_rising", false);
		GREEN_SPARKLE_RISING = register("green_sparkle_rising", false);
		LIGHT_BLUE_SPARKLE_RISING = register("light_blue_sparkle_rising", false);
		LIGHT_GRAY_SPARKLE_RISING = register("light_gray_sparkle_rising", false);
		LIME_SPARKLE_RISING = register("lime_sparkle_rising", false);
		MAGENTA_SPARKLE_RISING = register("magenta_sparkle_rising", false);
		ORANGE_SPARKLE_RISING = register("orange_sparkle_rising", false);
		PINK_SPARKLE_RISING = register("pink_sparkle_rising", false);
		PURPLE_SPARKLE_RISING = register("purple_sparkle_rising", false);
		RED_SPARKLE_RISING = register("red_sparkle_rising", false);
		WHITE_SPARKLE_RISING = register("white_sparkle_rising", false);
		YELLOW_SPARKLE_RISING = register("yellow_sparkle_rising", false);
	}
	
	@NotNull
	public static DefaultParticleType getCraftingParticle(DyeColor dyeColor) {
		return switch (dyeColor) {
			case BLACK -> BLACK_CRAFTING;
			case BLUE -> BLUE_CRAFTING;
			case BROWN -> BROWN_CRAFTING;
			case CYAN -> CYAN_CRAFTING;
			case GRAY -> GRAY_CRAFTING;
			case GREEN -> GREEN_CRAFTING;
			case LIGHT_BLUE -> LIGHT_BLUE_CRAFTING;
			case LIGHT_GRAY -> LIGHT_GRAY_CRAFTING;
			case LIME -> LIME_CRAFTING;
			case MAGENTA -> MAGENTA_CRAFTING;
			case ORANGE -> ORANGE_CRAFTING;
			case PINK -> PINK_CRAFTING;
			case PURPLE -> PURPLE_CRAFTING;
			case RED -> RED_CRAFTING;
			case YELLOW -> YELLOW_CRAFTING;
			default -> WHITE_CRAFTING;
		};
	}
	
	@NotNull
	public static ParticleEffect getFluidRisingParticle(DyeColor dyeColor) {
		return switch (dyeColor) {
			case BLACK -> BLACK_FLUID_RISING;
			case BLUE -> BLUE_FLUID_RISING;
			case BROWN -> BROWN_FLUID_RISING;
			case CYAN -> CYAN_FLUID_RISING;
			case GRAY -> GRAY_FLUID_RISING;
			case GREEN -> GREEN_FLUID_RISING;
			case LIGHT_BLUE -> LIGHT_BLUE_FLUID_RISING;
			case LIGHT_GRAY -> LIGHT_GRAY_FLUID_RISING;
			case LIME -> LIME_FLUID_RISING;
			case MAGENTA -> MAGENTA_FLUID_RISING;
			case ORANGE -> ORANGE_FLUID_RISING;
			case PINK -> PINK_FLUID_RISING;
			case PURPLE -> PURPLE_FLUID_RISING;
			case RED -> RED_FLUID_RISING;
			case YELLOW -> YELLOW_FLUID_RISING;
			default -> WHITE_FLUID_RISING;
		};
	}
	
	@NotNull
	public static ParticleEffect getSparkleRisingParticle(DyeColor dyeColor) {
		return switch (dyeColor) {
			case BLACK -> BLACK_SPARKLE_RISING;
			case BLUE -> BLUE_SPARKLE_RISING;
			case BROWN -> BROWN_SPARKLE_RISING;
			case CYAN -> CYAN_SPARKLE_RISING;
			case GRAY -> GRAY_SPARKLE_RISING;
			case GREEN -> GREEN_SPARKLE_RISING;
			case LIGHT_BLUE -> LIGHT_BLUE_SPARKLE_RISING;
			case LIGHT_GRAY -> LIGHT_GRAY_SPARKLE_RISING;
			case LIME -> LIME_SPARKLE_RISING;
			case MAGENTA -> MAGENTA_SPARKLE_RISING;
			case ORANGE -> ORANGE_SPARKLE_RISING;
			case PINK -> PINK_SPARKLE_RISING;
			case PURPLE -> PURPLE_SPARKLE_RISING;
			case RED -> RED_SPARKLE_RISING;
			case YELLOW -> YELLOW_SPARKLE_RISING;
			default -> WHITE_SPARKLE_RISING;
		};
	}
	
}
