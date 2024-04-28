package de.dafuqs.spectrum.deeper_down.weather;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.WeightedPool;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.joml.Vector3d;

import java.util.Optional;

public class ShowerWeatherState extends SimpleRainLikeState {

    public ShowerWeatherState() {
        super(SpectrumCommon.locate("shower"), 0.02F, 50, 0.075F);
    }

    @Override
    public void spawnAirParticle(ClientWorld world, Vector3d position, Random random) {
        world.addParticle(SpectrumParticleTypes.LIGHT_RAIN, position.x, position.y, position.z, 0, 0, 0);
    }

    @Override
    public void spawnCeilingParticle(ClientWorld world, Vector3d position, Random random) {

    }

    @Override
    public Optional<ParticleType<?>> getCeilingDropletType() {
        return Optional.of(SpectrumParticleTypes.LIGHT_RAIN);
    }
}
