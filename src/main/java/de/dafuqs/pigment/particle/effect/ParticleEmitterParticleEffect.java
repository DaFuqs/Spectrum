package de.dafuqs.pigment.particle.effect;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

public class ParticleEmitterParticleEffect implements ParticleEffect {

    protected ParticleEmitterParticleEffect(boolean alwaysShow) {
        super();
    }

    @Override
    public ParticleType<?> getType() {
        return null;
    }

    @Override
    public void write(PacketByteBuf buf) {

    }

    @Override
    public String asString() {
        return null;
    }
}
