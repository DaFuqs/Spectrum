package de.dafuqs.spectrum.particle;

import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumParticleTypes {

    public static DefaultParticleType SHOOTING_STAR;
    public static DefaultParticleType SPARKLESTONE_SPARKLE;
    public static DefaultParticleType PARTICLE_EMITTER;
    public static DefaultParticleType VOID_FOG;

    public static DefaultParticleType register(String name, boolean alwaysShow) {
        return Registry.register(Registry.PARTICLE_TYPE, new Identifier(SpectrumCommon.MOD_ID, name), FabricParticleTypes.simple(alwaysShow));
    }

    public static void register() {
        SHOOTING_STAR = register("shooting_star", true);
        SPARKLESTONE_SPARKLE = register("sparklestone_sparkle", true);
        PARTICLE_EMITTER = register("particle_emitter", false);
        VOID_FOG = register("void_fog", false);
    }

}
