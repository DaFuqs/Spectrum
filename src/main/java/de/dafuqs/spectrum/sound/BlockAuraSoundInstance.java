package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.sound.*;
import net.minecraft.registry.tag.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;

public class BlockAuraSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {

    public static final List<BlockAuraSoundInstance> INSTANCES = new ArrayList<>();
    private static final int MAX_DURATION = 80;
    private static int offsets = 0;

    private final MinecraftClient client = MinecraftClient.getInstance();
    private final int scaling;
    private final int tickOffset;
    private final TagKey<Block> checkedTag;
    private List<BlockPos> emitters;
    private int activeTicks = 1, interpTicks = 5;
    private boolean finished, active;
    private float lastCountMod, countMod;
    
    private BlockAuraSoundInstance(TagKey<Block> checkedTag, SoundEvent sound, int scaling, List<BlockPos> emitters) {
        super(sound, SoundCategory.AMBIENT, SoundInstance.createRandom());
        repeat = true;
        repeatDelay = 0;

        this.relative = false;
        this.emitters = emitters;
        this.scaling = scaling;
        this.tickOffset = offsets;
        this.active = true;
        this.checkedTag = checkedTag;
        offsets = (offsets + 1) % 20;

        updateAndVerifyEmitters(false);
        lastCountMod = countMod;
        updateVolumeAndPitch();
    }

    @Override
    public void tick() {
        var camera = client.getCameraEntity();

        if (camera == null || Math.sqrt(camera.getPos().squaredDistanceTo(x, y, z)) > 48) {
            finished = true;
            return;
        }

        if (interpTicks < 5)
            interpTicks++;

        if (emitters.isEmpty())
            active = false;

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

    private void updateAndVerifyEmitters(boolean allowRemovals) {
        double x = 0, y = 0, z = 0;

        if (allowRemovals) {
            emitters.removeIf(e -> !client.world.getBlockState(e).isIn(checkedTag));
        }

        if (emitters.isEmpty())
            return;

        for (BlockPos emitter : emitters) {
            x += emitter.getX();
            y += emitter.getY();
            z += emitter.getZ();
        }

        this.x = x / emitters.size();
        this.y = y / emitters.size();
        this.z = z / emitters.size();
        this.lastCountMod = countMod;
        this.countMod = (float) emitters.size() / scaling;
        interpTicks = 0;
    }

    private void updateVolumeAndPitch() {
        var heightMod = MathHelper.clamp((Math.abs(client.cameraEntity.getEyeY() - this.y) - 2F) / 32F, 0, 0.334F);
        var curCountMod = MathHelper.clampedLerp(lastCountMod, countMod, interpTicks / 5F);

        if (client.cameraEntity.getEyeY() < this.y)
            heightMod *= -1;

        this.volume = Math.max(0, ((float) activeTicks) / MAX_DURATION) * curCountMod * 2.5F;
        this.pitch = (float) (1 + heightMod);
    }

    private void rescan() {
        var maxRad = 12.0;
        var center = new Vec3d(x, y, z);

        for (BlockPos emitter : emitters) {
            var offset = emitter.toCenterPos().subtract(center);

            for (Direction.Axis axis : Direction.Axis.values()) {
                var mag = Math.abs(offset.getComponentAlongAxis(axis));
                if (mag > maxRad)
                    maxRad = mag;
            }
        }

        emitters = scanForBlocks(checkedTag, client.world, center, (int) Math.round(maxRad + 4));
        updateAndVerifyEmitters(false);
    }

    @Override
    public boolean isDone() {
        return activeTicks == 0 || emitters.isEmpty() || finished;
    }

    public static List<BlockPos> scanForBlocks(TagKey<Block> checkedTag, World world, Vec3d center, int radius) {
        var validPositions = new LinkedList<BlockPos>();

        for (BlockPos pos : BlockPos.iterateOutwards(BlockPos.ofFloored(center), radius, radius, radius)) {
            if (world.getBlockState(pos).isIn(SpectrumBlockTags.AZURITE_ORES)) {
                validPositions.add(pos.toImmutable());
            }
        }

        return validPositions;
    }

    public static List<BlockPos> scanForBlocks(TagKey<Block> checkedTag, World world, Vec3d center) {
        return scanForBlocks(checkedTag, world, center, 32);
    }

    public static boolean isNearPreexistingAura(Vec3d pos) {
        return INSTANCES.stream().anyMatch(instance ->
                Math.sqrt(pos.squaredDistanceTo(instance.x, instance.y, instance.z)) < 16);
    }

    public static void tryCreateNewInstance(TagKey<Block> checkedTag, SoundEvent sound, int scaling, List<BlockPos> aura) {
        var overlap = new HashSet<BlockPos>();

        for (BlockAuraSoundInstance instance : INSTANCES) {
            instance.emitters
                    .stream()
                    .filter(aura::contains)
                    .forEach(overlap::add);
        }

        // No more than a 15% overlap for instances
        if ((double) overlap.size() / aura.size() > 0.15)
            return;

        var instance = new BlockAuraSoundInstance(checkedTag, sound, scaling, aura);
        MinecraftClient.getInstance().getSoundManager().play(instance);
        INSTANCES.add(instance);
    }

    public static void update(World world) {
        INSTANCES.removeIf(BlockAuraSoundInstance::isDone);

        for (BlockAuraSoundInstance instance : INSTANCES) {
            var time = world.getTime() + instance.tickOffset;
            if (time % 200 == 0) {
                instance.rescan();
            }
            else if (time % 20 == 0) {
                instance.updateAndVerifyEmitters(true);
            }
        }
    }

    public static void clear() {
        INSTANCES.forEach(i -> i.finished = true);
        INSTANCES.clear();
    }
}