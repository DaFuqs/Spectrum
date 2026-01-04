package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.resources.sounds.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import org.jetbrains.annotations.*;

public class BiomeAttenuatingSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {

    @Nullable public static BiomeAttenuatingSoundInstance WIND_HIGH = null;
    @Nullable public static BiomeAttenuatingSoundInstance WIND_LOW = null;
    @Nullable public static BiomeAttenuatingSoundInstance SHOWER = null;
    @Nullable public static BiomeAttenuatingSoundInstance LAMENTS = null;
    private static boolean clear = true;
	private final Minecraft client = Minecraft.getInstance();
    private static final int MAX_DURATION = 80;
	private final ResourceKey<Biome> biome;
    private final float volumeMod;
    private float coverage, lastCoverage;
    private int biomeTicks = 1, coverageUpdateTicks;
    private final boolean altPitch;
    private boolean finished;
	
	protected BiomeAttenuatingSoundInstance(ResourceKey<Biome> biome, SoundEvent sound, float volumeMod, boolean altMod) {
		super(sound, SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
		looping = true;
		delay = 0;
        this.biome = biome;
        this.volumeMod = volumeMod;
        this.altPitch = altMod;
        this.relative = true;

        var camera = client.getCameraEntity();

        if (camera != null) {
			updateCoverage(camera.level(), camera);
        }

        updateVolumeAndPitch();
    }

    @Override
    public void tick() {
        var camera = client.getCameraEntity();

        if (camera == null) {
            finished = true;
            return;
        }
		
		var world = camera.level();

        if (coverageUpdateTicks < 15)
            coverageUpdateTicks++;

        if (coverageUpdateTicks == 15) {
            coverageUpdateTicks = 0;
            updateCoverage(world, camera);
        }
		
		if (world.getBiome(camera.blockPosition()).is(biome) && biomeTicks < MAX_DURATION) {
            biomeTicks++;
        }
        else if (biomeTicks > 0) {
            biomeTicks--;
        }

        updateVolumeAndPitch();
    }
	
	private void updateCoverage(Level world, Entity camera) {
		var pos = BlockPos.containing(camera.getEyePosition());
        lastCoverage = coverage;
        coverage = 0;

        for (Direction direction : Direction.values()) {
            if (direction == Direction.DOWN)
                continue;

            var up = direction == Direction.UP;
            var max = up ? 13 : 7;

            for (int i = 1; i < max; i++) {
				var offPos = pos.relative(direction, i);
                var state = world.getBlockState(offPos);

                if (up) {
					if (state.isFaceSturdy(world, offPos, direction.getOpposite()) || state.isFaceSturdy(world, offPos, direction)) {
                        coverage += 0.1334F / i;
                        break;
                    }
                    continue;
                }
				
				if (state.isFaceSturdy(world, offPos, direction.getOpposite()) || state.isFaceSturdy(world, offPos, direction)) {
                    coverage += 0.16F / i;
                    break;
                }
            }
        }
    }

    private void updateVolumeAndPitch() {
		var coverageMod = Mth.clampedLerp(lastCoverage, coverage, coverageUpdateTicks / 15F) + 0.1F;
        if (!altPitch)
            coverageMod *= 1.5F;

        coverageMod = Math.max(1 - coverageMod, 0);

        this.volume = Math.max(0, ((float) biomeTicks) / MAX_DURATION) * volumeMod * coverageMod;
		
		this.pitch = (altPitch ? 0.9F : 0.65F) * Mth.clamp(coverageMod * 2, 0.5F, 1);
    }

    @Override
	public boolean isStopped() {
        return biomeTicks == 0 || finished;
    }
	
	public static void update(Holder<Biome> biome) {
		var client = Minecraft.getInstance();
        clear = false;
		
		if (WIND_HIGH != null && WIND_HIGH.isStopped()) {
            WIND_HIGH = null;
        }
		
		if (WIND_LOW != null && WIND_LOW.isStopped()) {
            WIND_LOW = null;
        }
		
		if (SHOWER != null && SHOWER.isStopped()) {
            SHOWER = null;
        }
		
		if (LAMENTS != null && LAMENTS.isStopped()) {
            LAMENTS = null;
        }
        
        if (biome.is(SpectrumBiomeKeys.HOWLING_SPIRES)) {
            if (WIND_HIGH == null) {
                WIND_HIGH = new BiomeAttenuatingSoundInstance(SpectrumBiomeKeys.HOWLING_SPIRES, SpectrumSoundEvents.HOWLING_WIND_HIGH, 0.525F, false);
                client.getSoundManager().play(WIND_HIGH);
            }

            if (WIND_LOW == null) {
                WIND_LOW = new BiomeAttenuatingSoundInstance(SpectrumBiomeKeys.HOWLING_SPIRES, SpectrumSoundEvents.HOWLING_WIND_LOW, 1.8F, true);
                client.getSoundManager().play(WIND_LOW);
            }
        } else if (biome.is(SpectrumBiomeKeys.DEEP_DRIPSTONE_CAVES)) {
            if (SHOWER == null) {
                SHOWER = new BiomeAttenuatingSoundInstance(SpectrumBiomeKeys.DEEP_DRIPSTONE_CAVES, SpectrumSoundEvents.SHOWER, 2F, false);
                client.getSoundManager().play(SHOWER);
            }
        } else if (biome.is(SpectrumBiomeKeys.DRAGONROT_SWAMP)) {
            if (LAMENTS == null) {
                LAMENTS = new BiomeAttenuatingSoundInstance(SpectrumBiomeKeys.DRAGONROT_SWAMP, SpectrumSoundEvents.LAMENTS, 1.25F, true);
                client.getSoundManager().play(LAMENTS);
            }
            if (SHOWER == null) {
                SHOWER = new BiomeAttenuatingSoundInstance(SpectrumBiomeKeys.DRAGONROT_SWAMP, SpectrumSoundEvents.SHOWER, 2F, false);
                client.getSoundManager().play(SHOWER);
            }
        }
    }

    public static void clear() {
        if (clear)
            return;

        if (WIND_HIGH != null) {
            WIND_HIGH.finished = true;
            WIND_HIGH = null;
        }

        if (WIND_LOW != null) {
            WIND_LOW.finished = true;
            WIND_LOW = null;
        }

        if (SHOWER != null) {
            SHOWER.finished = true;
            SHOWER = null;
        }

        if (LAMENTS != null) {
            LAMENTS.finished = true;
            LAMENTS = null;
        }

        clear = true;
    }
}
