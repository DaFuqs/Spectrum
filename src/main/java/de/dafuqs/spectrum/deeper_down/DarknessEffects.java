package de.dafuqs.spectrum.deeper_down;

import com.google.common.collect.ImmutableMap;
import de.dafuqs.spectrum.registries.SpectrumBiomes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;

import java.util.Map;

public class DarknessEffects {

    public static final float INTERP_TICKS = 160;
    private static final Map<RegistryKey<Biome>, Float> INTERP_MULTIPLIERS, FOG_MULTIPLIERS;
    private static final Map<RegistryKey<Biome>, float[]> TRANS_MULTIPLIERS;
    public static boolean isInDarkenedBiome;
    public static int darkenTicks, darken, lastDarkenTicks, interpInterpTicks;
    public static float interpTarget, interp, lastInterpTarget, fogTarget = 1F, fogDarkness = 1F,
            lastFogTarget = 1F, nearTarget = 1F, near = 1F, lastNearTarget = 1F, farTarget = 1F, far = 1F, lastFarTarget = 1F;
    public static RegistryEntry<Biome> currentBiome;
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static void clientTick(ClientWorld world, Entity cameraEntity, RegistryEntry<Biome> biome) {
        if (client.isPaused())
            return;

        lastDarkenTicks = darkenTicks;

        if (currentBiome == null || !currentBiome.getKey().equals(biome.getKey())) {
            var biomeKey = biome.getKey().orElse(null);
            currentBiome = biome;
            lastInterpTarget = interp;
            lastFogTarget = fogDarkness;
            lastNearTarget = near;
            lastFarTarget = far;

            interpTarget = INTERP_MULTIPLIERS.getOrDefault(biomeKey, 0F);
            fogTarget = FOG_MULTIPLIERS.getOrDefault(biomeKey, 1F);
            var targets = TRANS_MULTIPLIERS.getOrDefault(biomeKey, new float[]{1F, 1F});
            nearTarget = targets[0];
            farTarget = targets[1];
            interpInterpTicks = 0;
        }

        if (interpInterpTicks < Math.round(INTERP_TICKS / 1.5F)) {
            interpInterpTicks++;
        }

        interp = MathHelper.lerp((float) interpInterpTicks / Math.round(INTERP_TICKS / 1.5F), lastInterpTarget, interpTarget);
        fogDarkness = MathHelper.lerp((float) interpInterpTicks / Math.round(INTERP_TICKS / 1.5F), lastFogTarget, fogTarget);
        near = MathHelper.lerp((float) interpInterpTicks / Math.round(INTERP_TICKS / 1.5F), lastNearTarget, nearTarget);
        far = MathHelper.lerp((float) interpInterpTicks / Math.round(INTERP_TICKS / 1.5F), lastFarTarget, farTarget);

        isInDarkenedBiome = INTERP_MULTIPLIERS.containsKey(biome.getKey().orElse(null));
        if (isInDarkenedBiome) {
            if (darkenTicks < INTERP_TICKS) {
                darkenTicks++;
            }
            else if (darkenTicks > INTERP_TICKS) {
                darkenTicks--;
            }

        }
        else if (darkenTicks > 0) {
            darkenTicks--;
        }
    }

    public static float getInterp() {
        if (client.cameraEntity == null)
            return interp;

        var y = MathHelper.lerp(client.getTickDelta(), client.cameraEntity.lastRenderY, client.cameraEntity.getY());
        float adjustedInterp;

        //entrance darkening
        if (y > -116) {
             adjustedInterp = (float) MathHelper.clampedLerp(0.175F, interp, (y + 64) / -52F);
        }
        //depth darkening
        else if (y < -256) {
            adjustedInterp = (float) Math.max(interp, Math.min(0.725F, interp + (y + 256) / -196F));
        }
        else {
            adjustedInterp = interp;
        }

        return adjustedInterp;
    }

    public static float getNear(float start) {
        if (client.cameraEntity == null)
            return near;

        var y = MathHelper.lerp(client.getTickDelta(), client.cameraEntity.lastRenderY, client.cameraEntity.getY());
        float distance;

        if (y < -272) {
            distance = (float) MathHelper.clampedLerp(0F, 1.334F, (y + 272) / -48) * near;
        }
        else {
            distance = near;
        }

        return distance * start;
    }

    public static float getFar(float end) {
        if (client.cameraEntity == null)
            return far;

        return far * end;
    }

    public static float getDarknessInterpolation() {
        return MathHelper.lerp(MinecraftClient.getInstance().getTickDelta(), (float) DarknessEffects.darkenTicks, DarknessEffects.lastDarkenTicks) / INTERP_TICKS * getInterp();
    }

    static {
        var builder = ImmutableMap.<RegistryKey<Biome>, Float>builder();
        builder.put(SpectrumBiomes.BLACK_LANGAST, 0.7F);
        builder.put(SpectrumBiomes.DEEP_BARRENS, 0.65F);
        builder.put(SpectrumBiomes.DEEP_DRIPSTONE_CAVES, 0.2F);
        builder.put(SpectrumBiomes.NOXSHROOM_FOREST, 0.125F);
        INTERP_MULTIPLIERS = builder.build();

        var fogBuilder = ImmutableMap.<RegistryKey<Biome>, Float>builder();
        fogBuilder.put(SpectrumBiomes.NOXSHROOM_FOREST, 0.125F);
        fogBuilder.put(SpectrumBiomes.DEEP_DRIPSTONE_CAVES, 0.25F);
        fogBuilder.put(SpectrumBiomes.DEEP_BARRENS, 0.55F);
        fogBuilder.put(SpectrumBiomes.BLACK_LANGAST, 0.0125F);
        FOG_MULTIPLIERS = fogBuilder.build();

        var transMultiplier = ImmutableMap.<RegistryKey<Biome>, float[]>builder();
        transMultiplier.put(SpectrumBiomes.NOXSHROOM_FOREST, new float[]{1.25F, 1F});
        transMultiplier.put(SpectrumBiomes.HOWLING_SPIRES, new float[]{-10F, 0.85F});
        transMultiplier.put(SpectrumBiomes.DEEP_BARRENS, new float[]{-1F, 0.5F});
        transMultiplier.put(SpectrumBiomes.BLACK_LANGAST, new float[]{0.8F, 0.8F});
        transMultiplier.put(SpectrumBiomes.DRAGONROT_SWAMP, new float[]{-5F, 0.8F});
        TRANS_MULTIPLIERS = transMultiplier.build();
    }
}
