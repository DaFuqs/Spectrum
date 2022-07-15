package de.dafuqs.spectrum.blocks.spirit_sallow;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.util.math.random.Random;

public class WindStrength {
	private SimplexNoiseSampler[] octaveSamplers;
	private final IntSortedSet octaves = new IntRBTreeSet(ImmutableList.of(-2, -1, 0));

	private static long cachedTick;

	public Vec3d cachedValue;

	public WindStrength() {
		octaveSamplers = new SimplexNoiseSampler[1];
	}

	public Vec3d getWindStrength(long tick, Random random) {
		int i = -octaves.firstInt();
		int j = octaves.lastInt();
		int k = i + j + 1;
		if (k < 1) {
			throw new IllegalArgumentException("Total number of octaves needs to be >= 1");
		} else {
			SimplexNoiseSampler simplexNoiseSampler = new SimplexNoiseSampler(random);
			if (j >= 0 && j < k && octaves.contains(0)) {
				this.octaveSamplers[j] = simplexNoiseSampler;

				this.octaveSamplers = new SimplexNoiseSampler[k];
				if (j >= 0 && j < k && octaves.contains(0)) {
					this.octaveSamplers[j] = simplexNoiseSampler;
				}
			}
		}



		if (tick != cachedTick) {
			cachedValue = new Vec3d(
					octaveSamplers[0].sample(0, 0, (tick + MinecraftClient.getInstance().getTickDelta()) / 512D),
					octaveSamplers[1].sample(0, 0, (tick + MinecraftClient.getInstance().getTickDelta()) / 512D),
					octaveSamplers[2].sample(0, 0, (tick + MinecraftClient.getInstance().getTickDelta()) / 512D)
			);

			cachedTick = tick;
		}

		return cachedValue;
	}

}
