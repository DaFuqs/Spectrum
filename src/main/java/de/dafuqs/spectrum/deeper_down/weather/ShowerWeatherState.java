package de.dafuqs.spectrum.deeper_down.weather;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.particle.*;
import net.minecraft.client.world.*;
import net.minecraft.particle.*;
import net.minecraft.util.math.random.Random;
import org.joml.*;

import java.util.*;

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
