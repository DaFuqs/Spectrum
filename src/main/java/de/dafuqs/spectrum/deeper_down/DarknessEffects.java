package de.dafuqs.spectrum.deeper_down;

import com.google.common.collect.ImmutableMap;
import de.dafuqs.spectrum.registries.SpectrumBiomes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;

import java.util.Map;

public class DarknessEffects {

    public static final float INTERP_TICKS = 160;
    private static final Map<RegistryKey<Biome>, Float> INTERP_MULTIPLIERS;
    public static boolean isInDarkenedBiome;
    public static int darkenTicks, darken, lastDarkenTicks, interpInterpTicks;
    public static float interpTarget, interp, lastInterpTarget;
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

            interpTarget = INTERP_MULTIPLIERS.getOrDefault(biomeKey, 0F);
            interpInterpTicks = 0;
        }

        if (interpInterpTicks < Math.round(INTERP_TICKS / 1.5F)) {
            interpInterpTicks++;
        }

        interp = MathHelper.lerp((float) interpInterpTicks / Math.round(INTERP_TICKS / 1.5F), lastInterpTarget, interpTarget);

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
        var y = MathHelper.lerp(client.getTickDelta(), client.cameraEntity.lastRenderY, client.cameraEntity.getY());
        float adjustedInterp;

        if (y > -144) {
             adjustedInterp = (float) MathHelper.clampedLerp(0.175F, interp, (y + 64) / -80F);
        }
        else {
            adjustedInterp = interp;
        }

        if (y < -288) {
            adjustedInterp = (float) Math.max(interp, Math.min(0.85F, interp + y / -64F));
        }

        return adjustedInterp;
    }

    public static float getDarknessInterpolation() {
        return MathHelper.lerp(MinecraftClient.getInstance().getTickDelta(), (float) DarknessEffects.darkenTicks, DarknessEffects.lastDarkenTicks) / INTERP_TICKS * getInterp();
    }

    static {
        var builder = ImmutableMap.<RegistryKey<Biome>, Float>builder();
        builder.put(SpectrumBiomes.BLACK_LANGAST, 1F);
        builder.put(SpectrumBiomes.DEEP_BARRENS, 0.75F);
        builder.put(SpectrumBiomes.DEEP_DRIPSTONE_CAVES, 0.3F);
        builder.put(SpectrumBiomes.NOXSHROOM_FOREST, 0.2F);
        INTERP_MULTIPLIERS = builder.build();
    }
}
