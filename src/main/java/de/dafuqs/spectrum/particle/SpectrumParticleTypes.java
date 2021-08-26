package de.dafuqs.spectrum.particle;

import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumParticleTypes {

    public static DefaultParticleType SHOOTING_STAR;
    public static DefaultParticleType SPARKLESTONE_SPARKLE;
    public static DefaultParticleType PARTICLE_SPAWNER;
    public static DefaultParticleType VOID_FOG;
    public static DefaultParticleType MUD_POP;
    public static DefaultParticleType LIQUID_CRYSTAL_SPARKLE;

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

    public static DefaultParticleType register(String name, boolean alwaysShow) {
        return Registry.register(Registry.PARTICLE_TYPE, new Identifier(SpectrumCommon.MOD_ID, name), FabricParticleTypes.simple(alwaysShow));
    }

    public static void register() {
        SHOOTING_STAR = register("shooting_star", true);
        SPARKLESTONE_SPARKLE = register("sparklestone_sparkle", true);
        PARTICLE_SPAWNER = register("particle_spawner", false);
        VOID_FOG = register("void_fog", false);
        MUD_POP = register("mud_pop", false);
        LIQUID_CRYSTAL_SPARKLE = register("liquid_crystal_sparkle", false);

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
    }

}
