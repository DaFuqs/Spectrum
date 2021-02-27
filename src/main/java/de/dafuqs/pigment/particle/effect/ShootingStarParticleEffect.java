package de.dafuqs.pigment.particle.effect;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;

public class ShootingStarParticleEffect implements ParticleEffect {

    protected ShootingStarParticleEffect(boolean alwaysShow) {
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
