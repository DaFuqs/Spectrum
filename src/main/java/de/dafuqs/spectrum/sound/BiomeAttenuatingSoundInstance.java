package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.registries.SpectrumBiomes;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Optional;

public class BiomeAttenuatingSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {

    public static Optional<BiomeAttenuatingSoundInstance> WIND_HIGH = Optional.empty();
    public static Optional<BiomeAttenuatingSoundInstance> WIND_LOW = Optional.empty();
    public static Optional<BiomeAttenuatingSoundInstance> SHOWER = Optional.empty();
    private final MinecraftClient client = MinecraftClient.getInstance();
    private static final int MAX_DURATION = 80;
    private final RegistryKey<Biome> biome;
    private final float volumeMod;
    private float coverage, lastCoverage;
    private int biomeTicks = 1, coverageUpdateTicks;
    private final boolean altPitch;
    private boolean finished;

    protected BiomeAttenuatingSoundInstance(RegistryKey<Biome> biome, SoundEvent sound, float volumeMod, boolean altMod) {
        super(sound, SoundCategory.AMBIENT, SoundInstance.createRandom());
        repeat = true;
        repeatDelay = 0;
        this.biome = biome;
        this.volumeMod = volumeMod;
        this.altPitch = altMod;
        this.relative = true;

        var camera = client.getCameraEntity();

        if (camera != null) {
            updateCoverage(camera.getWorld(), camera);
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

        var world = camera.getWorld();

        if (coverageUpdateTicks < 15)
            coverageUpdateTicks++;

        if (coverageUpdateTicks == 15) {
            coverageUpdateTicks = 0;
            updateCoverage(world, camera);
        }

        if (world.getBiome(camera.getBlockPos()).matchesKey(biome) && biomeTicks < MAX_DURATION) {
            biomeTicks++;
        }
        else if (biomeTicks > 0) {
            biomeTicks--;
        }

        updateVolumeAndPitch();
    }

    private void updateCoverage(World world, Entity camera) {
        var pos = BlockPos.ofFloored(camera.getEyePos());
        lastCoverage = coverage;
        coverage = 0;

        for (Direction direction : Direction.values()) {
            if (direction == Direction.DOWN)
                continue;

            var up = direction == Direction.UP;
            var max = up ? 13 : 7;

            for (int i = 1; i < max; i++) {
                var offPos = pos.offset(direction, i);
                var state = world.getBlockState(offPos);

                if (up) {
                    if (state.isSideSolidFullSquare(world, offPos, direction.getOpposite()) || state.isSideSolidFullSquare(world, offPos, direction)) {
                        coverage += 0.1334F / i;
                        break;
                    }
                    continue;
                }

                if (state.isSideSolidFullSquare(world, offPos, direction.getOpposite()) || state.isSideSolidFullSquare(world, offPos, direction)) {
                    coverage += 0.16F / i;
                    break;
                }
            }
        }
    }

    private void updateVolumeAndPitch() {
        var coverageMod = MathHelper.clampedLerp(lastCoverage, coverage, coverageUpdateTicks / 15F) + 0.1F;
        if (!altPitch)
            coverageMod *= 1.5F;

        coverageMod = Math.max(1 - coverageMod, 0);

        this.volume = Math.max(0, ((float) biomeTicks) / MAX_DURATION) * volumeMod * coverageMod;

        this.pitch = (altPitch ? 0.9F : 0.65F) * MathHelper.clamp(coverageMod * 2, 0.5F, 1);
    }

    @Override
    public boolean isDone() {
        return biomeTicks == 0 || finished;
    }

    public static void update(RegistryEntry<Biome> biome) {
        var client = MinecraftClient.getInstance();

        if (WIND_HIGH.map(TickableSoundInstance::isDone).orElse(false)) {
            WIND_HIGH = Optional.empty();
        }

        if (WIND_LOW.map(TickableSoundInstance::isDone).orElse(false)) {
            WIND_LOW = Optional.empty();
        }

        if (SHOWER.map(TickableSoundInstance::isDone).orElse(false)) {
            SHOWER = Optional.empty();
        }

        if (biome.matchesKey(SpectrumBiomes.HOWLING_SPIRES)) {
            if (WIND_HIGH.isEmpty()) {
                WIND_HIGH = Optional.of(new BiomeAttenuatingSoundInstance(SpectrumBiomes.HOWLING_SPIRES, SpectrumSoundEvents.HOWLING_WIND_HIGH, 0.525F, false));
                client.getSoundManager().play(WIND_HIGH.get());
            }

            if (WIND_LOW.isEmpty()) {
                WIND_LOW = Optional.of(new BiomeAttenuatingSoundInstance(SpectrumBiomes.HOWLING_SPIRES, SpectrumSoundEvents.HOWLING_WIND_LOW, 1.8F, true));
                client.getSoundManager().play(WIND_LOW.get());
            }
        }
        else if (biome.matchesKey(SpectrumBiomes.DEEP_DRIPSTONE_CAVES)) {
            if (SHOWER.isEmpty()) {
                SHOWER = Optional.of(new BiomeAttenuatingSoundInstance(SpectrumBiomes.DEEP_DRIPSTONE_CAVES, SpectrumSoundEvents.SHOWER, 2F, false));
                client.getSoundManager().play(SHOWER.get());
            }
        }
    }
}
