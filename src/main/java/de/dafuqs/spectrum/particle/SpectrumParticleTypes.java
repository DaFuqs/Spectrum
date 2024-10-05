package de.dafuqs.spectrum.particle;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.fabricmc.fabric.api.particle.v1.*;
import net.minecraft.particle.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class SpectrumParticleTypes {
	
	public static ParticleType<ItemTransmissionParticleEffect> ITEM_TRANSMISSION = register("item_transfer", ItemTransmissionParticleEffect.FACTORY, (particleType) -> ItemTransmissionParticleEffect.CODEC, false);
	public static ParticleType<ExperienceTransmissionParticleEffect> EXPERIENCE_TRANSMISSION = register("experience_transfer", ExperienceTransmissionParticleEffect.FACTORY, (particleType) -> ExperienceTransmissionParticleEffect.CODEC, false);
	public static ParticleType<WirelessRedstoneTransmissionParticleEffect> WIRELESS_REDSTONE_TRANSMISSION = register("wireless_redstone_transmission", WirelessRedstoneTransmissionParticleEffect.FACTORY, (particleType) -> WirelessRedstoneTransmissionParticleEffect.CODEC, false);
	public static ParticleType<ColoredTransmissionParticleEffect> COLORED_TRANSMISSION = register("colored_transmission", ColoredTransmissionParticleEffect.FACTORY, (particleType) -> ColoredTransmissionParticleEffect.CODEC, false);
	public static ParticleType<BlockPosEventTransmissionParticleEffect> BLOCK_POS_EVENT_TRANSMISSION = register("block_pos_event_transmission", BlockPosEventTransmissionParticleEffect.FACTORY, (particleType) -> BlockPosEventTransmissionParticleEffect.CODEC, false);
	public static ParticleType<PastelTransmissionParticleEffect> PASTEL_TRANSMISSION = register("pastel_transmission", PastelTransmissionParticleEffect.FACTORY, (particleType) -> PastelTransmissionParticleEffect.CODEC, false);
	public static ParticleType<HummingstoneTransmissionParticleEffect> HUMMINGSTONE_TRANSMISSION = register("hummingstone_transmission", HummingstoneTransmissionParticleEffect.FACTORY, (particleType) -> HummingstoneTransmissionParticleEffect.CODEC, false);
	
	public static DefaultParticleType SHIMMERSTONE_SPARKLE = register("shimmerstone_sparkle", false);
	public static DefaultParticleType SHIMMERSTONE_SPARKLE_SMALL = register("shimmerstone_sparkle_small", false);
	public static DefaultParticleType SHIMMERSTONE_SPARKLE_TINY = register("shimmerstone_sparkle_tiny", false);
	public static DefaultParticleType VOID_FOG = register("void_fog", false);
	
	public static DefaultParticleType RUNES = register("runes", false);
	public static DefaultParticleType AZURE_DIKE_RUNES = register("azure_dike_runes", false);
	public static DefaultParticleType AZURE_DIKE_RUNES_MAJOR = register("azure_dike_runes_major", false);
	public static DefaultParticleType DRAKEBLOOD_DIKE_RUNES = register("drakeblood_dike_runes", false);
	public static DefaultParticleType DRAKEBLOOD_DIKE_RUNES_MAJOR = register("drakeblood_dike_runes_major", false);
	public static DefaultParticleType MALACHITE_DIKE_RUNES = register("malachite_dike_runes", false);
	public static DefaultParticleType MALACHITE_DIKE_RUNES_MAJOR = register("malachite_dike_runes_major", false);

	public static DefaultParticleType AZURE_AURA = register("azure_aura", false);
	public static DefaultParticleType AZURE_MOTE = register("azure_mote", false);
	public static DefaultParticleType AZURE_MOTE_SMALL = register("azure_mote_small", false);

	public static DefaultParticleType BLUE_BUBBLE_POP = register("blue_bubble_pop", false);
	public static DefaultParticleType GREEN_BUBBLE_POP = register("green_bubble_pop", false);
	
	public static DefaultParticleType SPIRIT_SALLOW = register("spirit_sallow", false);
	public static DefaultParticleType DECAY_PLACE = register("decay_place", false);
	public static DefaultParticleType DIVINITY = register("divinity", false);
	public static DefaultParticleType SHOOTING_STAR = register("shooting_star", false);
	public static DefaultParticleType JADE_VINES = register("jade_vines", false);
	public static DefaultParticleType JADE_VINES_BLOOM = register("jade_vines_bloom", false);
	public static DefaultParticleType MOONSTONE_STRIKE = register("moonstone_strike", true);
	public static DefaultParticleType MIRROR_IMAGE = register("mirror_image", true);
	
	public static DefaultParticleType LAVA_FISHING = register("lava_fishing", false);
	
	public static DefaultParticleType PRIMORDIAL_COSY_SMOKE = register("primordial_cosy_smoke", true);
	public static DefaultParticleType PRIMORDIAL_SIGNAL_SMOKE = register("primordial_signal_smoke", true);
	public static DefaultParticleType PRIMORDIAL_SMOKE = register("primordial_smoke", true);
	public static DefaultParticleType PRIMORDIAL_FLAME = register("primordial_flame", true);
	public static DefaultParticleType PRIMORDIAL_FLAME_SMALL = register("primordial_flame_small", true);
	
	public static DefaultParticleType MUD_SPLASH = register("mud_splash", false);
	public static DefaultParticleType DRIPPING_MUD = register("dripping_mud", false);
	public static DefaultParticleType FALLING_MUD = register("falling_mud", false);
	public static DefaultParticleType LANDING_MUD = register("landing_mud", false);
	public static DefaultParticleType MUD_FISHING = register("mud_fishing", false);
	public static DefaultParticleType MUD_POP = register("mud_pop", false);
	
	public static DefaultParticleType LIQUID_CRYSTAL_SPLASH = register("liquid_crystal_splash", false);
	public static DefaultParticleType DRIPPING_LIQUID_CRYSTAL = register("dripping_liquid_crystal", false);
	public static DefaultParticleType FALLING_LIQUID_CRYSTAL = register("falling_liquid_crystal", false);
	public static DefaultParticleType LANDING_LIQUID_CRYSTAL = register("landing_liquid_crystal", false);
	public static DefaultParticleType LIQUID_CRYSTAL_FISHING = register("liquid_crystal_fishing", false);
	public static DefaultParticleType LIQUID_CRYSTAL_SPARKLE = register("liquid_crystal_sparkle", false);
	
	public static DefaultParticleType MIDNIGHT_SOLUTION_SPLASH = register("midnight_solution_splash", false);
	public static DefaultParticleType DRIPPING_MIDNIGHT_SOLUTION = register("dripping_midnight_solution", false);
	public static DefaultParticleType FALLING_MIDNIGHT_SOLUTION = register("falling_midnight_solution", false);
	public static DefaultParticleType LANDING_MIDNIGHT_SOLUTION = register("landing_midnight_solution", false);
	public static DefaultParticleType MIDNIGHT_SOLUTION_FISHING = register("midnight_solution_fishing", false);
	public static DefaultParticleType DRAGONROT = register("dragonrot", false);
	
	public static DefaultParticleType DRAGONROT_SPLASH = register("dragonrot_splash", false);
	public static DefaultParticleType DRIPPING_DRAGONROT = register("dripping_dragonrot", false);
	public static DefaultParticleType FALLING_DRAGONROT = register("falling_dragonrot", false);
	public static DefaultParticleType LANDING_DRAGONROT = register("landing_dragonrot", false);
	public static DefaultParticleType DRAGONROT_FISHING = register("dragonrot_fishing", false);
	
	public static DefaultParticleType BLACK_FALLING_SPORE_BLOSSOM = register("black_falling_spore_blossom", false);
	public static DefaultParticleType BLUE_FALLING_SPORE_BLOSSOM = register("blue_falling_spore_blossom", false);
	public static DefaultParticleType BROWN_FALLING_SPORE_BLOSSOM = register("brown_falling_spore_blossom", false);
	public static DefaultParticleType CYAN_FALLING_SPORE_BLOSSOM = register("cyan_falling_spore_blossom", false);
	public static DefaultParticleType GRAY_FALLING_SPORE_BLOSSOM = register("gray_falling_spore_blossom", false);
	public static DefaultParticleType GREEN_FALLING_SPORE_BLOSSOM = register("green_falling_spore_blossom", false);
	public static DefaultParticleType LIGHT_BLUE_FALLING_SPORE_BLOSSOM = register("light_blue_falling_spore_blossom", false);
	public static DefaultParticleType LIGHT_GRAY_FALLING_SPORE_BLOSSOM = register("light_gray_falling_spore_blossom", false);
	public static DefaultParticleType LIME_FALLING_SPORE_BLOSSOM = register("lime_falling_spore_blossom", false);
	public static DefaultParticleType MAGENTA_FALLING_SPORE_BLOSSOM = register("magenta_falling_spore_blossom", false);
	public static DefaultParticleType ORANGE_FALLING_SPORE_BLOSSOM = register("orange_falling_spore_blossom", false);
	public static DefaultParticleType PINK_FALLING_SPORE_BLOSSOM = register("pink_falling_spore_blossom", false);
	public static DefaultParticleType PURPLE_FALLING_SPORE_BLOSSOM = register("purple_falling_spore_blossom", false);
	public static DefaultParticleType RED_FALLING_SPORE_BLOSSOM = register("red_falling_spore_blossom", false);
	public static DefaultParticleType WHITE_FALLING_SPORE_BLOSSOM = register("white_falling_spore_blossom", false);
	public static DefaultParticleType YELLOW_FALLING_SPORE_BLOSSOM = register("yellow_falling_spore_blossom", false);
	
	public static DefaultParticleType BLACK_SPORE_BLOSSOM_AIR = register("black_spore_blossom_air", false);
	public static DefaultParticleType BLUE_SPORE_BLOSSOM_AIR = register("blue_spore_blossom_air", false);
	public static DefaultParticleType BROWN_SPORE_BLOSSOM_AIR = register("brown_spore_blossom_air", false);
	public static DefaultParticleType CYAN_SPORE_BLOSSOM_AIR = register("cyan_spore_blossom_air", false);
	public static DefaultParticleType GRAY_SPORE_BLOSSOM_AIR = register("gray_spore_blossom_air", false);
	public static DefaultParticleType GREEN_SPORE_BLOSSOM_AIR = register("green_spore_blossom_air", false);
	public static DefaultParticleType LIGHT_BLUE_SPORE_BLOSSOM_AIR = register("light_blue_spore_blossom_air", false);
	public static DefaultParticleType LIGHT_GRAY_SPORE_BLOSSOM_AIR = register("light_gray_spore_blossom_air", false);
	public static DefaultParticleType LIME_SPORE_BLOSSOM_AIR = register("lime_spore_blossom_air", false);
	public static DefaultParticleType MAGENTA_SPORE_BLOSSOM_AIR = register("magenta_spore_blossom_air", false);
	public static DefaultParticleType ORANGE_SPORE_BLOSSOM_AIR = register("orange_spore_blossom_air", false);
	public static DefaultParticleType PINK_SPORE_BLOSSOM_AIR = register("pink_spore_blossom_air", false);
	public static DefaultParticleType PURPLE_SPORE_BLOSSOM_AIR = register("purple_spore_blossom_air", false);
	public static DefaultParticleType RED_SPORE_BLOSSOM_AIR = register("red_spore_blossom_air", false);
	public static DefaultParticleType WHITE_SPORE_BLOSSOM_AIR = register("white_spore_blossom_air", false);
	public static DefaultParticleType YELLOW_SPORE_BLOSSOM_AIR = register("yellow_spore_blossom_air", false);
	
	public static DefaultParticleType BLACK_CRAFTING = register("black_crafting", false);
	public static DefaultParticleType BLUE_CRAFTING = register("blue_crafting", false);
	public static DefaultParticleType BROWN_CRAFTING = register("brown_crafting", false);
	public static DefaultParticleType CYAN_CRAFTING = register("cyan_crafting", false);
	public static DefaultParticleType GRAY_CRAFTING = register("gray_crafting", false);
	public static DefaultParticleType GREEN_CRAFTING = register("green_crafting", false);
	public static DefaultParticleType LIGHT_BLUE_CRAFTING = register("light_blue_crafting", false);
	public static DefaultParticleType LIGHT_GRAY_CRAFTING = register("light_gray_crafting", false);
	public static DefaultParticleType LIME_CRAFTING = register("lime_crafting", false);
	public static DefaultParticleType MAGENTA_CRAFTING = register("magenta_crafting", false);
	public static DefaultParticleType ORANGE_CRAFTING = register("orange_crafting", false);
	public static DefaultParticleType PINK_CRAFTING = register("pink_crafting", false);
	public static DefaultParticleType PURPLE_CRAFTING = register("purple_crafting", false);
	public static DefaultParticleType RED_CRAFTING = register("red_crafting", false);
	public static DefaultParticleType WHITE_CRAFTING = register("white_crafting", false);
	public static DefaultParticleType YELLOW_CRAFTING = register("yellow_crafting", false);
	
	public static DefaultParticleType BLACK_FLUID_RISING = register("black_fluid_rising", false);
	public static DefaultParticleType BLUE_FLUID_RISING = register("blue_fluid_rising", false);
	public static DefaultParticleType BROWN_FLUID_RISING = register("brown_fluid_rising", false);
	public static DefaultParticleType CYAN_FLUID_RISING = register("cyan_fluid_rising", false);
	public static DefaultParticleType GRAY_FLUID_RISING = register("gray_fluid_rising", false);
	public static DefaultParticleType GREEN_FLUID_RISING = register("green_fluid_rising", false);
	public static DefaultParticleType LIGHT_BLUE_FLUID_RISING = register("light_blue_fluid_rising", false);
	public static DefaultParticleType LIGHT_GRAY_FLUID_RISING = register("light_gray_fluid_rising", false);
	public static DefaultParticleType LIME_FLUID_RISING = register("lime_fluid_rising", false);
	public static DefaultParticleType MAGENTA_FLUID_RISING = register("magenta_fluid_rising", false);
	public static DefaultParticleType ORANGE_FLUID_RISING = register("orange_fluid_rising", false);
	public static DefaultParticleType PINK_FLUID_RISING = register("pink_fluid_rising", false);
	public static DefaultParticleType PURPLE_FLUID_RISING = register("purple_fluid_rising", false);
	public static DefaultParticleType RED_FLUID_RISING = register("red_fluid_rising", false);
	public static DefaultParticleType WHITE_FLUID_RISING = register("white_fluid_rising", false);
	public static DefaultParticleType YELLOW_FLUID_RISING = register("yellow_fluid_rising", false);
	
	public static DefaultParticleType BLACK_SPARKLE_RISING = register("black_sparkle_rising", false);
	public static DefaultParticleType BLUE_SPARKLE_RISING = register("blue_sparkle_rising", false);
	public static DefaultParticleType BROWN_SPARKLE_RISING = register("brown_sparkle_rising", false);
	public static DefaultParticleType CYAN_SPARKLE_RISING = register("cyan_sparkle_rising", false);
	public static DefaultParticleType GRAY_SPARKLE_RISING = register("gray_sparkle_rising", false);
	public static DefaultParticleType GREEN_SPARKLE_RISING = register("green_sparkle_rising", false);
	public static DefaultParticleType LIGHT_BLUE_SPARKLE_RISING = register("light_blue_sparkle_rising", false);
	public static DefaultParticleType LIGHT_GRAY_SPARKLE_RISING = register("light_gray_sparkle_rising", false);
	public static DefaultParticleType LIME_SPARKLE_RISING = register("lime_sparkle_rising", false);
	public static DefaultParticleType MAGENTA_SPARKLE_RISING = register("magenta_sparkle_rising", false);
	public static DefaultParticleType ORANGE_SPARKLE_RISING = register("orange_sparkle_rising", false);
	public static DefaultParticleType PINK_SPARKLE_RISING = register("pink_sparkle_rising", false);
	public static DefaultParticleType PURPLE_SPARKLE_RISING = register("purple_sparkle_rising", false);
	public static DefaultParticleType RED_SPARKLE_RISING = register("red_sparkle_rising", false);
	public static DefaultParticleType WHITE_SPARKLE_RISING = register("white_sparkle_rising", false);
	public static DefaultParticleType YELLOW_SPARKLE_RISING = register("yellow_sparkle_rising", false);
	
	public static DefaultParticleType BLACK_EXPLOSION = register("black_explosion", true);
	public static DefaultParticleType BLUE_EXPLOSION = register("blue_explosion", true);
	public static DefaultParticleType BROWN_EXPLOSION = register("brown_explosion", true);
	public static DefaultParticleType CYAN_EXPLOSION = register("cyan_explosion", true);
	public static DefaultParticleType GRAY_EXPLOSION = register("gray_explosion", true);
	public static DefaultParticleType GREEN_EXPLOSION = register("green_explosion", true);
	public static DefaultParticleType LIGHT_BLUE_EXPLOSION = register("light_blue_explosion", true);
	public static DefaultParticleType LIGHT_GRAY_EXPLOSION = register("light_gray_explosion", true);
	public static DefaultParticleType LIME_EXPLOSION = register("lime_explosion", true);
	public static DefaultParticleType MAGENTA_EXPLOSION = register("magenta_explosion", true);
	public static DefaultParticleType ORANGE_EXPLOSION = register("orange_explosion", true);
	public static DefaultParticleType PINK_EXPLOSION = register("pink_explosion", true);
	public static DefaultParticleType PURPLE_EXPLOSION = register("purple_explosion", true);
	public static DefaultParticleType RED_EXPLOSION = register("red_explosion", true);
	public static DefaultParticleType WHITE_EXPLOSION = register("white_explosion", true);
	public static DefaultParticleType YELLOW_EXPLOSION = register("yellow_explosion", true);

	// Biome particles
	public static DefaultParticleType FALLING_ASH = register("falling_ash", true);
	public static DefaultParticleType FIREFLY = register("firefly", true);
	public static DefaultParticleType BLOODFLY = register("bloodfly", true);
	public static DefaultParticleType QUARTZ_FLUFF = register("quartz_fluff", true);

	public static DefaultParticleType LIGHT_RAIN = register("light_rain", true);
	public static DefaultParticleType HEAVY_RAIN = register("heavy_rain", true);
	public static DefaultParticleType RAIN_SPLASH = register("rain_splash", false);
	public static DefaultParticleType RAIN_RIPPLE = register("rain_ripple", false);

	public static DefaultParticleType LIGHT_TRAIL = register("light_trail", true);
	
	public static ParticleType<DynamicParticleEffect> DYNAMIC = register("particle_spawner", DynamicParticleEffect.FACTORY, (particleType) -> DynamicParticleEffect.CODEC, false);
	public static ParticleType<DynamicParticleEffectAlwaysShow> DYNAMIC_ALWAYS_SHOW = register("particle_spawner_always_show", DynamicParticleEffectAlwaysShow.FACTORY, (particleType) -> DynamicParticleEffectAlwaysShow.CODEC, true);
	
	// Simple particles
	private static DefaultParticleType register(String name, boolean alwaysShow) {
		return Registry.register(Registries.PARTICLE_TYPE, SpectrumCommon.locate(name), FabricParticleTypes.simple(alwaysShow));
	}
	
	// complex particles
	@SuppressWarnings("deprecation")
	private static <T extends ParticleEffect> ParticleType<T> register(String name, ParticleEffect.Factory<T> factory, final Function<ParticleType<T>, Codec<T>> function, boolean alwaysShow) {
		return Registry.register(Registries.PARTICLE_TYPE, SpectrumCommon.locate(name), new ParticleType<T>(alwaysShow, factory) {
			@Override
			public Codec<T> getCodec() {
				return function.apply(this);
			}
		});
	}
	
	public static void register() {
	
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
	
	@NotNull
	public static ParticleEffect getExplosionParticle(DyeColor dyeColor) {
		return switch (dyeColor) {
			case BLACK -> BLACK_EXPLOSION;
			case BLUE -> BLUE_EXPLOSION;
			case BROWN -> BROWN_EXPLOSION;
			case CYAN -> CYAN_EXPLOSION;
			case GRAY -> GRAY_EXPLOSION;
			case GREEN -> GREEN_EXPLOSION;
			case LIGHT_BLUE -> LIGHT_BLUE_EXPLOSION;
			case LIGHT_GRAY -> LIGHT_GRAY_EXPLOSION;
			case LIME -> LIME_EXPLOSION;
			case MAGENTA -> MAGENTA_EXPLOSION;
			case ORANGE -> ORANGE_EXPLOSION;
			case PINK -> PINK_EXPLOSION;
			case PURPLE -> PURPLE_EXPLOSION;
			case RED -> RED_EXPLOSION;
			case YELLOW -> YELLOW_EXPLOSION;
			default -> WHITE_EXPLOSION;
		};
	}
	
}
