package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.sound.*;
import net.minecraft.entity.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;

public class CrystalAuraSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {

    public static Optional<CrystalAuraSoundInstance> INSTANCE = Optional.empty();
    private final MinecraftClient client = MinecraftClient.getInstance();
    private static final int MAX_DURATION = 80;
    private int activeTicks = 1, interpTicks = 5;
    private boolean finished, active;
    private float lastDistanceMod, distanceMod;
    private float lastCountMod, countMod;

    protected CrystalAuraSoundInstance(SoundEvent sound, float distMod, float countMod) {
        super(sound, SoundCategory.AMBIENT, SoundInstance.createRandom());
        repeat = true;
        repeatDelay = 0;
        this.distanceMod = distMod;
        this.countMod = countMod;
        this.relative = true;
        updateVolumeAndPitch();
    }

    @Override
    public void tick() {
        var camera = client.getCameraEntity();

        if (camera == null) {
            finished = true;
            return;
        }

        if (interpTicks < 5)
            interpTicks++;

        if (active && activeTicks < MAX_DURATION) {
            activeTicks++;
        }
        else if (!active && activeTicks > 0) {
            activeTicks--;
        }

        updateVolumeAndPitch();
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void updateState(float distanceMod , float countMod) {
        interpTicks = 0;
        this.lastDistanceMod = this.distanceMod;
        this.distanceMod = distanceMod;
        this.lastCountMod = this.countMod;
        this.countMod = countMod;
    }

    private void updateVolumeAndPitch() {
        var curDistanceMod = MathHelper.clampedLerp(lastDistanceMod, distanceMod, interpTicks / 5F);
        var curCountMod = MathHelper.clampedLerp(lastCountMod, countMod, interpTicks / 5F);

        this.volume = Math.max(0, ((float) activeTicks) / MAX_DURATION) * curDistanceMod * curCountMod;
    }

    @Override
    public boolean isDone() {
        return activeTicks == 0 || finished;
    }

    public static void update(World world, Entity player) {
        var client = MinecraftClient.getInstance();

        double x = 0, y = 0, z = 0;
        int count = 0;
        for (BlockPos pos : BlockPos.iterateOutwards(player.getBlockPos(), 16, 16, 16)) {
            if (world.getBlockState(pos).isIn(SpectrumBlockTags.AZURITE_ORES)) {
                x += pos.getX();
                y += pos.getY();
                z += pos.getZ();
                count++;
            }
        }

        Vec3d soundSource;
        if (count > 1) {
            soundSource = new Vec3d(x / count, y / count, z / count);
        }
        else {
            soundSource = new Vec3d(x, y, z);
        }

        if (INSTANCE.map(TickableSoundInstance::isDone).orElse(false)) {
            INSTANCE = Optional.empty();
        }

        if (count >= 40) {
            if (INSTANCE.isEmpty()) {
                INSTANCE = Optional.of(new CrystalAuraSoundInstance(SpectrumSoundEvents.CRYSTAL_AURA,
                        (float) MathHelper.clamp((soundSource.distanceTo(player.getPos()) - 3) / 24F, 0, 1),
                        MathHelper.clamp(count / 100F, 0, 1)
                        ));
                client.getSoundManager().play(INSTANCE.get());
            }
        }

        if (INSTANCE.isPresent()) {
            var instance = INSTANCE.get();
            instance.setActive(count > 0);
            instance.updateState(
                    1 - (float) MathHelper.clamp((soundSource.distanceTo(player.getPos()) - 3) / 24, 0, 1),
                    MathHelper.clamp(count / 100F, 0, 1)
            );
        }
    }

    public static int getTickTime() {
        return INSTANCE.isPresent() ? 5 : 80;
    }
}
